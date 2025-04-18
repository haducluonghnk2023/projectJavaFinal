package ra.edu.business.service.auth;

import ra.edu.business.dao.auth.AuthDAO;
import ra.edu.business.dao.auth.AuthDAOImp;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.admin.Admin;
import ra.edu.business.model.student.Student;


public class AuthServiceImp implements AuthService {
    private static Object currentUser = null;
    private final AuthDAO authDAO = new AuthDAOImp();

    @Override
    public Object login(String email, String password) {
        Account account = authDAO.accountLogin(email, password);
        if (account != null) {
            switch (account.getRole()) {
                case ADMIN:
                    currentUser = account;
                    return account;
                case STUDENT:
                    currentUser = account;
                    return account;
                default:
                    return null;
            }
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
