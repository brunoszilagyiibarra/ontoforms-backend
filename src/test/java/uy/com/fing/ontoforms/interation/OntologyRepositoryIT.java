package uy.com.fing.ontoforms.interation;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uy.com.fing.ontoforms.dataproviders.OntologyTestDescriptor;
import uy.com.fing.ontoforms.dataproviders.StringToOntoDescriptorConverter;
import uy.com.fing.ontoforms.service.ontology.persistence.OntologyRepository;
import uy.com.fing.ontoforms.service.ontology.persistence.client.FusekiTripleStoreClient;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static uy.com.fing.ontoforms.dataproviders.descriptors.BreastCancerRecommOntologyDescriptor.BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL;
import static uy.com.fing.ontoforms.dataproviders.descriptors.MammographyScreeningOntologyDescriptor.MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL;
import static uy.com.fing.ontoforms.dataproviders.descriptors.PeopleOntologyDescriptor.PEOPLE_TTL;
import static uy.com.fing.ontoforms.dataproviders.descriptors.PercepcionArteOntologyDescriptor.PERCEPCION_ARTE_SUBPERCEPCIONES_RDF;
import static uy.com.fing.ontoforms.dataproviders.descriptors.WineOntologyFixedDescriptor.WINE_RDF;

@SpringBootTest
@Slf4j
public class OntologyRepositoryIT {

    @Autowired
    private OntologyRepository sut;

    @MockBean
    private FusekiTripleStoreClient tripleStoreClient;


    /**
     * Recupera todas las datatype properties y se asegura que el sistema cargue las mismas.
     * @param onto
     */
    @ParameterizedTest
    @CsvSource({PEOPLE_TTL, WINE_RDF, BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL,
            MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL, PERCEPCION_ARTE_SUBPERCEPCIONES_RDF})
    void whenGetDatatypePropertiesThenRetrieveAllExpected(@ConvertWith(StringToOntoDescriptorConverter.class) OntologyTestDescriptor onto) {
        mockTripleStoreWithLocalImplementation(onto);

        var datatypeProperties = sut.getDatatypePropertiesByOntoId(onto.getOntologyFileName());

        assertEquals(onto.getDatatypeProperties().stream().sorted().toList(),
                datatypeProperties.stream().map(Resource::getURI).sorted().toList());
        assertEquals(onto.getOntologyStats().getDataPropertyCount(), datatypeProperties.size());
    }

    /**
     * Recupera todas las object properties y se asegura que el sistema cargue las mismas.
     * @param onto
     */
    @ParameterizedTest
    @CsvSource({
            PEOPLE_TTL,
            WINE_RDF,
            BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL,
            MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
            PERCEPCION_ARTE_SUBPERCEPCIONES_RDF
    })
    void whenGetObjectPropertiesThenRetrieveAllExpected(@ConvertWith(StringToOntoDescriptorConverter.class) OntologyTestDescriptor onto) {
        mockTripleStoreWithLocalImplementation(onto);

        var objectProperties = sut.getObjectPropertiesByOntoId(onto.getOntologyFileName());

        assertEquals(onto.getObjectProperties().stream().sorted().toList(),
                objectProperties.stream().map(Resource::getURI).sorted().toList());
        assertEquals(onto.getOntologyStats().getObjectPropertyCount(), objectProperties.size());
    }

    /**
     * Recupera todas las clases de primer nivel de la ontología.
     * @param onto
     */
    @ParameterizedTest
    @CsvSource({
            PEOPLE_TTL,
            WINE_RDF,
            BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL,
            MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
            PERCEPCION_ARTE_SUBPERCEPCIONES_RDF
    })
    void whenGetFirstLevelClassesThenRetrieveAllExpected(@ConvertWith(StringToOntoDescriptorConverter.class) OntologyTestDescriptor onto) {
        mockTripleStoreWithLocalImplementation(onto);

        var classes = sut.getFirstLevelClassesByOntoId(onto.getOntologyFileName());

        assertEquals(onto.getFirstLevelClasses().stream().sorted().toList(),
                classes.stream().map(Resource::getURI).sorted().toList());
    }

    /**
     * Recupera todos los individuos según cada clase de la ontología.
     * @param onto
     */
    @ParameterizedTest
    @MethodSource("getOntologyPlusClasses")
    void whenGetIndividualsThenRetrieveAllExpected(@ConvertWith(StringToOntoDescriptorConverter.class)
                                                   OntologyTestDescriptor onto, String classUri) {
        mockTripleStoreWithLocalImplementation(onto);

        var individuals = sut.getIndividualsByOntoIdAndClassUri(onto.getOntologyFileName(), classUri);

        assertEquals(onto.getIndividualsByClassUri(classUri).stream().sorted().toList(),
                individuals.stream().map(Resource::getURI).sorted().toList());
    }

    @ParameterizedTest
    @CsvSource({
            PEOPLE_TTL,
            WINE_RDF,
            BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL,
            MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
            PERCEPCION_ARTE_SUBPERCEPCIONES_RDF
    })
    void whenGetOntoIndividualCountThenMatchExpected(@ConvertWith(StringToOntoDescriptorConverter.class)
                                             OntologyTestDescriptor onto) {
        mockTripleStoreWithLocalImplementation(onto);

        var individualsCount = sut.getIndividualsByOntoId(onto.getOntologyFileName()).size();

        assertEquals(onto.getOntologyStats().getIndividualCount(), individualsCount);
    }

    @ParameterizedTest
    @CsvSource({
            PEOPLE_TTL,
            WINE_RDF,
            BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL,
            MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
            PERCEPCION_ARTE_SUBPERCEPCIONES_RDF
    })
    void whenGetOntoClassCountThenMatchExpected(@ConvertWith(StringToOntoDescriptorConverter.class)
                                                    OntologyTestDescriptor onto) {
        mockTripleStoreWithLocalImplementation(onto);

        var count = sut.getAllNamedClassesByOntoId(onto.getOntologyFileName()).size();

        assertEquals(onto.getOntologyStats().getClassCount(), count);
    }


    private static Stream<Arguments> getOntologyPlusClasses() {

        return Stream.of(
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Gender"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Person"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Adult"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Man"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Parent"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Woman"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Child"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Boy"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Girl"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Hermit"),
                Arguments.of(PEOPLE_TTL, "http://www.semanticweb.org/mdebe/ontologies/example#Social_Person"),

                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C18772"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16818"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://purl.org/ontology/mammography_screening_recommendation#Periodicity"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C25197"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://purl.org/ontology/mammography_screening_recommendation#Recommendation_Type"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C14284"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://purl.org/ontology/mammography_screening_recommendation#With_intermediate_risk"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://purl.org/ontology/mammography_screening_recommendation#greaterThan55"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://purl.org/ontology/mammography_screening_recommendation#between_40_to_44"),
                Arguments.of(MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL,
                        "http://purl.org/ontology/mammography_screening_recommendation#between_45_to_54")
        );
    }


    private void mockTripleStoreWithLocalImplementation(OntologyTestDescriptor onto) {
        Model model = RDFDataMgr.loadModel(onto.getOntologyFilePath(), onto.getLang());
        when(tripleStoreClient.getOntologyByName(onto.getOntologyFileName())).thenReturn(model);
    }

}
