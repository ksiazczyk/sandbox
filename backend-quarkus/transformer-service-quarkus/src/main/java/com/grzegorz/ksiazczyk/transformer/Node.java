package com.grzegorz.ksiazczyk.transformer;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private List<Node> children;

    public Node() {
        children = new ArrayList<>();
    }

    public void addNode(Node node) {
        this.children.add(node);
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "N" + children;
    }
}
