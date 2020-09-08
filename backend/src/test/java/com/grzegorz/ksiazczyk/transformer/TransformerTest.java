package com.grzegorz.ksiazczyk.transformer;

import com.grzegorz.ksiazczyk.ServiceApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
class TransformerTest {

    Logger log = Logger.getLogger(TransformerTest.class.getName());
    @Autowired
    TransformerService transformerService;

    @Test
    void shouldTransformToTreeWithSuccess() {
        Node root = transformerService.transform("[[10,[[20, [30]]],[40]]]");

        Assertions.assertEquals("N[N[10, N[N[20, N[30]]], N[40]]]", root.toString());
    }

    @ParameterizedTest
    @MethodSource("flattenTestDataProvider")
    void flatten(String input, String expectedValues, int expectedDepth) {

        Node root = transformerService.transform(input);
        FlattenTree result = transformerService.transform(root);

        Assertions.assertEquals(expectedDepth, result.getMaxDepth());
        Assertions.assertEquals(expectedValues, result.getValues().toString());

    }

    static Stream<Arguments> flattenTestDataProvider() {
        return Stream.of(
                Arguments.of("[10]", "[10]", 1),
                Arguments.of("[[10]]", "[10]", 2),
                Arguments.of("[]", "[]", 1),
                Arguments.of("[[10,20,30],40]", "[10, 20, 30, 40]", 2),
                Arguments.of("[[10,20,[30]],40]", "[10, 20, 30, 40]", 3),
                Arguments.of("[[10,[[20, [30]]],[40]]]", "[10, 20, 30, 40]", 5),
                Arguments.of("[10, 20, 30]", "[10, 20, 30]", 1)
        );
    }

    @Test
    void shouldWorkWithABitMoreInputData() {
        long start1 = System.currentTimeMillis();
        String input = generateTestData(1000, 1, 4);
        long start2 = System.currentTimeMillis();
        Node root = transformerService.transform(input);
        long start3 = System.currentTimeMillis();

        FlattenTree result = transformerService.transform(root);
        long stop = System.currentTimeMillis();

        log.info("input: " + input);
        log.info("result: " + result.getValues());

        log.info("input data generated in " + (start2 - start1));
        log.info("converted to Tree in " + (start3 - start2));
        log.info("Tree to output converted in " + (stop - start3));
        log.info("input data converted to output in " + (stop - start2));

        log.info("deep: " + result.getMaxDepth());

        String trimmedInput = cleanForAssertion(input);
        String trimmedResult = cleanForAssertion(result.getValues().toString());
        Assertions.assertEquals(trimmedInput, trimmedResult);
    }

    @Test
    void shouldWorkWithBigInputData() {
        long start1 = System.currentTimeMillis();
        String input = generateTestData(1000000, 1, 3);
        long start2 = System.currentTimeMillis();
        Node root = transformerService.transform(input);
        long start3 = System.currentTimeMillis();

        FlattenTree result = transformerService.transform(root);
        long stop = System.currentTimeMillis();

        log.info("input data generated in " + (start2 - start1));
        log.info("converted to Tree in " + (start3 - start2));
        log.info("Tree to output converted in " + (stop - start3));
        log.info("input data converted to output in " + (stop - start2));

        log.info("deep: " + result.getMaxDepth());

        String trimmedInput = cleanForAssertion(input);
        String trimmedResult = cleanForAssertion(result.getValues().toString());
        Assertions.assertEquals(trimmedInput, trimmedResult);

    }

    String generateTestData(int nodesCount, int minArrayLength, int maxArrayLength) {

        int arrayLengthRange = maxArrayLength - minArrayLength;

        Queue<Node> nodeQueue = new ArrayDeque<>();

        Node root = new Node();
        nodeQueue.add(root);

        boolean hasOnlyLeafes = true;

        while (nodesCount > 0 && nodeQueue.size() > 0 && nodesCount - nodeQueue.size() > 0) {
            Node parent = nodeQueue.remove();
            int numberOfKids = (int) (arrayLengthRange * Math.random()) + minArrayLength;
            //make sure we will have exactly that number of nodes we want :)
            if (numberOfKids > nodesCount - nodeQueue.size()) {
                numberOfKids = nodesCount;
            }
            nodesCount -= numberOfKids;

            hasOnlyLeafes = true;
            for (; numberOfKids > 0; numberOfKids--) {
                if (Math.random() > 0.5
                        || (numberOfKids == 1 && hasOnlyLeafes)) {
                    //create Node
                    hasOnlyLeafes = false;
                    Node kid = new Node();
                    parent.addNode(kid);
                    nodeQueue.add(kid);
                } else {
                    //create Leaf
                    Leaf kid = new Leaf();
                    //ASCII 48 -> 0, 57 -> 9
                    kid.addDigitChar((char) ((int) ((57 - 48) * Math.random()) + 48));
                    kid.addDigitChar((char) ((int) ((57 - 48) * Math.random()) + 48));
                    parent.addNode(kid);
                }

            }

        }

        while (!nodeQueue.isEmpty()) {
            Node parent = nodeQueue.remove();
            Leaf kid = new Leaf();
            kid.addDigitChar((char) ((int) ((57 - 48) * Math.random()) + 48));
            parent.addNode(kid);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = printInputData(root, stringBuilder);

        return stringBuilder.toString();
    }

    private StringBuilder printInputData(Node root, StringBuilder stringBuilder) {
        stringBuilder.append("[");
        List<Node> children = root.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node currentNode = children.get(i);
            if (currentNode instanceof Leaf) {
                String value = ((Leaf) currentNode).getValue();
                stringBuilder.append(value);
            } else {
                stringBuilder.append("[");
                stringBuilder = printInputData(currentNode, stringBuilder);
                stringBuilder.append("]");
            }
            if (i != children.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder;
    }

    private String cleanForAssertion(String input) {
        return input
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll(",", "")
                .replaceAll(" ", "");
    }
}