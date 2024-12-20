package uy.com.fing.ontoforms.service.ontology.processors;

import lombok.AllArgsConstructor;
import org.apache.jena.rdf.model.*;

import java.util.function.Function;

/**
 * La idea de esta clase es la de hcer un wrapper sobre los RDFVisitor para
 * proveer una interfaz más amigable para operar donde no tengamos que castear el tipo
 * de retorno y donde el propio visitor sea el procesador.
 *
 * Se implementa interfaz function para que sea sencillo usar sustituir en lambdas por
 * esta misma funcion.
 *
 * @param <T> tipo de retorno.
 */
abstract class RDFNodeProcessor<T> implements Function<RDFNode, T> {

    private final RDFVisitorWithTypes<T> rdfVisitorWithTypes;

    protected RDFNodeProcessor() {
        rdfVisitorWithTypes = new RDFVisitorWithTypes<>(this);
    }

    @Override
    public final T apply(RDFNode node) {
        return (T) node.visitWith(rdfVisitorWithTypes);
    }

    protected abstract T visitBlank(Resource r, AnonId id);
    protected abstract T visitURI(Resource r, String uri);
    protected abstract T visitLiteral(Literal l);
    protected abstract T visitStmt(Resource r, Statement statement);

    @AllArgsConstructor
    private static class RDFVisitorWithTypes<T> implements RDFVisitor{

        private RDFNodeProcessor<T> rdfNodeProcessor;

        @Override
        public T visitBlank(Resource r, AnonId id) {
            return rdfNodeProcessor.visitBlank(r, id);
        }

        @Override
        public T visitURI(Resource r, String uri) {
            return rdfNodeProcessor.visitURI(r, uri);
        }

        @Override
        public T visitLiteral(Literal l) {
            return rdfNodeProcessor.visitLiteral(l);
        }

        @Override
        public T visitStmt(Resource r, Statement statement) {
            return rdfNodeProcessor.visitStmt(r, statement);
        }
    }


}
