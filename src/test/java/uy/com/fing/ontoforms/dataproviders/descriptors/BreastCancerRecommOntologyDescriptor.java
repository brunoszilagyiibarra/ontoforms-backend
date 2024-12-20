package uy.com.fing.ontoforms.dataproviders.descriptors;

import org.apache.jena.riot.Lang;
import uy.com.fing.ontoforms.dataproviders.OntologyMetrics;
import uy.com.fing.ontoforms.dataproviders.OntologyTestDescriptor;

import java.util.List;

public class BreastCancerRecommOntologyDescriptor implements OntologyTestDescriptor {


    public static final String BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL = "Breast_cancer_recommendationV25_4_2024.owl";

    @Override
    public List<String> getDatatypeProperties() {
        return List.of("http://purl.org/ontology/breast_cancer_recommendation#age",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRiskCondition",
                "http://purl.org/ontology/breast_cancer_recommendation#numberOfBiopsies",
                "http://purl.org/ontology/breast_cancer_recommendation#numberOfChilds",
                "http://purl.org/ontology/breast_cancer_recommendation#numberRecentYearsWithoutHormonalTherapy",
                "http://purl.org/ontology/breast_cancer_recommendation#question",
                "http://purl.org/ontology/breast_cancer_recommendation#RecentAnticonceptive",
                "http://purl.org/ontology/breast_cancer_recommendation#yearsNumberAnticonceptive");
    }

    @Override
    public List<String> getObjectProperties() {
        return List.of(
                "http://purl.org/ontology/breast_cancer_recommendation#basedOn",
                "http://purl.org/ontology/breast_cancer_recommendation#forInterval",
                "http://purl.org/ontology/breast_cancer_recommendation#forRiskLevel",
                "http://purl.org/ontology/breast_cancer_recommendation#gives",
                "http://purl.org/ontology/breast_cancer_recommendation#hasAge",
                "http://purl.org/ontology/breast_cancer_recommendation#hasAgeHigh",
                "http://purl.org/ontology/breast_cancer_recommendation#hasAgeMedium",
                "http://purl.org/ontology/breast_cancer_recommendation#hasFirstChildBirthAge",
                "http://purl.org/ontology/breast_cancer_recommendation#hasHistory",
                "http://purl.org/ontology/breast_cancer_recommendation#hasImaging",
                "http://purl.org/ontology/breast_cancer_recommendation#hasLevel",
                "http://purl.org/ontology/breast_cancer_recommendation#hasMenarcheAge",
                "http://purl.org/ontology/breast_cancer_recommendation#hasModelQuestion",
                "http://purl.org/ontology/breast_cancer_recommendation#hasPeriodicity",
                "http://purl.org/ontology/breast_cancer_recommendation#hasQuestion",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRace",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRange",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRecommendationHigh",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRecommendationMedium",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRecStrength",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRisk",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRiskFactor",
                "http://purl.org/ontology/breast_cancer_recommendation#hasRiskFactorValue",
                "http://purl.org/ontology/breast_cancer_recommendation#intervalOfHigh",
                "http://purl.org/ontology/breast_cancer_recommendation#intervalOfMedium",
                "http://purl.org/ontology/breast_cancer_recommendation#predicts"
        );
    }

    @Override
    public List<String> getFirstLevelClasses() {
        return List.of(
                "http://purl.org/ontology/breast_cancer_recommendation#Age",
                "http://purl.org/ontology/breast_cancer_recommendation#Guideline",
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C54625", //HISTORY
                "http://purl.org/ontology/breast_cancer_recommendation#Imaging",
                "http://purl.org/ontology/breast_cancer_recommendation#Model",
                "http://purl.org/ontology/breast_cancer_recommendation#Periodicity",
                "http://purl.org/ontology/breast_cancer_recommendation#Race",
                "http://purl.org/ontology/breast_cancer_recommendation#Rec_interval",
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C25197", //RECOMMENDATION
                "http://purl.org/ontology/breast_cancer_recommendation#Recommendation_Strength",
                "http://purl.org/ontology/breast_cancer_recommendation#Risk",
                "http://purl.org/ontology/breast_cancer_recommendation#Risk_factor",
                "http://purl.org/ontology/breast_cancer_recommendation#Risk_factor_question",
                "http://purl.org/ontology/breast_cancer_recommendation#Risk_level",
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C14284" //WOMAN
        );

    }

    @Override
    public List<String> getIndividualsByClassUri(String classUri) {
        return null;
    }

    @Override
    public OntologyMetrics getOntologyStats() {
        return OntologyMetrics.builder()
                .classCount(29) // // Protegé marca 30 porque considera owl:Thing
                .objectPropertyCount(26) //Protegé marca 27 porque considera topObjectProperty
                .dataPropertyCount(8) //Protegé marca 9 porque considera topDataProperty
                .individualCount(86)
                .build();
    }

    @Override
    public String getOntologyFilePath() {
        return "ontologies/Breast_cancer_recommendationV25_4_2024.owl";
    }

    @Override
    public String getOntologyFileName() {
        return BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL;
    }

    @Override
    public String toString() {
        return getOntologyFileName();
    }

    @Override
    public Lang getLang() {
        return Lang.RDFXML;
    }
}