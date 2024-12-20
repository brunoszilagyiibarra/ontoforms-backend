package uy.com.fing.ontoforms.service.ontology;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;
import uy.com.fing.ontoforms.controller.PropertyDescriptor;
import uy.com.fing.ontoforms.service.ontology.persistence.OntologyRepository;
import uy.com.fing.ontoforms.service.ontology.processors.RDFNodePrettyLabelProcessor;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class OntologyService {

    private static final String DATA_PROP_TYPE = "Data Prop";
    private static final String OBJECT_PROP_TYPE = "Object Prop";
    public static final String UNDEFINED = "<<Undefined>>";

    private final OntologyRepository ontologyRepository;
    private final RDFNodePrettyLabelProcessor prettyLabelProcessor;


    /**
     * Genera un named-graph para la ontologìa.
     * @param ontologyName nombre de la ontologìa
     * @param ontologyContent grafo
     * @return nombre uri.
     */
    public String createOntology(String ontologyName, byte[] ontologyContent) {
        return ontologyRepository.createOntology(ontologyName, ontologyContent);
    }


    /**
     * @return lista con las top-level classes de la ontología.
     */
    public OntoTree getOntologyTopLevelClasses(String ontoId) {

        OntModel ontoModel = ontologyRepository.getOntologyModelForTBoxById(ontoId);

        OntClass thingClass = ontoModel.getOntClass(OWL.Thing.getURI());
        OntoTree classesTree = new OntoTree(new OntoTree.OntoNode(thingClass.getLocalName(), thingClass.getURI()));

        Map<String, Set<OntClass>> ontClassListMap = listAllEquivalentClassesWithPair(ontoModel);

        ontologyRepository.getFirstLevelClassesByOntoId(ontoId)
                .stream().sorted(Comparator.comparing(prettyLabelProcessor))
                .forEach( clazz -> {
                    OntoTree subTree = classesTree.addChildren(new OntoTree.OntoNode(prettyLabelProcessor.apply(clazz), clazz.getURI()));
                    listSubClasses(clazz, subTree, ontClassListMap);
                }
                );

        return classesTree;
    }

    private void listSubClasses(OntClass ontClass, OntoTree tree, Map<String, Set<OntClass>> ontClassListMap) {

        Set<OntClass> directSubclasses = ontClass.listSubClasses(true).toSet();
        Set<OntClass> c = ontClassListMap.get(ontClass.getURI());
        if(c != null)
            directSubclasses.addAll(c);

        var sortedList = directSubclasses.stream().sorted(Comparator.comparing(prettyLabelProcessor)).toList();

        for (OntClass subClass : sortedList) {
            log.info("label: {}, class-name: {}, uri: {}", prettyLabelProcessor.apply(subClass),
                    subClass.getLocalName(), subClass.getURI());

            var subTree = tree.addChildren(new OntoTree.OntoNode(prettyLabelProcessor.apply(subClass), subClass.getURI()));

            //Recursión sobre los hijos.
            listSubClasses(subClass, subTree, ontClassListMap);
        }

    }


    public List<PropertyDescriptor> getObjectProperties(String ontoId) {
        return ontologyRepository.getObjectPropertiesByOntoId(ontoId).stream()
                .map(this::getPropertyDescriptor).toList();
    }

    public List<PropertyDescriptor> getDataProperties(String ontoId) {
        return ontologyRepository.getDatatypePropertiesByOntoId(ontoId).stream()
                .map(this::getPropertyDescriptor).toList();
    }

    public List<PropertyDescriptor> getObjectAndDataPropsForClass(String ontoId, String classUri) {
        return ontologyRepository.getPropertiesByOntoIdAndClassUri(ontoId, classUri).stream()
                .map(this::getPropertyDescriptor).toList();
    }

    private PropertyDescriptor getPropertyDescriptor(OntProperty p) {
        String propType = p.isObjectProperty() ? OBJECT_PROP_TYPE : DATA_PROP_TYPE;
        String propName = prettyLabelProcessor.apply(p);
        String domain = p.getDomain() != null ? prettyLabelProcessor.apply(p.getDomain()) : UNDEFINED;
        String range = p.getRange() != null ? prettyLabelProcessor.apply(p.getRange()) : UNDEFINED;

        log.info("Property ID: Uri {}, Label {}, LocalName {} // Meta: Domain {}, Range {}, DataProp {}, ObjectProp {}",
                p.getURI(), p.getLabel(null), p.getLocalName(), p.getDomain(), p.getRange(), p.isDatatypeProperty(),
                p.isObjectProperty());

        return new PropertyDescriptor(propName, p.getURI(), p.getDomain(), p.getRange(), domain, range, propType, p.isFunctionalProperty(),
                p.isInverseFunctionalProperty());
    }

    public Map<String, Set<OntClass>> listAllEquivalentClassesWithPair(OntModel ontModel) {

        Map<String, Set<OntClass>> equivalent = new HashMap<>();

        List<OntClass> classesWithEquiv = ontModel.listClasses().toList().stream()
                .filter(c -> c.listEquivalentClasses() != null)
                .toList();

        for (OntClass classWithEquiv: classesWithEquiv) {

            try{
                classWithEquiv.listEquivalentClasses().toList().forEach(
                        equivClass -> {
                            System.out.println("Equivalent Classes:");
                            System.out.println("Processing Blank Node: " + equivClass.asResource().getId().getLabelString());
                            System.out.println("Original Class: " + classWithEquiv.getLocalName());
                            // Iterate over statements related to the blank node
                            Statement property = equivClass.asResource().getProperty(OWL.intersectionOf);
                            if(property != null) {
                                RDFNode relatedClass = property.getProperty(RDF.first).getObject();
                                System.out.println(" Related clazz: " + relatedClass.asNode().getLocalName());
                                System.out.println(" Related URI: " + relatedClass.asNode().getURI());


                                OntClass originalClazz = ontModel.getOntClass(relatedClass.asNode().getURI());

                                Set<OntClass> ontClasses = equivalent.get(originalClazz.getURI());

                                if(ontClasses == null) {
                                    Set<OntClass> value = new HashSet<>();
                                    value.add(classWithEquiv);
                                    equivalent.put(originalClazz.getURI(), value);
                                } else {
                                    ontClasses.add(classWithEquiv);
                                }
                            }

                        }
                );
            } catch (Exception e) {
                log.error("Error calculando equiv", e);

            }
        }

        return equivalent;
    }

}
