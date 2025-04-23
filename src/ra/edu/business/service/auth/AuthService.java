package ra.edu.business.service.auth;

import ra.edu.business.model.student.Student;

public interface AuthService {
    Object login(String username, String password);
    void logout();
    boolean isAuthenticated();
    Object  getCurrentStudent();

}
