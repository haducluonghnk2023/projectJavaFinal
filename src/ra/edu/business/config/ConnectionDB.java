package ra.edu.business.config;

import ra.edu.exception.login.AppException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/project_java";
    private static final String USER = "root";
    private static final String PASSWORD = "new_password";

    public static Connection openConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new AppException("Lỗi kết nối đến cơ sở dữ liệu.", e);
        } catch (Exception e) {
            throw new AppException("Lỗi không xác định khi kết nối CSDL.", e);
        }
    }

    public static void closeConnection(Connection connection, CallableStatement callableStatement) {
        try {
            if (callableStatement != null) {
                callableStatement.close();
            }
        } catch (SQLException e) {
            throw new AppException("Lỗi khi đóng CallableStatement.", e);
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new AppException("Lỗi khi đóng kết nối cơ sở dữ liệu.", e);
        }
    }
}
