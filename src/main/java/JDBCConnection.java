import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        String url = "jdbc:postgresql://localhost:5432/abb_tech";
        String username = "psg_user";
        String password = "pass";

        return DriverManager.getConnection(url, username, password);
    }
}