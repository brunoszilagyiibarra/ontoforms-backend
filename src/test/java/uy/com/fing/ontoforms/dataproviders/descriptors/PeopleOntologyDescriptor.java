package uy.com.fing.ontoforms.dataproviders.descriptors;

import org.apache.jena.riot.Lang;
import uy.com.fing.ontoforms.dataproviders.OntologyMetrics;
import uy.com.fing.ontoforms.dataproviders.OntologyTestDescriptor;

import java.util.HashMap;
import java.util.List;

public class PeopleOntologyDescriptor implements OntologyTestDescriptor {


    public static final String PEOPLE_TTL = "People.ttl";
    private static final String PREFIX = "http://www.semanticweb.org/mdebe/ontologies/example#";

    @Override
    public List<String> getDatatypeProperties() {
        return List.of(PREFIX + "has_Age",
                PREFIX + "has_Social_Relations");
    }

    @Override
    public List<String> getObjectProperties() {
        return List.of(
                PREFIX + "has_Gender",
                PREFIX + "has_Social_Relation_With",
                PREFIX + "has_Aunt",
                PREFIX + "has_Child",
                PREFIX + "has_Daughter",
                PREFIX + "has_Son",
                PREFIX + "has_Friend",
                PREFIX + "has_Parent",
                PREFIX + "has_Father",
                PREFIX + "has_Mother",
                PREFIX + "has_Sibling",
                PREFIX + "has_Brother",
                PREFIX + "has_Sister",
                PREFIX + "has_Spouse",
                PREFIX + "has_Husband",
                PREFIX + "has_Wife",
                PREFIX + "has_Uncle"
        );
    }

    @Override
    public List<String> getFirstLevelClasses() {
        return List.of(
                PREFIX + "Gender",
                PREFIX + "Person"
                );
    }

    @Override
    public List<String> getIndividualsByClassUri(String classUri) {

        HashMap<String, List<String>> mappings = new HashMap<>();

        mappings.put(PREFIX + "Gender",
                List.of(PREFIX + "Female",
                        PREFIX + "Male"));


        mappings.put(PREFIX + "Person",
                List.of(PREFIX + "Beth_Doe",
                        PREFIX + "Daisy_Buchanan",
                        PREFIX + "Jay_Gatsby",
                        PREFIX + "John_Doe",
                        PREFIX + "John_Smith",
                        PREFIX + "Mary_Doe",
                        PREFIX + "Miss_Havisham",
                        PREFIX + "Nick_Carraway",
                        PREFIX + "Sarah_Doe",
                        PREFIX + "Susan_Doe",
                        PREFIX + "Tom_Doe"));

        mappings.put(PREFIX + "Adult",
                List.of(
                        PREFIX + "Jay_Gatsby",
                        PREFIX + "John_Smith",
                        PREFIX + "Nick_Carraway",
                        PREFIX + "Beth_Doe",
                        PREFIX + "Daisy_Buchanan",
                        PREFIX + "John_Doe",
                        PREFIX + "Miss_Havisham"
                        ));

        mappings.put(PREFIX + "Man",
                List.of());

        mappings.put(PREFIX + "Parent",
                List.of(PREFIX + "John_Doe"));

        mappings.put(PREFIX + "Woman",
                List.of(PREFIX + "Beth_Doe",
                        PREFIX + "Daisy_Buchanan",
                        PREFIX + "Miss_Havisham"));

        mappings.put(PREFIX + "Child",
                List.of());

        mappings.put(PREFIX + "Boy",
                List.of());

        mappings.put(PREFIX + "Girl",
                List.of());

        mappings.put(PREFIX + "Hermit",
                List.of());

        mappings.put(PREFIX + "Social_Person",
                List.of( PREFIX + "Jay_Gatsby",  PREFIX + "John_Doe"));

        return mappings.get(classUri);
    }

    @Override
    public String getOntologyFilePath() {
        return "ontologies/People.ttl";
    }

    @Override
    public String getOntologyFileName() {
        return PEOPLE_TTL;
    }

    @Override
    public String toString() {
        return getOntologyFileName();
    }

    @Override
    public Lang getLang() {
        return Lang.TURTLE;
    }

    @Override
    public OntologyMetrics getOntologyStats() {
        return OntologyMetrics.builder()
                .classCount(11)
                .objectPropertyCount(17)
                .dataPropertyCount(2)
                .individualCount(13)
                .build();
    }
}