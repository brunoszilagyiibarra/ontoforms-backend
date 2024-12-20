package uy.com.fing.ontoforms.dataproviders.descriptors;

import org.apache.jena.riot.Lang;
import uy.com.fing.ontoforms.dataproviders.OntologyMetrics;
import uy.com.fing.ontoforms.dataproviders.OntologyTestDescriptor;

import java.util.List;

public class WineOntologyFixedDescriptor implements OntologyTestDescriptor {


    public static final String WINE_RDF = "wine.rdf";

    @Override
    public List<String> getDatatypeProperties() {
        return List.of("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#yearValue");
    }

    @Override
    public List<String> getObjectProperties() {
        return List.of("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#adjacentRegion",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#course",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#hasDrink",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#hasFood",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasMaker",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasVintageYear",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasWineDescriptor",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasBody",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasColor",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasFlavor",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasSugar",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#locatedIn",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#madeFromFruit",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#madeFromGrape",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#madeIntoWine",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#producesWine"
        );
    }


    @Override
    public List<String> getFirstLevelClasses() {
        return List.of(
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#ConsumableThing",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#Fruit",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#NonConsumableThing",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#Region",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#Vintage",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#VintageYear",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/food#Wine",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#WineDescriptor",
                "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#Winery"
        );
    }

    @Override
    public List<String> getIndividualsByClassUri(String classUri) {
        return null;
    }

    @Override
    public String getOntologyFilePath() {
        return "ontologies/wine.rdf";
    }

    @Override
    public String getOntologyFileName() {
        return WINE_RDF;
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
                .classCount(137) // Protegé marca 138 porque considera owl:Thing
                .objectPropertyCount(16)
                .dataPropertyCount(1)
                .individualCount(206)
                .build();
    }

}