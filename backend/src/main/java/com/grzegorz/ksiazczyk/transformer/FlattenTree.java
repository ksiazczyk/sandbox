package com.grzegorz.ksiazczyk.transformer;

import java.util.ArrayList;
import java.util.List;

public class FlattenTree {
    private List<String> values;
    private int depth;
    private int maxDepth;

    public FlattenTree() {
        this.values = new ArrayList<>();
        this.depth = 1;
        this.maxDepth = depth;
    }

    public void increaseDepth() {
        this.depth++;
        if (maxDepth < depth) {
            maxDepth = depth;
        }
    }

    public void decreaseDepth() {
        depth--;
    }

    public void addValue(String value) {
        this.values.add(value);
    }

    public List<String> getValues() {
        return values;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
