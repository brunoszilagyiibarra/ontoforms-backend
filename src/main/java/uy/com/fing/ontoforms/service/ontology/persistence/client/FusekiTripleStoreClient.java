package uy.com.fing.ontoforms.service.ontology.persistence.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class FusekiTripleStoreClient {


    private static final String NAMED_GRAPH_PREFIX = "http://www.fing.edu.uy/ontologies/";
    private static final String ONTOLOGIES_TBOX_DATASET = "/ontologies";
    private static final String ONTOLOGIES_ABOX_DATASET = "/individuals";
    private static final String ONTOLOGIES_CONFIG_DATASET = "/configs";

    @Value("${triplestore.client.url}")
    private String host;

    @Value("${triplestore.auth.user}")
    private String username;

    @Value("${triplestore.auth.pass}")
    private String pass;

    private RestClient restClient;

    public record OntologyName( String id, String name) {}

    @PostConstruct
    private void initializeDatasets(){

        restClient = RestClient.builder().requestInterceptor(new BasicAuthenticationInterceptor(username, pass)).build();

        Map<String, Object> datasets = restClient.get().uri(URI.create(host + "/$/datasets"))
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> o = (List<Map<String, Object>>) datasets.get("datasets");
        List<String> dsNames = o.stream().map(m -> (String) m.get("ds.name")).toList();

        generateDataSetIfNotExists(ONTOLOGIES_TBOX_DATASET, dsNames);
        generateDataSetIfNotExists(ONTOLOGIES_ABOX_DATASET, dsNames);
        generateDataSetIfNotExists(ONTOLOGIES_CONFIG_DATASET, dsNames);
    }

    private void generateDataSetIfNotExists(String dsName, List<String> dsAvailable) {
        if(dsAvailable.contains(dsName)) {
            log.info("Dataset {} ya está disponible", dsName);
        } else {
            MultiValueMap<String, String> bodyPair = new LinkedMultiValueMap();
            bodyPair.put("dbType", List.of("tdb2"));
            bodyPair.put("dbName", List.of(dsName));

            ResponseEntity<Void> bodilessEntity = restClient.post().uri(URI.create(host + "/$/datasets"))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(bodyPair)
                    .retrieve()
                    .toBodilessEntity();

            if(bodilessEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Se generó con éxito el dataset {}", dsName);
            } else {
                log.info("hubo un fallo {} al intentar crear el dataset {}", bodilessEntity.getStatusCode(), dsName);
            }
        }
    }


    /**
     * Se obtienen todos los nombres de las ontologías guardadas en el triplestore, dataset ontologies.
     * @return
     */
    public List<OntologyName> getAllOntologyNames() {
        try (RDFConnection conn = RDFConnectionFuseki.connect(host + ONTOLOGIES_TBOX_DATASET)) {
            Iterable<String> iterableNames = () -> conn.fetchDataset().listNames();

            return StreamSupport.stream(iterableNames.spliterator(), false)
                    .map(graphName -> new OntologyName(graphName, graphName.replace(NAMED_GRAPH_PREFIX, "")))
                    .toList();
        }
    }

    public List<String> allGraphNamesConfig() {
        try (RDFConnection conn = RDFConnectionFuseki.connect(host + ONTOLOGIES_CONFIG_DATASET)) {
            Iterable<String> iterableNames = () -> conn.fetchDataset().listNames();

            return StreamSupport.stream(iterableNames.spliterator(), false)
                    .toList();
        }
    }

    /**
     * Retorna un modelo representando a la ontología pasada por parámetro. Se recupera del triple-store.
     * @param ontoId puede ser la uri completa o solamente la terminación
     * @return
     */
    public Model getOntologyByName(String ontoId) {
        return getModelFromDataset(ontoId, ONTOLOGIES_TBOX_DATASET);
    }

    public Model getConfigurationsModelFor(String ontoId) {
        return getModelFromDataset(ontoId, ONTOLOGIES_CONFIG_DATASET);
    }

    public Model getIndividualsModelFor(String ontoId) {
        return getModelFromDataset(ontoId, ONTOLOGIES_ABOX_DATASET);
    }

    /**
     * Guarda el modelo pasado por parámetro bajo el named-graph de ontologyName.
     * @param model modelo a persistir
     * @param ontologyName nombre bajo el cuál se persistirá.
     * @return
     */
    public String saveOntology(Model model, String ontologyName) {
        return saveModelIntoNamedGraph(model, ontologyName, ONTOLOGIES_TBOX_DATASET);
    }

    public String saveConfiguration(Model model, String ontologyName) {
        return saveModelIntoNamedGraph(model, ontologyName, ONTOLOGIES_CONFIG_DATASET);
    }

    public String saveModelWithIndividuals(Model model, String ontologyName) {
        return saveModelIntoNamedGraph(model, ontologyName, ONTOLOGIES_ABOX_DATASET);
    }

    private String saveModelIntoNamedGraph(Model model, String ontologyName, String dataset) {
        var uriNamedGraph = NAMED_GRAPH_PREFIX + ontologyName;

        try (RDFConnection conn = RDFConnectionFuseki.connect(host + dataset)) {
            conn.put(uriNamedGraph, model);
        }

        return uriNamedGraph;
    }

    private Model getModelFromDataset(String ontoId, String dataset) {
        String graphName = ontoId.startsWith(NAMED_GRAPH_PREFIX) ? ontoId : NAMED_GRAPH_PREFIX + ontoId;

        try (RDFConnection conn = RDFConnectionFuseki.connect(host + dataset)) {
            return conn.fetch(graphName);
        }
    }
}
