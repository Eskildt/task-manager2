package taskManager;

import com.google.gson.Gson;
import no.projectMembers.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MemberDB {

    private static TaskDao taskDao;
    private static MemberDao memberDao;
    private PGSimpleDataSource dataSource;
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));



    public MemberDB() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        dataSource = new PGSimpleDataSource();

        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));


        Flyway.configure().dataSource(dataSource).load().migrate();

        memberDao = new MemberDao(dataSource);
        taskDao = new TaskDao(dataSource);
    }

    public static void main(String[] args) throws IOException, SQLException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.setFileLocation("src/main/resources");
        httpServer.start();

        new MemberDB().run();
    }

    public void run() throws IOException, SQLException {
        System.out.println("Choose: Create [Member] OR Create [Project]");
        String action = input.readLine();

        switch (action.toLowerCase()) {
            case "member":
                insertMember();
                break;
            case "project":
                insertTask();
                break;
        }

        System.out.println("Current Members " + memberDao.listAll());
        System.out.println("Current Projects " + taskDao.listAll());

        System.out.println(memberDao.listAll());
    }

    public static String listAllTasks() throws SQLException {
        List<Task> tasks = taskDao.listAll();
        String json = new Gson().toJson(tasks);
        return json;
    }

    public static String listAllMembers() throws SQLException {
        List<Member> members = memberDao.listAll();
        String json = new Gson().toJson(members);
        return json;
    }

    private void insertTask() throws IOException, SQLException {
        System.out.println("Please create project");
        Task task = new Task();
        task.setName(input.readLine());
        taskDao.insert(task);
    }

    private void insertMember() throws IOException, SQLException {

        Member member = new Member();

        System.out.println("Please type the name of a new Member.");
        member.setName(input.readLine());

        System.out.println("Please assign member to a project.");
        member.setTask(input.readLine());

        System.out.println("Please assign email to member.");
        member.setEmail(input.readLine());

        memberDao.insert(member);
    }
}
