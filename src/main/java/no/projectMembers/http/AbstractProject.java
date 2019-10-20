package no.projectMembers.http;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProject<T> {
    protected DataSource dataSource;

    public AbstractProject(DataSource dataSource) { this.dataSource = dataSource; }

    public void insert(T project, String sql1) throws SQLException {
        try (Connection connection = dataSource.getConnection()){
                    String sql = sql1;
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                insertProjectMembers(project, stmt);
                stmt.executeUpdate();
            }
        }
    }

    protected abstract void insertProjectMembers(T project, PreparedStatement stmt) throws SQLException;

    public List<T> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    List<T> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(readObject(rs));
                    }

                    return result;
                }
            }
        }
    }


    protected abstract T readObject(ResultSet rs) throws SQLException;
}


/*
    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter a project member name to insert: ");
        String projectMemberName = new Scanner(System.in).nextLine();

        Properties properties = new Properties();
        properties.load(new FileReader("project.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/project");
        dataSource.setUser("project");
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        ProjectMembers projectMembers = new ProjectMembers(dataSource);
        projectMembers.insertMember(projectMemberName);

        System.out.println(projectMembers.listAll());
    }

 */