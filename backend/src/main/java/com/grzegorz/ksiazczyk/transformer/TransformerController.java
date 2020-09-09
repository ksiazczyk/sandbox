package com.grzegorz.ksiazczyk.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransformerController {

    @Autowired
    TransformerService transformerService;

    @PostMapping("/transform/")
    public ResponseEntity<Object> transform(@RequestBody String input) {
        try {
            Node root = transformerService.transform(input);
            FlattenTree result = transformerService.transform(root);
            return ResponseEntity.ok(result.getValues().toString().replaceAll(" ", "") + ", deep: " + result.getMaxDepth());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
