package ra.edu.business.dao.auth;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.admin.Admin;
import ra.edu.business.model.student.Student;

import java.time.format.DateTimeFormatter;

public interface AuthDAO extends AppDAO {
    Student studentLogin(String name, String password);
    Admin adminLogin(String username, String password);
}
