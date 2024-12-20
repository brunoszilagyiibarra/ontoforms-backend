package uy.com.fing.ontoforms.dataproviders.descriptors;

import org.apache.jena.riot.Lang;
import uy.com.fing.ontoforms.dataproviders.OntologyMetrics;
import uy.com.fing.ontoforms.dataproviders.OntologyTestDescriptor;

import java.util.HashMap;
import java.util.List;

public class MammographyScreeningOntologyDescriptor implements OntologyTestDescriptor {


    public static final String MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL = "Mammography_screening_recommendation.owl";
    private static final String PREFIX = "http://purl.org/ontology/mammography_screening_recommendation#";

    @Override
    public List<String> getDatatypeProperties() {
        return List.of(PREFIX + "age");
    }

    @Override
    public List<String> getObjectProperties() {
        return List.of(
                PREFIX + "doesNotHaveHistory",
                PREFIX + "hasHistory",
                PREFIX + "hasMammography",
                PREFIX + "hasPeriodicity",
                PREFIX + "hasRecommendation",
                PREFIX + "hasRecType",
                PREFIX + "isRecommended"
        );
    }


    @Override
    public List<String> getFirstLevelClasses() {
        return List.of(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C54625", //History
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16818", //Mammography
                PREFIX + "Periodicity", //Periodicity
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C25197", //Recommendation
                PREFIX + "Recommendation_Type", //Recommendation_Type
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C14284" //Woman
        );
    }

    @Override
    public List<String> getIndividualsByClassUri(String classUri) {
        HashMap<String, List<String>> mappings = new HashMap<>();

        mappings.put("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C18772",
                List.of(PREFIX + "Confirmed_or_suspected_genetic_mutation",
                        PREFIX + "Personal_history_of_breast_cancer",
                        PREFIX + "Radiotherapy_to_chest"));

        mappings.put("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16818",
                List.of(PREFIX + "Screening_Mammography"));

        mappings.put("http://purl.org/ontology/mammography_screening_recommendation#Periodicity",
                List.of(PREFIX + "Annual", PREFIX + "Annual_or_Biannual"));

        mappings.put("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C25197",
                List.of(PREFIX + "40_to_44_Age_range_recommendation",
                        PREFIX + "44_to_54_Age_range_recommendation",
                        PREFIX + "greater_than_55_Age_range_recommendation"));

        mappings.put("http://purl.org/ontology/mammography_screening_recommendation#Recommendation_Type",
                List.of(PREFIX + "Qualified_Recommendation",
                        PREFIX + "Strong_Recommendation"));

        mappings.put("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C14284",
                List.of(PREFIX + "Woman1", PREFIX + "Woman2", PREFIX + "Woman3", PREFIX + "Woman4", PREFIX + "Woman5",
                        PREFIX + "Woman6"));

        mappings.put("http://purl.org/ontology/mammography_screening_recommendation#With_intermediate_risk",
                List.of(PREFIX + "Woman6", PREFIX + "Woman2", PREFIX + "Woman1", PREFIX + "Woman5"));

        mappings.put("http://purl.org/ontology/mammography_screening_recommendation#greaterThan55",
                List.of(PREFIX + "Woman2"));

        mappings.put("http://purl.org/ontology/mammography_screening_recommendation#between_40_to_44",
                List.of(PREFIX + "Woman1"));

        mappings.put("http://purl.org/ontology/mammography_screening_recommendation#between_45_to_54",
                List.of(PREFIX + "Woman5"));

        return mappings.get(classUri);
    }

    @Override
    public String getOntologyFilePath() {
        return "ontologies/Mammography_screening_recommendation.owl";
    }

    @Override
    public String getOntologyFileName() {
        return MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL;
    }

    @Override
    public String toString() {
        return getOntologyFileName();
    }

    @Override
    public Lang getLang() {
        return Lang.RDFXML;
    }

    @Override
    public OntologyMetrics getOntologyStats() {
        return OntologyMetrics.builder()
                .classCount(11)
                .objectPropertyCount(7) //Protegé marca 8 porque considera topObjectProperty
                .dataPropertyCount(1)
                .individualCount(18)
                .build();
    }
}