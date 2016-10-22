import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api/")
public class BasicResource {

	public BasicResource() {
	}

	@GET
	@Path("/test/")
	public Response getBase() {
		return Response.ok("Hello World!").build();
	}

}
