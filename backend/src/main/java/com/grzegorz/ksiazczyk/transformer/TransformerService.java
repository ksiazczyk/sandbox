package com.grzegorz.ksiazczyk.transformer;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TransformerService {

    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";

    public Node transform(String input) {
        input = input.replaceAll(SPACE, EMPTY_STRING);

        Node root = new Node();
        if (!StringUtils.isEmpty(input)) {
            // we already created Root,
            // so for consistency of input data we will remove first char -> [ and last char -> ] from input string
            input = input.substring(1, input.length() - 1);

            convertToTree(root, input, 0);
        }
        return root;
    }

    public FlattenTree transform(Node node) {
        FlattenTree result = new FlattenTree();
        flattenNode(node, result);
        return result;
    }

    private void flattenNode(Node node, FlattenTree flattenTree) {

        for (Node currentNode : node.getChildren()) {
            if (currentNode instanceof Leaf) {
                String value = ((Leaf) currentNode).getValue();
                flattenTree.addValue(value);
            } else {
                flattenTree.increaseDepth();
                flattenNode(currentNode, flattenTree);
                flattenTree.decreaseDepth();
            }
        }
    }

    private int convertToTree(Node parent, String input, int inputIndex) {
        while (inputIndex < input.length()) {
            char c = input.charAt(inputIndex);
            if (c == 91) { //ASCII 91 -> [
                inputIndex++;
                Node currentNode = new Node();
                parent.addNode(currentNode);
                inputIndex = convertToTree(currentNode, input, inputIndex);
            } else if (isDigit(c)) {
                Leaf leaf = new Leaf();
                leaf.addDigitChar(c);
                inputIndex++;

                while (inputIndex < input.length() && isDigit(input.charAt(inputIndex))) {
                    leaf.addDigitChar(input.charAt(inputIndex));
                    inputIndex++;
                }
                //here is option for input data error check
                //for next char, after digit always it should be ',' or ']'
                //not implemented, it may be done in the future
                parent.addNode(leaf);
            } else if (c == 93) { //ASCII 93 -> ]
                inputIndex++;
                break;
            } else {
                inputIndex++;
                //other characters are not important :P just skip
            }

        }
        return inputIndex;
    }

    private boolean isDigit(char c) { //ASCII 48 -> 0, 57 -> 9
        return c >= 48 && c <= 57;
    }
}
