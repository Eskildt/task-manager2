package taskManager;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class TaskDaoTest {
    private DataSource dataSource = TestDatabase.testDataSource();

    @Test
    void shouldFindSavedTasks() throws SQLException{

        Task task = new Task();
        task.setName("test");
        TaskDao taskDao =new TaskDao(dataSource);

        taskDao.insert(task);
        assertThat(taskDao.listAll()).contains(task);
    }
}
