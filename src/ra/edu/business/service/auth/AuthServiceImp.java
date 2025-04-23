package ra.edu.business.service.auth;

import ra.edu.business.dao.auth.AuthDAO;
import ra.edu.business.dao.auth.AuthDAOImp;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.admin.Admin;
import ra.edu.business.model.student.Student;
import ra.edu.exception.login.AppException;


public class AuthServiceImp implements AuthService {
    private static Object currentUser = null;
    private final AuthDAO authDAO = new AuthDAOImp();

    @Override
    public Object login(String email, String password) {
        boolean emailExists = authDAO.checkEmailExists(email);

        if (!emailExists) {
            throw new AppException("Email không tồn tại.");
        }

        Account account = authDAO.accountLogin(email, password);

        if (account == null) {
            throw new AppException("Mật khẩu không chính xác.");
        }

        if (account.getRole() == null) {
            throw new AppException("Vai trò tài khoản không hợp lệ.");
        }

        currentUser = account;
        return account;
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
