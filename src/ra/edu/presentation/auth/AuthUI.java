package ra.edu.presentation.auth;

import ra.edu.MainApplication;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.Status;
import ra.edu.business.service.auth.AuthServiceImp;
import ra.edu.presentation.admin.AdminUI;
import ra.edu.presentation.student.StudentUI;
import ra.edu.utils.Color;

public class AuthUI {
    public static void login() {
        AuthServiceImp authService = new AuthServiceImp();
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println(Color.MAGENTA + "=== LOGIN ===" + Color.RESET);
            String email = ra.edu.validate.Validator.validateEmail("Nhập email: ", MainApplication.sc);
            String password = ra.edu.validate.Validator.validatePassword(MainApplication.sc);

            Account account = (Account) authService.login(email, password);

            if (account == null) {
                System.out.println(Color.RED + "Đăng nhập không thành công. Vui lòng thử lại!" + Color.RESET);
            } else if (account.getStatus() != Status.ACTIVE) {
                System.out.printf(Color.YELLOW + "Tài khoản đang ở trạng thái [%s]. Vui lòng liên hệ quản trị viên.\n" + Color.RESET, account.getStatus());
            } else {
                loggedIn = true;
                switch (account.getRole()) {
                    case ADMIN:
                        System.out.println(Color.GREEN + "Chào mừng Admin!" + Color.RESET);
                        AdminUI.displayMenuAdmin();
                        break;

                    case STUDENT:
                        System.out.println(Color.GREEN + "Chào mừng Student!" + Color.RESET);
                        StudentUI.displayMenuStudent();
                        break;

                    default:
                        System.out.println(Color.RED + "Không xác định được vai trò người dùng!" + Color.RESET);
                        break;
                }
            }
        }
    }

    public static void logout() {
        AuthServiceImp authService = new AuthServiceImp();
        authService.logout();
        System.out.println(Color.GREEN + "Bạn đã đăng xuất!" + Color.RESET);
        login();
    }
}
