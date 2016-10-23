package com.tmitim.timmybank;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class TaskMapper implements ResultSetMapper<Task> {
	@Override
	public Task map(int index, ResultSet r, StatementContext ctx) throws SQLException {

		Task task = new Task();
		task.setId(r.getInt("id"));
		task.setUserId(r.getInt("userId"));
		task.setMessage(r.getString("message"));
		task.setAccountableId(r.getInt("accountableId"));
		task.setCompleted(r.getBoolean("completed"));

		return task;
	}
}