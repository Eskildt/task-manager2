package no.projectMembers.http;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectMembers extends AbstractProject<String> {

    public ProjectMembers(DataSource dataSource) { super(dataSource); }

    @Override
    protected void insertProjectMembers(String project, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, project);
    }

    @Override
    protected String readObject(ResultSet rs) throws SQLException {
        return rs.getString("name");
    }

    public void insert(String project) throws SQLException{
        insert(project, "insert into products (name) values (?)");
    }

    public List<String> list() throws SQLException {
        return listAll("select * from projects");
    }
}
