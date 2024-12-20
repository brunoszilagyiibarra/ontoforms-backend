package uy.com.fing.ontoforms.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uy.com.fing.ontoforms.configuration.ConfigurationService;
import uy.com.fing.ontoforms.exception.BadRequestException;
import uy.com.fing.ontoforms.service.ontology.*;
import uy.com.fing.ontoforms.service.ontology.persistence.OntologyRepository;
import uy.com.fing.ontoforms.service.ontology.persistence.client.FusekiTripleStoreClient;

import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("ontoforms-api/v1")
public class OntoformsController {

    private final OntologyService ontologyService;
    private final OntologyRepository ontologyRepository;
    private final FormService formService;
    private final IndividualsService individualsService;
    private final ConfigurationService configurationService;


    public record OntologyCreateResponse(String id) {};

    @PostMapping("/ontologies")
    public OntologyCreateResponse createOntology(@RequestParam("file") MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();

        if (fileName == null) {
            throw new BadRequestException("El nombre del archivo de la ontología es requerido");
        }

        if (multipartFile.getSize() == 0) {
            throw new BadRequestException("El archivo no debe ser vacío");
        }

        byte[] fileContent;
        try {
            fileContent = multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo obtener el contenido de la ontologìa", e);
        }

        return new OntologyCreateResponse(ontologyService.createOntology(fileName, fileContent));
    }


    @GetMapping( "/ontologies")
    public List<FusekiTripleStoreClient.OntologyName> listAllOntologies() {
        return ontologyRepository.listAllOntologiesNames();
    }

    @GetMapping( "/ontologies/{ontoId}")
    public String getOntologyDetails(@PathVariable String ontoId) {
        return ontologyRepository.getOntologyRDFJSON(ontoId);
    }

    @GetMapping( "/ontologies/{ontoId}/forms")
    public Form getOntologyClassInputForm(@PathVariable String ontoId, @RequestParam("classUri") String classUri) {
        log.info("Se consulta el formulario desde WEB para ontología {} y clase {}", ontoId, classUri);

        Form form = formService.getForm(ontoId, classUri, false);

        log.info("Se completa consulta el formulario desde WEB para ontología {} y clase {} con resultado form: {}",
                ontoId, classUri, form);
        return form;
    }

    @GetMapping( "/ontologies/{ontoId}/configurations/calculated-properties")
    public List<ConfigurationService.CalculatedPropertyConfigDescriptor> getOntologyCalculatedPropertiesConfig(@PathVariable String ontoId) {
        log.info("Recuperando configuración calculated-props-config de la ontología {}", ontoId);
        List<ConfigurationService.CalculatedPropertyConfigDescriptor> calculatedPropertyConfig = configurationService.getCalculatedPropertyConfig(ontoId);
        log.info("Se recupera configuración calculated-props-config de la ontología {}, valores {}", ontoId, calculatedPropertyConfig);
        return calculatedPropertyConfig;
    }

    @PostMapping( "/ontologies/{ontoId}/configurations/calculated-properties")
    public void addOntologyCalculatedPropertyConfig(@PathVariable String ontoId, @RequestBody ConfigurationService.CalculatedPropertyConfigDescriptor calculatedPropertyConfigDescriptor) {
        log.info("Agregando configuración calculated-prop-config de la ontología {} con parámetros {}", ontoId, calculatedPropertyConfigDescriptor);
        configurationService.addCalculatedProperty(ontoId, calculatedPropertyConfigDescriptor.propUri(), calculatedPropertyConfigDescriptor.mainClass());
        log.info("Configuración agregada calculated-prop-config con éxito de la ontología {} con parámetros {}", ontoId, calculatedPropertyConfigDescriptor);
    }

