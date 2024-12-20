package uy.com.fing.ontoforms.service.ontology.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Component;

/**
 * Dado un RDFNode, que es o un literal o un recurso, se realiza una estrategia para
 * visitarlo y obtener un nombre mostrable al usuario.
 *
 * TODO: Acá se podría injectar una correspondencia de nombres/lenguajes si se necesitara.
 */
@Component
@Slf4j
public class RDFNodePrettyLabelProcessor extends RDFNodeProcessor<String> {

    private static final String AND_TOKEN = " AND ";
    private static final String OR_TOKEN = " OR ";
    private static final String SOME_TOKEN = " some ";
    private static final String VALUE_TOKEN = " value ";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";
    private static final String INVERSE_TOKEN = " inverse ";

    @Override
    protected String visitBlank(Resource r, AnonId id) {

        final var sb = new StringBuilder();

        if(r.canAs(UnionClass.class)) {
            var unionClass = r.asResource().as(UnionClass.class);

            var operands = unionClass.getOperands();

            //Recorro y proceso todos los operandos de la intersección.
            while (!operands.isEmpty()) {
                RDFNode operand = operands.getHead();
                operands = operands.getTail();

                sb.append(apply(operand));

                if(!operands.isEmpty()) {
                    sb.append(OR_TOKEN);
                }
            }

        } else if(r.canAs(IntersectionClass.class)) {
            var intersectionClass = r.asResource().as(IntersectionClass.class);

            var operands = intersectionClass.getOperands();

            //Recorro y proceso todos los operandos de la intersección.
            while (!operands.isEmpty()) {
                RDFNode operand = operands.getHead();
                operands = operands.getTail();

                sb.append(apply(operand));

                if(!operands.isEmpty()) {
                    sb.append(AND_TOKEN);
                }
            }

        } else if(r.canAs(Restriction.class)) {
            Restriction restriction = r.as(Restriction.class);
            processRestrictionNode(restriction, sb);

        } else if(r.canAs(OntProperty.class)) {
            OntProperty ontProperty = r.as(OntProperty.class);

            if(!ontProperty.listInverseOf().toList().isEmpty()) {
                sb.append(INVERSE_TOKEN);
                sb.append(LEFT_BRACKET);
                ontProperty.listInverseOf().toList().forEach(inv -> sb.append(apply(inv)));
                sb.append(RIGHT_BRACKET);

                return sb.toString();
            }
        } else {
            //TODO caso no contemplado.
            sb.append(r.getLocalName() == null ? "" : r.getLocalName());
            log.error("CASO NO CONTEMPLADO, {}", r);
        }


        log.info("Label to Return {}", sb);
        return sb.toString();
    }

    private void processRestrictionNode(Restriction restriction, StringBuilder sb) {
        if (restriction.isAllValuesFromRestriction()) {
            log.error("NO CONTEMPLADO:  AllValuesFrom: " + restriction.asAllValuesFromRestriction().getAllValuesFrom());
            //TODO hacer

        } else if (restriction.isSomeValuesFromRestriction()) {
            var someValuesFromRestriction = restriction.asSomeValuesFromRestriction();

            sb.append(LEFT_BRACKET);
            ((OntModel) restriction.getModel()).setStrictMode(false);
            OntProperty onProperty = restriction.getOnProperty();
            ((OntModel) restriction.getModel()).setStrictMode(true);
            sb.append(apply(onProperty));
            sb.append(SOME_TOKEN);
            sb.append(apply(someValuesFromRestriction.getSomeValuesFrom()));
            sb.append(RIGHT_BRACKET);

            log.info("SomeValuesFrom: {}", sb);
        } else if (restriction.isHasValueRestriction()) {
            HasValueRestriction hasValueRestriction = restriction.asHasValueRestriction();

            sb.append(LEFT_BRACKET);
            sb.append(apply(hasValueRestriction.getOnProperty()));
            sb.append(VALUE_TOKEN);
            sb.append(apply(hasValueRestriction.getHasValue()));
            sb.append(RIGHT_BRACKET);

            log.info("HasValue: {}", sb);

        } else if (restriction.isCardinalityRestriction()) {
            System.out.println("  Cardinality: " + restriction.asCardinalityRestriction().getCardinality());
        } else {
            System.out.println("  Other type of restriction.");
        }
    }

    @Override
    protected String visitURI(Resource r, String uri) {
        //TODO: revisar si no hay que ver otras properties de las clases con mayor prioridad
        //     antes que las labels.
        Statement rdfsLabelProp = r.getProperty(RDFS.label);

        //Si la clase tiene LABEL entonces retornarla ya que será un nombre mucho
        //más legible.
        //TODO ver como operar con los distintos idiomas de las labels, actualmente se
        //     retorna un idioma al azar.
        if(rdfsLabelProp != null)
            return rdfsLabelProp.getObject().asLiteral().getString();

        //retorna el nombre local dentro de su namespace, tiene que ser una URI
        return r.getLocalName();
    }

    @Override
    protected String visitLiteral(Literal l) {
        return l.getString();
    }

    @Override
    protected String visitStmt(Resource r, Statement statement) {

        //TODO el predicado puede ser complejo, pasar por filtro recursivo.
        return statement.getPredicate().getLocalName();
    }

}
