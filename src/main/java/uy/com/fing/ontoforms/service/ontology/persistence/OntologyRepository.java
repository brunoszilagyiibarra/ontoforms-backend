package uy.com.fing.ontoforms.service.ontology.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.writer.RDFJSONWriter;
import org.springframework.stereotype.Service;
import uy.com.fing.ontoforms.service.ontology.persistence.client.FusekiTripleStoreClient;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OntologyRepository {

    private final FusekiTripleStoreClient tripleStore;


    /**
     * Guarda una nueva ontología en el sistema.
     *
     * @param ontologyName
     * @param ontologyContent
     * @return
     */
    public String createOntology(String ontologyName, byte[] ontologyContent) {
        //Genero un modelo de ontología para leer el archivo y poder persistirlo.

        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
        //Parse hasta que tengamos éxito.
        boolean parsingSuccessful = false;
        List<Lang> rdfLanguages = Arrays.asList(Lang.RDFXML, Lang.TURTLE, Lang.NTRIPLES, Lang.JSONLD);
        for(Lang lang : rdfLanguages) {

            try {
                ontoModel.read(new ByteArrayInputStream(ontologyContent), null, lang.getLabel());

                // Parsing successful, break out of the loop
                parsingSuccessful = true;
                log.error("Success parsing RDF with " + lang.getName());
                break;
            } catch (Exception e) {
                // RDF parsing failed with this language, try the next one
                log.error("Error parsing RDF with " + lang.getName() + ": " + e.getMessage());
            }

        }

        if(parsingSuccessful ){
            tripleStore.saveConfiguration(ModelFactory.createOntologyModel(), ontologyName);
            tripleStore.saveModelWithIndividuals(ModelFactory.createOntologyModel(), ontologyName);
            return tripleStore.saveOntology(ontoModel, ontologyName);
        } else {
            throw new RuntimeException("Cannot parse ontology, invalid format");
        }

    }

    public void saveConfigurationModel(OntModel ontModel, String ontoId) {
        tripleStore.saveConfiguration(ontModel, ontoId);
    }

    /**
     * Obtiene todas las object properties de una ontología dado su identificador.
     * @param ontoId identificador de la ontología (filename)
     * @return lista de object properties.
     */
    public List<ObjectProperty> getObjectPropertiesByOntoId(String ontoId) {
        return getOntologyModelForTBoxById(ontoId).listObjectProperties().toList();
    }


    /**
     * Obtiene todas las datatypeProperty de una ontología dado su identificador.
     * @param ontoId identificador de la ontología (filename)
     * @return lista de datatypeproperties.
     */
    public List<DatatypeProperty> getDatatypePropertiesByOntoId(String ontoId) {
        return getOntologyModelForTBoxById(ontoId).listDatatypeProperties().toList();
    }

    /**
     * Obtiene todas las pripiedades de una clase dentro de una ontología usando la estrategia Frame-Like
     * para asignar las propiedades a la clase.
     * @param ontoId identificador de la ontología (filename)
     * @param classUri identificador de la clase.
     * @return Listado de las ontoProperties.
     */
    public List<OntProperty> getPropertiesByOntoIdAndClassUri(String ontoId, String classUri) {
        OntModel model = getOntologyModelForTBoxById(ontoId);
        return model.getOntClass(classUri).listDeclaredProperties()
                .filterKeep(p -> p.isObjectProperty() || p.isDatatypeProperty())
                .toList();
    }


    /**
     * Obtiene todas las clases de primer nivel de la ontología dada por su identificador.
     * Las clases de primer nivel son aquellas que no tienen super-clase, y también que no tienen
     * equivalencia.
     *
     * @param ontoId identificador de la ontología (filename)
     * @return lista de OntClass de primer nivel.
     */
    public List<OntClass> getFirstLevelClassesByOntoId(String ontoId) {
        return getOntologyModelForTBoxById(ontoId).listNamedClasses().toList().stream()
                .filter(c -> !c.isIntersectionClass()) //clases interseccion las considero como sub-classes.
                .filter(c -> {
                    if(c.isHierarchyRoot()) {
                        return true;
                    }

                    List<OntClass> superClasses = c.listSuperClasses(true).toList();
                    boolean valid = false;

                    for (OntClass superClass: superClasses) {

                        if(superClass.isRestriction()) {
                            Restriction restrictionClass = superClass.asRestriction();

                            if(restrictionClass.isIntersectionClass()) {
                                valid = false;
                                break;
                            }

                            valid = true;

                        } else {
                            //Caso en donde tenemos un bucle, las dos clases deben ser de primer nivel.
                            if(superClass.listSuperClasses().toList().size() == 1 && superClass.listSuperClasses().toList().get(0).equals(c)) {
                                valid = true;
                            } else {
                                valid = false;
                                break;
                            }
                        }
                    }

                    return valid;
                })
                .filter(c-> {
                    OntClass equivalentClass = c.getEquivalentClass();

                    if(equivalentClass == null) {
                        return true;
                    } else if(equivalentClass.canAs(Restriction.class)) {
                        Restriction restriction = equivalentClass.asRestriction();

                        return !restriction.isIntersectionClass();

                    } else {
                        return false;
                    }

                })
                .toList();
    }


    /**
     * Obtiene todas las clases de la ontología.
     * @param ontoId identificador de la ontología (filename)
     * @return lista de OntClass de la ontología.
     */
    public List<OntClass> getAllNamedClassesByOntoId(String ontoId) {
        return getOntologyModelForTBoxById(ontoId).listNamedClasses().toList();
    }

    /**
     * Obtiene todos los individuos de una ontología.
     * @param ontoId ontoId identificador de la ontología (filename)
     * @return lista de individuos de una ontología.
     */
    public List<Individual> getIndividualsByOntoId(String ontoId) {
        return getOntologyModelABoxByIdFor(ontoId).listIndividuals().toList();
    }

    /**
     * Obtiene todos los individuos de una clase con URI.
     *
     * @param ontoId identificador de la ontología (filename)
     * @param classUri uri de la clase
     * @return los individuos de esa clase
     */
    public List<Individual> getIndividualsByOntoIdAndClassUri(String ontoId, String classUri) {
        if(ontoId == null || classUri == null)
            throw new IllegalArgumentException("classUri o ontoId es nulo y no se permite");

        return getIndividualsByOntoIdAndResource(ontoId, ResourceFactory.createResource(classUri));
    }

    /**
     * Obtiene todos los individuos de un recurso.
     * @param ontoId identificador de la ontología (filename)
     * @param resource recurso para el cuál buscar los individuos
     * @return lista de individuos
     */
    public List<Individual> getIndividualsByOntoIdAndResource(String ontoId, Resource resource) {
        if(ontoId == null || resource == null)
            throw new IllegalArgumentException("resource o ontoId es nula y no se permite");

        return getOntologyModelABoxByIdFor(ontoId).listIndividuals(resource).toList();
    }

    public OntClass getOntClassFrom(String ontoId, String classUri) {
        return getOntologyModelForTBoxById(ontoId).getOntClass(classUri);
    }


    /**
     * Genera un ontology model a partir de un Id de ontologìa.
     * Esta función se utiliza para recuperar el TBox y por tanto tiene un razonador
     * específico.
     *
     * @param ontoId identificador de la ontología (filename)
     * @return modelo.
     */
    public OntModel getOntologyModelForTBoxById(String ontoId) {
        return getOntologyModelById(ontoId, OntModelSpec.OWL_MEM_TRANS_INF);
    }

    /**
     * Genera un ontology model a partir de un Id de ontologìa.
     * Esta función se utiliza para recuperar el ABox y por tanto tiene un razonador
     * específico.
     *
     * @param ontoId identificador de la ontología (filename)
     * @return modelo.
     */
    public OntModel getOntologyModelABoxByIdFor(String ontoId){
        return getOntologyModelById(ontoId, PelletReasonerFactory.THE_SPEC);
    }

    private OntModel getOntologyModelById(String ontoId, OntModelSpec ontModelSpec) {
        Model model = tripleStore.getOntologyByName(ontoId);
        OntModel ontologyModel = ModelFactory.createOntologyModel(ontModelSpec, model);
        ontologyModel.setStrictMode(false); //TODO: Algunas definiciones de ontologías hacen que falle apache Jena.
        return ontologyModel;
    }

    public OntModel getModelForOntologyConfiguration(String ontoId) {
        Model model = tripleStore.getConfigurationsModelFor(ontoId);
        OntModel ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF, model);
        ontologyModel.setStrictMode(false); //TODO: Algunas definiciones de ontologías hacen que falle apache Jena.
        return ontologyModel;
    }

    /**
     * Lista todas las ontologìas persistidas en el sistema.
     * @return Lista con los identificadores de todas las ontogías.
     */
    public List<FusekiTripleStoreClient.OntologyName> listAllOntologiesNames() {
        return tripleStore.getAllOntologyNames();
    }

    public List<String> listAllConfigurationsGraphs() {
        return tripleStore.allGraphNamesConfig();
    }

    /**
     * @return el modelo completo de la ontologìa como JsonString
     */
    public String getOntologyRDFJSON(String ontoId) {
        var ontoModel = getOntologyModelForTBoxById(ontoId);

        StringWriter out = new StringWriter();
        RDFJSONWriter.output(out, ontoModel.getGraph());

        return out.toString();
    }

}
