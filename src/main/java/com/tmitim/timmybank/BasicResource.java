package com.tmitim.timmybank;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/")
public class BasicResource {

	private TaskDao taskDao;

	public BasicResource(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	@GET
	@Path("/test/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBase() {
		Map<String, String> map = new HashMap<>();
		map.put("hello", "world");
		return Response.ok(map).build();
	}

	@PUT
	@Path("/task/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setComplete(@PathParam("taskId") int taskId) {
		taskDao.setTaskCompletion(taskId, true);
		Task task = taskDao.getTask(taskId);
		return Response.ok(task).build();

	}

	@POST
	@Path("/task")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTask(@Valid Task task) {
		int id = taskDao.insert(task.getUserId(), task.getAccountableId(), task.getMessage(), false);

		Task task2 = taskDao.getTask(id);
		return Response.ok(task2).build();
	}

	@GET
	@Path("/task/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTask(@PathParam("taskId") int taskId) {

		System.out.println(taskId);
		Task task = taskDao.getTask(taskId);
		return Response.ok(task).build();

	}

	@GET
	@Path("/task/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserTasks(@PathParam("userId") int userId) {
		return Response.ok(taskDao.getUserTasks(userId)).build();
	}

	@GET
	@Path("/task/partner/{accountableId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccountableTasks(@PathParam("accountableId") int accountableId) {
		return Response.ok(taskDao.getAccountableTasks(accountableId)).build();
	}
}
