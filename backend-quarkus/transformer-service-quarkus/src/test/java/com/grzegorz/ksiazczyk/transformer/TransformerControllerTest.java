package com.grzegorz.ksiazczyk.transformer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TransformerControllerTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().body("[[10,20,[30]],40]").post("/transform")
          .then()
             .statusCode(200)
             .body(is("[10,20,30,40], deep: 3"));
    }

}