package com.grzegorz.ksiazczyk.transformer;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/transform")
public class TransformerController {

    Logger log = Logger.getLogger(TransformerController.class.getName());

    @Inject
    TransformerService transformerService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(String input) {
        log.info(input);
        Node root = transformerService.transform(input);
        FlattenTree result = transformerService.transform(root);

        return result.getValues().toString().replaceAll(" ", "") + ", deep: " + result.getMaxDepth();
    }
}