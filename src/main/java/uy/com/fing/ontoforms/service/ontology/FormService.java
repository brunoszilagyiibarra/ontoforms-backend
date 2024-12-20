package uy.com.fing.ontoforms.service.ontology;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uy.com.fing.ontoforms.configuration.ConfigurationService;
import uy.com.fing.ontoforms.controller.PropertyDescriptor;
import uy.com.fing.ontoforms.service.ontology.persistence.OntologyRepository;
import uy.com.fing.ontoforms.service.ontology.processors.RDFNodePrettyLabelProcessor;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FormService {

    private OntologyService ontologyService;

    private IndividualsService individualsService;
    private final OntologyRepository ontologyRepository;
    private final RDFNodePrettyLabelProcessor prettyLabelProcessor;
    private final ConfigurationService configurationService;

    public Form getForm(String ontoId, String classUri, boolean embeddedForm) {
        OntClass ontClass = ontologyRepository.getOntClassFrom(ontoId, classUri);
        return getForm(ontoId, ontClass, embeddedForm);
    }

    private Form getForm(String ontoId, OntClass ontClass, boolean embeddedForm) {
        List<OntClass> subclasses = ontClass.listSubClasses(true).toList();

        Form form;

        //Si no hay sub-classes entonces no es necesario armar secciones.
        if(CollectionUtils.isEmpty(subclasses) || !embeddedForm) {
            form = buildFormSection(ontoId, ontClass);
        } else {
            form = new Form(ontClass.getURI(), prettyLabelProcessor.apply(ontClass));
            //necesito heredar las propiedades del padre y agregar las de los hijos
            for (OntClass subClass: subclasses) {

                Form subForm = getForm(ontoId, subClass, true);

                if(subForm != null) {
                    form.addSubSection(subForm);
                }
            }
        }

        return form;
    }

    /**
     * Retorna las propiedades (Frame-Like) de una clase en una estructura de sección.
     * Si es vacìo entonces retorna mapa vacìo.
     * @param ontClass
     * @return
     */
    public Form buildFormSection(String ontoId, OntClass ontClass) {
        var formSectionFields = new ArrayList<Form.FormField>();

        var mainClassUri = ontClass.getURI();
        var props = ontologyService.getObjectAndDataPropsForClass(ontoId, mainClassUri);

        var form = new Form(mainClassUri, configurationService.getTranslationFor(ontoId, mainClassUri, prettyLabelProcessor.apply(ontClass)),
                formSectionFields);

        for (PropertyDescriptor prop: props) {

            if(configurationService.isPropertyCalculated(ontoId, prop.getPropUri(), mainClassUri)) {
                log.info("la propiedad {} está excluida del formulario por defecto ya que se asume razonada", prop.getPropUri());
                continue;
            }

            final Form.FormField field;
            switch (prop.getPropType()) {
                case "Data Prop": {
                    field = buildFormDataField(ontoId, prop);
                    break;
                }
                case "Object Prop": {

                    if(prop.getRangeClass() != null &&
                            !prop.getRangeClass().isAnon() &&
                            configurationService.isArtificeClass(ontoId, prop.getRangeClass().getURI(), mainClassUri)) {

                        Form formsFromClass = getForm(ontoId, prop.getRangeClass().asClass(), true);

                        form.addSubSection(formsFromClass);

                        continue;
                    } else {
                        field = buildFormObjectField(ontoId, prop);
                    }
                    break;
                }
                default:
                    throw new RuntimeException("Not Valid property" + prop);
            }

            formSectionFields.add(field);
        }


        return props.isEmpty() ? null : form;
    }

    private Form.ObjectField buildFormObjectField(String ontoId, PropertyDescriptor prop) {
        var propLabel = configurationService.getTranslationFor(ontoId, prop.getPropUri(), prop.getPropLabel());

        Resource rangeClass = prop.getRangeClass() == null ? ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Thing") : prop.getRangeClass().asClass();

        List<IndividualsService.IndividualDescriptor> ontologyClassIndividuals = individualsService.getOntologyClassIndividuals(ontoId, rangeClass);

        List<Form.FieldOption> options = ontologyClassIndividuals.stream()
                .map(ind -> new Form.FieldOption(ind.prettyName(), ind.uri())).toList();

        return Form.ObjectField.builder()
                .label(propLabel)
                .uri(prop.getPropUri())
                .singleOption(prop.isFunctional())
                .options(options)
                .build();

    }

    private Form.DatatypeField buildFormDataField(String ontoId, PropertyDescriptor prop) {
        var propLabel = configurationService.getTranslationFor(ontoId, prop.getPropUri(), prop.getPropLabel());

        OntResource rangeClass = prop.getRangeClass();

        String text = "<<desconocido>>";
        if(rangeClass != null) {
            text = prettyLabelProcessor.apply(rangeClass);
        }

        return Form.DatatypeField.builder()
                .label(propLabel)
                .uri(prop.getPropUri())
                .datatype(text)
                .build();
    }

}
