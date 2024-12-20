package uy.com.fing.ontoforms.service.ontology;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Form {

    private final String classUri;
    private final String sectionName;
    private final List<FormField> fields;
    private final List<Form> subForms;

    public Form(String classUri, String sectionName) {
        this(classUri, sectionName, new ArrayList<>());
    }

    public Form(String classUri, String sectionName, List<FormField> fields) {
        this.classUri = classUri;
        this.sectionName = sectionName;
        this.fields = fields;
        this.subForms = new ArrayList<>();
    }

    public void addSubSection(Form subSection) {
        subForms.add(subSection);
    }

    public record FieldOption(String label, String uri) {}

    @Data
    @NoArgsConstructor
    @SuperBuilder
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "classType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DatatypeField.class, name = "datatype-field"),
            @JsonSubTypes.Type(value = ObjectField.class, name = "object-field"),
    })
    public static abstract class FormField {
        private String label;
        private String uri;
        private Integer order;

        public abstract String getType();
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonTypeName("datatype-field")
    public static class DatatypeField extends FormField {

        private String datatype;

        @Override
        public String getType() {
            return datatype;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonTypeName("object-field")
    public static class ObjectField extends FormField {

        private List<FieldOption> options;
        private boolean singleOption;

        @Override
        public String getType() {
            return singleOption ? "single-option" : "multi-option";
        }
    }

}