    @DeleteMapping ( "/ontologies/{ontoId}/configurations/calculated-properties")
    public void deleteOntologyCalculatedPropertyConfig(@PathVariable String ontoId, @RequestBody ConfigurationService.CalculatedPropertyConfigDescriptor calculatedPropertyConfigDescriptor) {
        log.info("Eliminando configuración calculated-prop-config de la ontología {} con parámetros {}", ontoId, calculatedPropertyConfigDescriptor);
        configurationService.deleteCalculatedPropConfig(ontoId, calculatedPropertyConfigDescriptor.propUri(), calculatedPropertyConfigDescriptor.mainClass());
        log.info("Configuración eliminada calculated-prop-config con éxito de la ontología {} con parámetros {}", ontoId, calculatedPropertyConfigDescriptor);
    }

    @GetMapping( "/ontologies/{ontoId}/configurations/translations")
    public List<ConfigurationService.TranslationConfigDescriptor> getOntologyTranslationsConfig(@PathVariable String ontoId) {
        log.info("Recuperando configuración translations-config de la ontología {}", ontoId);
        List<ConfigurationService.TranslationConfigDescriptor> translationsConfig = configurationService.getTranslationsConfig(ontoId);
        log.info("Se recupera configuración translations-config de la ontología {}, valores {}", ontoId, translationsConfig);
        return translationsConfig;
    }

    @PostMapping( "/ontologies/{ontoId}/configurations/translations")
    public void addOntologyTranslationsConfig(@PathVariable String ontoId, @RequestBody ConfigurationService.TranslationConfigDescriptor translation) {
        log.info("Agregando configuración translations-config de la ontología {} con parámetros {}", ontoId, translation);
        configurationService.addTranslationForResourceLabel(ontoId, translation.propertyUri(), translation.propertyLabelTranslation());
        log.info("Configuración agregada translations-config con éxito de la ontología {} con parámetros {}", ontoId, translation);
    }

    @DeleteMapping ( "/ontologies/{ontoId}/configurations/translations")
    public void deleteOntologyTranslationsConfig(@PathVariable String ontoId, @RequestBody ConfigurationService.TranslationConfigDescriptor translation) {
        log.info("Eliminando configuración translations-config de la ontología {} con parámetros {}", ontoId, translation);
        configurationService.deleteTranslationFor(ontoId, translation.propertyUri());
        log.info("Configuración eliminada translations-config con éxito de la ontología {} con parámetros {}", ontoId, translation);
    }


    @GetMapping( "/ontologies/{ontoId}/configurations/artifice-classes")
    public List<ConfigurationService.ArtificeClassConfigDescriptor> getOntologyArtificeClassesConfig(@PathVariable String ontoId) {
        log.info("Recuperando configuración artifice-classes de la ontología {}", ontoId);
        List<ConfigurationService.ArtificeClassConfigDescriptor> artificeClassesConfig = configurationService.getArtificeClassesConfig(ontoId);
        log.info("Se recupera configuración artifice-classes de la ontología {}, valores {}", ontoId, artificeClassesConfig);
        return artificeClassesConfig;
    }

    @PostMapping( "/ontologies/{ontoId}/configurations/artifice-classes")
    public void addOntologyArtificeClassesConfig(@PathVariable String ontoId, @RequestBody ConfigurationService.ArtificeClassConfigDescriptor artificeClassConfigDescriptor) {
        log.info("Agregando configuración artifice-classes de la ontología {} con parámetros {}", ontoId, artificeClassConfigDescriptor);
        configurationService.addArtificeClass(ontoId, artificeClassConfigDescriptor.classUri(), artificeClassConfigDescriptor.mainClassUri());
        log.info("Configuración agregada artifice-classes con éxito de la ontología {} con parámetros {}", ontoId, artificeClassConfigDescriptor);
    }

    @DeleteMapping ( "/ontologies/{ontoId}/configurations/artifice-classes")
    public void deleteOntologyArtificeClassesConfig(@PathVariable String ontoId, @RequestBody ConfigurationService.ArtificeClassConfigDescriptor artificeClassConfigDescriptor) {
        log.info("Eliminando configuración artifice-classes de la ontología {} con parámetros {}", ontoId, artificeClassConfigDescriptor);
        configurationService.deleteArtificeClassConfig(ontoId, artificeClassConfigDescriptor.classUri(), artificeClassConfigDescriptor.mainClassUri());
        log.info("Configuración eliminada artifice-classes con éxito de la ontología {} con parámetros {}", ontoId, artificeClassConfigDescriptor);
    }

