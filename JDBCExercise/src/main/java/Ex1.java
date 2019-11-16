import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Ex1 {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/Computer_Firm";
        String user = "matan";
        String password = "1q2w3e4r";
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn.getTransactionIsolation());
    }
}
