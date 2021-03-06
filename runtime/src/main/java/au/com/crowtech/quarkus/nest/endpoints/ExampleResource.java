package au.com.crowtech.quarkus.nest.endpoints;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.com.crowtech.quarkus.nest.beans.Echo;

@Path("/v1/api/nest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {

    @Inject
    Echo echo;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(String foo) {
        return echo.echo(foo);
    }
}