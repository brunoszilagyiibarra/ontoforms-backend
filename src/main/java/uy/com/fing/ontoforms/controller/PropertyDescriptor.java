package uy.com.fing.ontoforms.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.jena.ontology.OntResource;

@Data
@AllArgsConstructor
public class PropertyDescriptor {

    private String propLabel;
    private String propUri;

    @JsonIgnore
    private OntResource domainClass;
    @JsonIgnore
    private OntResource rangeClass;

    private String domain;
    private String range;


    private String propType;
    private boolean functional;
    private boolean inverseFunctional;

}
