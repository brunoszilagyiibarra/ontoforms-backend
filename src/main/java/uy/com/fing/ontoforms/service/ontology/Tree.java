package uy.com.fing.ontoforms.service.ontology;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Tree<T> {
    private final T data;
    private final List<Tree<T>> children;

    public Tree(T rootData) {
        data = rootData;
        children = new ArrayList<>();
    }
}