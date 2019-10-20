package taskManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MemberDao extends AbstractDao<Member> {

    public MemberDao(DataSource datasource){
        super(datasource);
    }

    public long insert(Member member) throws SQLException{
        return insert(member, "INSERT INTO members (NAME, TASK, EMAIL) VALUES (?, ?, ?)");
    }

    @Override
    protected void insertObject(Member member, PreparedStatement statement) throws SQLException{
        statement.setString(1, member.getName());
        statement.setString(2, member.getTask());
        statement.setString(3, member.getEmail());
    }

    public List<Member> listAll() throws SQLException {
        return listAll("SELECT * FROM members");
    }

    @Override
    protected Member readObject(ResultSet resultset) throws SQLException {
        Member member = new Member();
        member.setName(resultset.getString("name"));
        member.setTask(resultset.getString("task"));
        member.setEmail(resultset.getString("email"));
        return member;
    }

    public Member retrieve(long id) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM members WHERE ID = ?")) {
                statement.setLong(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {

                    if(resultSet.next()){
                        return readObject(resultSet);
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
