package uy.com.fing.ontoforms.dataproviders;

import org.apache.jena.riot.Lang;

import java.util.List;

public interface OntologyTestDescriptor {

    List<String> getDatatypeProperties();
    List<String> getObjectProperties();
    List<String> getFirstLevelClasses();
    List<String> getIndividualsByClassUri(String classUri);

    String getOntologyFilePath();
    String getOntologyFileName();
    Lang getLang();

    OntologyMetrics getOntologyStats();

}
