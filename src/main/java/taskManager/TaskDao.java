package taskManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TaskDao extends AbstractDao<Task> {

    public TaskDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    protected void insertObject(Task task, PreparedStatement statement) throws SQLException {
        statement.setString(1, task.getName());
    }

    @Override
    protected Task readObject(ResultSet resultSet) throws SQLException{
        Task task = new Task();
        task.setName(resultSet.getString("name"));
        return task;
    }

    public void insert(Task member) throws SQLException {
        insert(member,"INSERT INTO tasks (name) VALUES (?)");
    }

    public List<Task> listAll() throws SQLException{
        return listAll("SELECT * FROM tasks");
    }
}

