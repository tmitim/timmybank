package com.tmitim.timmybank;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(TaskMapper.class)
public interface TaskDao {
	@SqlQuery("SELECT * FROM tasks WHERE id=:id")
	Task getTask(@Bind("id") int id);

	@SqlQuery("SELECT * FROM tasks WHERE userId=:userId")
	List<Task> getUserTasks(@Bind("userId") int userId);

	@SqlUpdate("INSERT INTO tasks (userId, accountableId, message, completed, amount) VALUES (:userId, :accountableId, :message, :completed, :amount)")
	@GetGeneratedKeys
	int insert(@Bind("userId") int userId, @Bind("accountableId") int accountableId, @Bind("message") String message,
			@Bind("completed") boolean completed, @Bind("amount") int amount);

	@SqlUpdate("UPDATE tasks SET completed=:completed WHERE id=:id")
	void setTaskCompletion(@Bind("id") int id, @Bind("completed") boolean completed);

	@SqlQuery("SELECT * FROM tasks WHERE accountableId =:accountableId")
	List<Task> getAccountableTasks(@Bind("accountableId") int accountableId);
}