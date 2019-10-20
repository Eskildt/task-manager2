package no.projectMembers.http;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ProjectMembersTest {


    @Test
    public void shouldRetrieveGroupMembers() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test");

        Connection connection = dataSource.getConnection();
        connection.createStatement().executeUpdate(
                "create table products(name varchar(100))"
        );

        ProjectMembers dao = new ProjectMembers(dataSource);
        String projectMemberName = pickOne(new String[]{"Apples", "Bananas", "Coconuts", "Dates"});
        //dao.insertProject(projectMemberName, "insert into ProjectMembers (name) values (?)");
        assertThat(dao.listAll("select * from projects")).contains(projectMemberName);
    }

    private String pickOne(String[] strings){ return strings[new Random().nextInt(strings.length)]; }

}
