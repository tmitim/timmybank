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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.auth0.jwt.JWTSigner;

@Path("/api/")
public class BasicResource {

	private String URL_BASE = "https://hack.modoapi.com/1.0.0-dev/";

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

		post("https://hack.modoapi.com/1.0.0-dev/vault/get_type_list");
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
		int id = taskDao.insert(task.getUserId(), task.getAccountableId(), task.getMessage(), false, task.getAmount());

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

	private String getJwt() {
		final String secret = "jWGZ7BhN0l3uLBkmE/M91UsSylqQprp8hACrqgUsEEDR20GBmaVPQd30q2lx3Mmyvmiyi1c8aFIpQMOMbFIWmw==";

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 60L; // expires claim. In this case the token
									// expires in 60 seconds

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("api_key", "97964300-57d5-4585-8624-d2ddd06b0500");
		claims.put("iat", iat);

		final String jwt = signer.sign(claims);

		return jwt;
	}

	private String post(String url) {

		HttpClient httpClient = HttpClientBuilder.create().build(); // Use this
																	// instead

		String json = "";
		try {
			HttpPost request = new HttpPost(url);

			request.addHeader("Authorization", "Token " + getJwt());

			System.out.println(getJwt());
			HttpResponse response = httpClient.execute(request);

			json = EntityUtils.toString(response.getEntity());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return json;
	}

	private String post(String url, String jsonBody) {

		HttpClient httpClient = HttpClientBuilder.create().build(); // Use this
																	// instead

		String json = "";
		try {
			HttpPost request = new HttpPost(url);

			request.addHeader("Authorization", "Token " + getJwt());
			StringEntity params = new StringEntity(jsonBody);
			// request.addHeader("content-type",
			// "application/x-www-form-urlencoded");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);

			json = EntityUtils.toString(response.getEntity());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return json;
	}
}
