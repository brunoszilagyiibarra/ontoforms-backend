package uy.com.fing.ontoforms.dataproviders;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import uy.com.fing.ontoforms.dataproviders.descriptors.*;

import static uy.com.fing.ontoforms.dataproviders.descriptors.BreastCancerRecommOntologyDescriptor.BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL;
import static uy.com.fing.ontoforms.dataproviders.descriptors.MammographyScreeningOntologyDescriptor.MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL;
import static uy.com.fing.ontoforms.dataproviders.descriptors.PeopleOntologyDescriptor.PEOPLE_TTL;
import static uy.com.fing.ontoforms.dataproviders.descriptors.PercepcionArteOntologyDescriptor.PERCEPCION_ARTE_SUBPERCEPCIONES_RDF;
import static uy.com.fing.ontoforms.dataproviders.descriptors.WineOntologyFixedDescriptor.WINE_RDF;

/**
 * Factory para recuperar las clases a partir de un string.
 */
public class StringToOntoDescriptorConverter implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {


        if (!(source instanceof String className)) {
            throw new IllegalArgumentException(
                    "The argument should be a string: " + source);
        }

        return switch (className) {
            case PEOPLE_TTL -> new PeopleOntologyDescriptor();
            case WINE_RDF -> new WineOntologyFixedDescriptor();
            case BREAST_CANCER_RECOMMENDATION_V_25_4_2024_OWL -> new BreastCancerRecommOntologyDescriptor();
            case MAMMOGRAPHY_SCREENING_RECOMMENDATION_OWL -> new MammographyScreeningOntologyDescriptor();
            case PERCEPCION_ARTE_SUBPERCEPCIONES_RDF -> new PercepcionArteOntologyDescriptor();
            default -> new IllegalArgumentException();
        };

    }
}
