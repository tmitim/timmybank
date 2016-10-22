import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;


@UseStringTemplate3StatementLocator
@RegisterMapper(TaskMapper.class)
public interface TaskDao {
	@SqlQuery("SELECT * FROM tasks WHERE id =:id")
	Task getTask(@Bind("id") int id);

	@SqlQuery("SELECT * FROM tasks WHERE userId =:userId")
	List<Task> getUserTasks(@Bind("userId") int userId);

	@SqlUpdate("INSERT INTO tasks (userId, accountableId, message, completed) VALUES (:userId, :accountableId, :message, :completed)")
	@GetGeneratedKeys
	int insert(@Bind("userId") int userId, @Bind("accountableId") int accountableId, @Bind("message") String message,
			@Bind("completed") boolean completed);

	@SqlUpdate("UPDATE tasks SET completed=:completed WHERE id=:id")
	void editPost(@Bind("id") int id, @Bind("completed") boolean completed);
}