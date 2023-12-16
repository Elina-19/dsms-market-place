package ru.itis.oncall;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.extern.slf4j.Slf4j;
import ru.itis.oncall.service.GeneratorService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/generator")
@RegisterForReflection
public class GeneratorResource {

    @Inject
    GeneratorService generatorService;

    @GET
    @Path("/users")
    public Response generateUsers(@QueryParam("count") Integer count) {
        generatorService.generateUsers(count);
        return Response.noContent().build();
    }

    @GET
    @Path("/visits")
    public Response generateVisits(@QueryParam("count") Integer count) {
        generatorService.generateVisits(count);
        return Response.noContent().build();
    }

    @GET
    @Path("/supportServiceRequests")
    public Response generateSupportServiceRequests(@QueryParam("count") Integer count) {
        generatorService.generateSupportServiceRequests(count);
        return Response.noContent().build();
    }

    @GET
    @Path("/itemToCart")
    public Response generateItemToCart(@QueryParam("count") Integer count) {
        generatorService.generateItemToCart(count);
        return Response.noContent().build();
    }
}
