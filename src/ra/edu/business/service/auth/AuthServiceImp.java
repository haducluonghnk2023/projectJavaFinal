package ra.edu.business.service.auth;

import ra.edu.business.dao.auth.AuthDAO;
import ra.edu.business.dao.auth.AuthDAOImp;
import ra.edu.business.model.admin.Admin;
import ra.edu.business.model.student.Student;


public class AuthServiceImp implements AuthService {
    private static Object currentUser = null;
    private final AuthDAO authDAO = new AuthDAOImp();

    @Override
    public Object login(String username, String password) {
        Admin admin = authDAO.adminLogin(username, password);
        if (admin != null) {
            currentUser = admin;
            return admin;
        }
        Student student = authDAO.studentLogin(username, password);
        if (student != null) {
            currentUser = student;
            return student;
        }
        return null;
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    @Override
    public Object getCurrentStudent() {
        return currentUser;
    }
}
