package no.projectMembers.http;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDao extends AbstractProject<Order> {

    public OrderDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    protected void insertProjectMembers(Order order, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, order.getName());
    }

    @Override
    protected Order readObject(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setName(rs.getString(1));
        return order;
    }

    public void insert(Order project) throws SQLException {
        insert(project, "insert into orders (name) values (?)");
    }

    public List<Order> listAll() throws SQLException {
        return listAll("select * from orders");
    }
}
