package uy.com.fing.ontoforms.service.ontology;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OntoTree {

    private final OntoNode data;
    private final List<OntoTree> children;

    public OntoTree(OntoNode rootData) {
        data = rootData;
        children = new ArrayList<>();
    }


    public OntoTree addChildren(OntoNode data) {
        var e = new OntoTree(data);
        children.add(e);
        return e;
    }

    public record OntoNode(String className, String uri){}
}
