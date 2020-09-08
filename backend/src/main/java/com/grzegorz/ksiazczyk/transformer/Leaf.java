package com.grzegorz.ksiazczyk.transformer;

public class Leaf extends Node {
    private String value;

    public Leaf() {
        this.value = "";
    }

    public void addDigitChar(char c) {
        value = value + c;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