    @GetMapping( "/ontologies/{ontoId}/configurations/app-mapping")
    public List<ConfigurationService.AppDescriptor> getOntoAppMappingConfig(@PathVariable String ontoId) {
        log.info("Recuperando configuración app-mapping de la ontología {}", ontoId);
        List<ConfigurationService.AppDescriptor> appConfigs = configurationService.getAppOntologyRelationConfig(ontoId);
        log.info("Se recupera configuración app-mapping de la ontología {}, valores {}", ontoId, appConfigs);
        return appConfigs;
    }

    @GetMapping( "/apps/{appName}/configurations/app-mapping")
    public List<String> getAppMappingOntoConfig(@PathVariable String appName) {
        log.info("Recuperando configuración app-mapping de la app {}", appName);
        List<String> appConfigs = configurationService.getAppOntologyRelationConfigFromApp(appName);
        log.info("Se recupera configuración app-mapping de la app {}, valores {}", appName, appConfigs);
        return appConfigs;
    }

    @PostMapping( "/ontologies/{ontoId}/configurations/app-mapping")
    public void addOntologyArtificeClassesConfig(@PathVariable String ontoId, @RequestBody AppMappingRequest appMappingRequest) {
        log.info("Agregando configuración app-mapping de la ontología {} con parámetros {}", ontoId, appMappingRequest);
        configurationService.addAppOntologyRelation(ontoId, appMappingRequest.appName);
        log.info("Configuración agregada app-mapping con éxito de la ontología {} con parámetros {}", ontoId, appMappingRequest);
    }

    @DeleteMapping ( "/ontologies/{ontoId}/configurations/app-mapping")
    public void deleteOntologyArtificeClassesConfig(@PathVariable String ontoId, @RequestBody AppMappingRequest appMappingRequest) {
        log.info("Eliminando configuración app-mapping de la ontología {} con parámetros {}", ontoId, appMappingRequest);
        configurationService.deleteAppOntologyRelationConfig(ontoId, appMappingRequest.appName());
        log.info("Configuración eliminada app-mapping con éxito de la ontología {} con parámetros {}", ontoId, appMappingRequest);
    }

    public record AppMappingRequest(String appName) {}


    @GetMapping( "/ontologies/{ontoId}/classes")
    public OntoTree getOntologyTopLevelClasses(@PathVariable String ontoId) {
        return ontologyService.getOntologyTopLevelClasses(ontoId);
    }

    @GetMapping( "/ontologies/{ontoId}/data_props")
    public List<PropertyDescriptor> getOntologyDataProperties(@PathVariable String ontoId) {
        return ontologyService.getDataProperties(ontoId);
    }

    @GetMapping( "/ontologies/{ontoId}/object_props")
    public List<PropertyDescriptor> getOntologyObjectProperties(@PathVariable String ontoId) {
        return ontologyService.getObjectProperties(ontoId);
    }

    @GetMapping( "/ontologies/{ontoId}/properties")
    public List<PropertyDescriptor> searchOntologyProperties(@PathVariable String ontoId,
                                                             @RequestParam("domainClassUri") String classUri) {
        return ontologyService.getObjectAndDataPropsForClass(ontoId, classUri);
    }

    @GetMapping("/ontologies/{ontoId}/individuals")
    public List<IndividualsService.IndividualDescriptor> getOntologyIndividuals(@PathVariable String ontoId,
                                                             @RequestParam String classUri) {
        log.info("Se comienza recuperar los individuos de la ontología {} con parámetro {}", ontoId, classUri);

        List<IndividualsService.IndividualDescriptor> individuals;
        if(classUri == null || "null".equals(classUri)) {
            individuals = individualsService.getIndividuals(ontoId);
        } else {
            individuals = individualsService.getOntologyClassIndividuals(ontoId, classUri);
        }

        log.info("Se retornan {} individuos de la ontología {} con parámetro {}", individuals.size(),
                ontoId, classUri);
        return individuals;
    }

}
