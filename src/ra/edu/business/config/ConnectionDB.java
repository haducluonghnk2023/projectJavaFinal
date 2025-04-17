package ra.edu.business.config;

import ra.edu.utils.Color;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/project_java";
    private static final String USER = "root";
    private static final String PASSWORD = "new_password";

    public static Connection openConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(Color.RED + "Lỗi kết nối đến cơ sở dữ liệu do: " + e.getMessage() + Color.RESET);
        }
        catch (Exception e) {
            System.out.println(Color.RED + "Có lỗi không xác định khi kết nối CSDL" + e.getMessage() + Color.RESET);
        }
        return connection;
    }
    public static void closeConnection(Connection connection, CallableStatement callableStatement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(Color.RED + "Lỗi đóng kết nối đến cơ sở dữ liệu do: " + e.getMessage()  + Color.RESET);
            }
        }
        if (callableStatement != null) {
            try {
                callableStatement.close();
            } catch (SQLException e) {
                System.out.println(Color.RED + "Lỗi đóng CallableStatement do: " + e.getMessage() + Color.RESET);
            }
        }
    }
}
