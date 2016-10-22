import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/")
public class BasicResource {

	private TaskDao taskDao;

	public BasicResource(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	@GET
	@Path("/test/")
	public Response getBase() {
		return Response.ok("Hello World!").build();
	}

	@PUT
	@Path("/task/{taskId)")
	public Response setComplete(@PathParam("taskId") int taskId) {
		taskDao.setTaskCompletion(taskId, true);

		return Response.ok().build();

	}
}
