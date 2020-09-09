package com.grzegorz.ksiazczyk.transformer;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/async/transform")
public class TransformerAsyncController {

    Logger log = Logger.getLogger(TransformerAsyncController.class.getName());

    @Inject
    TransformerService transformerService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> transformToFlatArray(String input) {
        log.info(input);
        return Uni.createFrom().item(input)
            .onItem().transform(n -> {
                    Node root = transformerService.transform(input);
                    FlattenTree result = transformerService.transform(root);
                    return result.getValues().toString().replaceAll(" ", "") + ", deep: " + result.getMaxDepth();
            });
    }
}