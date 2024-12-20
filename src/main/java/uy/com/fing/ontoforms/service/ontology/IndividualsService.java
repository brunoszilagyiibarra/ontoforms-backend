package uy.com.fing.ontoforms.service.ontology;

import lombok.AllArgsConstructor;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;
import uy.com.fing.ontoforms.service.ontology.persistence.OntologyRepository;
import uy.com.fing.ontoforms.service.ontology.processors.RDFNodePrettyLabelProcessor;

import java.util.List;

@Service
@AllArgsConstructor
public class IndividualsService {

    private OntologyRepository ontologyRepository;
    private final RDFNodePrettyLabelProcessor prettyLabelProcessor;

    public List<IndividualDescriptor> getIndividuals(String ontoId) {
        return mapToIndividualDescriptor(ontologyRepository.getIndividualsByOntoId(ontoId));
    }

    public List<IndividualDescriptor> getOntologyClassIndividuals(String ontoId, String classUri) {
        return mapToIndividualDescriptor(ontologyRepository.getIndividualsByOntoIdAndClassUri(ontoId, classUri));
    }

    public List<IndividualDescriptor> getOntologyClassIndividuals(String ontoId, Resource ontClass) {
        return mapToIndividualDescriptor(ontologyRepository.getIndividualsByOntoIdAndResource(ontoId, ontClass));
    }

    private List<IndividualDescriptor> mapToIndividualDescriptor(List<Individual> individuals) {
        return individuals.stream().map(
                i -> new IndividualDescriptor(i.getURI(), prettyLabelProcessor.apply(i))
        ).toList();
    }

    public record IndividualDescriptor(String uri, String prettyName) {}

}
