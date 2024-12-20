package uy.com.fing.ontoforms.configuration;

import lombok.AllArgsConstructor;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.springframework.stereotype.Service;
import uy.com.fing.ontoforms.service.ontology.persistence.OntologyRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ConfigurationService {


    private final OntologyRepository ontologyRepository;

    private static final String ONTO_CONFIG_CLASS = "http://www.ontoforms.com/config";
    private static final String ONTO_CONFIG_INDIVIDUAL = "http://www.ontoforms.com/config#main_config";

    private static final String CONFIG_HAS_ARTIFICE_CLASS_PROP = "http://www.ontoforms.com/config/has_artifice_class";
    private static final String CONFIG_HAS_TRANSLATION_PROP = "http://www.ontoforms.com/config/has_translation";
    private static final String CONFIG_HAS_CALCULATED_PROP = "http://www.ontoforms.com/config/has_calculated_property";
    private static final String CONFIG_HAS_APP_ONTO_MAPPING_PROP = "http://www.ontoforms.com/config/has_app_onto_mapping";

    private static final String ARTIFICE_CLASS_CONFIG_CLASS = "http://www.ontoforms.com/config/artifice_class";
    private static final String ARTIFICE_CLASS_CONFIG_FOR_MAIN_CLASS = "http://www.ontoforms.com/config/for_main_class";
    private static final String ARTIFICE_CLASS_CONFIG_ARTIFICE_CLASS_URI = "http://www.ontoforms.com/config/artifice_class_uri";

    private static final String RESOURCE_LABEL_TRANSLATION_CONFIG_CLASS = "http://www.ontoforms.com/config/resource_label_translation_class";
    private static final String TRANSLATION_FOR_PROP = "http://www.ontoforms.com/config/translation_for";
    private static final String HAS_TRANSLATION_VALUE_PROP = "http://www.ontoforms.com/config/has_translation_value";

    private static final String CALCULATED_PROP_CONFIG_CLASS = "http://www.ontoforms.com/config/calculated_property_class";
    private static final String CALCULATED_PROP_CONFIG_FOR_MAIN_CLASS = "http://www.ontoforms.com/config/calc_prop_for_main_class";
    private static final String CALCULATED_PROP_CONFIG_CLASS_URI = "http://www.ontoforms.com/config/calc_prop_uri";

    private static final String APP_ONTO_MAPPING_CONFIG_CLASS = "http://www.ontoforms.com/config/app_onto_mapping_class";
    private static final String HAS_RELATED_APP = "http://www.ontoforms.com/config/has_related_app";

    public record TranslationConfigDescriptor(String propertyUri, String propertyLabelTranslation) {}
    public record ArtificeClassConfigDescriptor(String classUri, String mainClassUri) {}
    public record CalculatedPropertyConfigDescriptor(String propUri, String mainClass) {}

    public void addAppOntologyRelation(String ontoId, String appName) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        var configurationIndividual = getOrCreateOntoConfigIndividual(model);

        Individual appRel = model.createIndividual(getOrCreateOntclass(model, APP_ONTO_MAPPING_CONFIG_CLASS));
        appRel.addProperty(getOrCreateProperty(model, HAS_RELATED_APP), appName);

        configurationIndividual.addProperty(getOrCreateProperty(model, CONFIG_HAS_APP_ONTO_MAPPING_PROP), appRel);

        ontologyRepository.saveConfigurationModel(model, ontoId);
    }

    public List<AppDescriptor> getAppOntologyRelationConfig(String ontoId) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        return getOrCreateOntclass(model, APP_ONTO_MAPPING_CONFIG_CLASS).listInstances().mapWith(i ->
            i.getPropertyValue(getOrCreateProperty(model, HAS_RELATED_APP)).asLiteral().getString())
                .mapWith(AppDescriptor::new).toList();
    }

    public List<String> getAppOntologyRelationConfigFromApp(String app) {
        List<String> ontologyIds = ontologyRepository.listAllConfigurationsGraphs();

        Set<String> ontoIdsThatHaveAppConfigured = new HashSet<>();
        for (String ontoId: ontologyIds) {
            if(getAppOntologyRelationConfig(ontoId).contains(new AppDescriptor(app))) {
                ontoIdsThatHaveAppConfigured.add(ontoId);
            }
        }

        return ontoIdsThatHaveAppConfigured.stream().toList();
    }

    public void deleteAppOntologyRelationConfig(String ontoId, String appname) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);

        getOrCreateOntclass(model, APP_ONTO_MAPPING_CONFIG_CLASS).listInstances().filterKeep(i ->
            appname.equals(i.getPropertyValue(getOrCreateProperty(model, HAS_RELATED_APP)).asLiteral().getString())
        ).nextOptional().ifPresent(
                foundIndividual -> {
                    foundIndividual.remove();
                    ontologyRepository.saveConfigurationModel(model, ontoId);
                }
        );
    }


    public void addCalculatedProperty(String ontoId, String propUri, String mainClassUri) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        var configurationIndividual = getOrCreateOntoConfigIndividual(model);

        Individual calcPropInd = model.createIndividual(getOrCreateOntclass(model, CALCULATED_PROP_CONFIG_CLASS));
        calcPropInd.addProperty(getOrCreateProperty(model, CALCULATED_PROP_CONFIG_FOR_MAIN_CLASS), mainClassUri);
        calcPropInd.addProperty(getOrCreateProperty(model, CALCULATED_PROP_CONFIG_CLASS_URI), propUri);

        configurationIndividual.addProperty(getOrCreateProperty(model, CONFIG_HAS_CALCULATED_PROP), calcPropInd);

        ontologyRepository.saveConfigurationModel(model, ontoId);
    }

    public List<CalculatedPropertyConfigDescriptor> getCalculatedPropertyConfig(String ontoId) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        return getOrCreateOntclass(model, CALCULATED_PROP_CONFIG_CLASS).listInstances().mapWith(i -> {
            String mainClass = i.getPropertyValue(getOrCreateProperty(model, CALCULATED_PROP_CONFIG_FOR_MAIN_CLASS)).asLiteral().getString();
            String propUri = i.getPropertyValue(getOrCreateProperty(model, CALCULATED_PROP_CONFIG_CLASS_URI)).asLiteral().getString();
            return new CalculatedPropertyConfigDescriptor(propUri, mainClass);
        }).toList();
    }

    public boolean isPropertyCalculated(String ontoId, String propUri, String mainClassUri) {
        return getCalculatedPropertyConfig(ontoId).stream().anyMatch(i -> mainClassUri.equals(i.mainClass())
                && propUri.equals(i.propUri()));
    }

    public void deleteCalculatedPropConfig(String ontoId, String propUri, String mainClassUri) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);

        getOrCreateOntclass(model, CALCULATED_PROP_CONFIG_CLASS).listInstances().filterKeep(indi -> {
            String mainClass = indi.getPropertyValue(getOrCreateProperty(model, CALCULATED_PROP_CONFIG_FOR_MAIN_CLASS))
                    .asLiteral().getString();
            String propUriSaved = indi.getPropertyValue(getOrCreateProperty(model, CALCULATED_PROP_CONFIG_CLASS_URI))
                    .asLiteral().getString();
            return mainClassUri.equals(mainClass) && propUri.equals(propUriSaved);
        }).nextOptional().ifPresent(
                foundIndividual -> {
                    foundIndividual.remove();
                    ontologyRepository.saveConfigurationModel(model, ontoId);
                }
        );
    }

    public void addTranslationForResourceLabel(String ontoId, String resourceUri, String translatedLabel) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        var configurationIndividual = getOrCreateOntoConfigIndividual(model);

        Individual translationConfigInd = model.createIndividual(getOrCreateOntclass(model, RESOURCE_LABEL_TRANSLATION_CONFIG_CLASS));
        translationConfigInd.addProperty(getOrCreateProperty(model, TRANSLATION_FOR_PROP), resourceUri);
        translationConfigInd.addProperty(getOrCreateProperty(model, HAS_TRANSLATION_VALUE_PROP), translatedLabel);

        configurationIndividual.addProperty(getOrCreateProperty(model, CONFIG_HAS_TRANSLATION_PROP), translationConfigInd);

        ontologyRepository.saveConfigurationModel(model, ontoId);
    }


    public List<TranslationConfigDescriptor> getTranslationsConfig(String ontoId) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        return getOrCreateOntclass(model, RESOURCE_LABEL_TRANSLATION_CONFIG_CLASS).listInstances().mapWith(i -> {
            String translationFor = i.getPropertyValue(getOrCreateProperty(model, TRANSLATION_FOR_PROP)).asLiteral().getString();
            String translatedValue = i.getPropertyValue(getOrCreateProperty(model, HAS_TRANSLATION_VALUE_PROP)).asLiteral().getString();
            return new TranslationConfigDescriptor(translationFor, translatedValue);
        }).toList();
    }


    public String getTranslationFor(String ontoId, String resourceUri, String defaultValue) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        return getOrCreateOntclass(model, RESOURCE_LABEL_TRANSLATION_CONFIG_CLASS).listInstances()
                .filterKeep(i -> resourceUri.equals(i.getPropertyValue(getOrCreateProperty(model, TRANSLATION_FOR_PROP))
                            .asLiteral().getString()))
                .mapWith(i -> i.getPropertyValue(getOrCreateProperty(model, CONFIG_HAS_TRANSLATION_PROP))
                            .asLiteral().getString())
                .nextOptional().orElse(defaultValue);
    }

    public void deleteTranslationFor(String ontoId, String resourceUri) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);

        getOrCreateOntclass(model, RESOURCE_LABEL_TRANSLATION_CONFIG_CLASS).listInstances()
                .filterKeep(i -> resourceUri.equals(i.getPropertyValue(getOrCreateProperty(model, TRANSLATION_FOR_PROP))
                .asLiteral().getString()))
                .nextOptional()
                .ifPresent(foundIndividual -> {
                    foundIndividual.remove();
                    ontologyRepository.saveConfigurationModel(model, ontoId);
                }
        );
    }

    public void addArtificeClass(String ontoId, String artificeClassUri, String mainClassUri) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        var configurationIndividual = getOrCreateOntoConfigIndividual(model);

        Individual artificeClassConfigIndividual = model.createIndividual(getOrCreateOntclass(model, ARTIFICE_CLASS_CONFIG_CLASS));
        artificeClassConfigIndividual.addProperty(getOrCreateProperty(model, ARTIFICE_CLASS_CONFIG_FOR_MAIN_CLASS), mainClassUri);
        artificeClassConfigIndividual.addProperty(getOrCreateProperty(model, ARTIFICE_CLASS_CONFIG_ARTIFICE_CLASS_URI), artificeClassUri);

        configurationIndividual.addProperty(getOrCreateProperty(model, CONFIG_HAS_ARTIFICE_CLASS_PROP), artificeClassConfigIndividual);

        ontologyRepository.saveConfigurationModel(model, ontoId);
    }

    public List<ArtificeClassConfigDescriptor> getArtificeClassesConfig(String ontoId) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);
        return getOrCreateOntclass(model, ARTIFICE_CLASS_CONFIG_CLASS).listInstances().mapWith(i -> {
            String mainClass = i.getPropertyValue(getOrCreateProperty(model, ARTIFICE_CLASS_CONFIG_FOR_MAIN_CLASS))
                    .asLiteral().getString();
            String artificeClassUri = i.getPropertyValue(getOrCreateProperty(model, ARTIFICE_CLASS_CONFIG_ARTIFICE_CLASS_URI))
                    .asLiteral().getString();
            return new ArtificeClassConfigDescriptor(artificeClassUri, mainClass);
        }).toList();
    }

    public void deleteArtificeClassConfig(String ontoId, String classUri, String mainClassUri) {
        var model = ontologyRepository.getModelForOntologyConfiguration(ontoId);

        getOrCreateOntclass(model, ARTIFICE_CLASS_CONFIG_CLASS).listInstances().filterKeep(indi -> {
            String mainClass = indi.getPropertyValue(getOrCreateProperty(model, ARTIFICE_CLASS_CONFIG_FOR_MAIN_CLASS))
                    .asLiteral().getString();
            String artificeClassUri = indi.getPropertyValue(getOrCreateProperty(model, ARTIFICE_CLASS_CONFIG_ARTIFICE_CLASS_URI))
                    .asLiteral().getString();
            return mainClassUri.equals(mainClass) && classUri.equals(artificeClassUri);
        }).nextOptional().ifPresent(
                foundIndividual -> {
                    foundIndividual.remove();
                    ontologyRepository.saveConfigurationModel(model, ontoId);
                }
        );
    }

    public Boolean isArtificeClass(String ontology, String classUri, String mainClassUri) {
        return getArtificeClassesConfig(ontology).contains(new ArtificeClassConfigDescriptor(classUri, mainClassUri));
    }

    private Property getOrCreateProperty(OntModel model, String uri) {
        Property property = model.getProperty(uri);
        return property == null ? model.createProperty(uri) : property;
    }

    private OntClass getOrCreateOntclass(OntModel model, String uri) {
        OntClass ontClass = model.getOntClass(uri);
        return ontClass == null ? model.createClass(uri) : ontClass;
    }

    private Individual getOrCreateOntoConfigIndividual(OntModel model) {
        var ontoConfigClass = getOrCreateOntclass(model, ONTO_CONFIG_CLASS);
        var configurations = ontoConfigClass.listInstances().mapWith(m -> m.as(Individual.class)).toList();

        return configurations.isEmpty() ? model.createIndividual(ONTO_CONFIG_INDIVIDUAL,ontoConfigClass) :
                configurations.get(0);
    }

    public record AppDescriptor(String appName){}
}
