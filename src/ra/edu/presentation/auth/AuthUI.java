package ra.edu.presentation.auth;

import ra.edu.MainApplication;
import ra.edu.business.model.admin.Admin;
import ra.edu.business.model.student.Student;
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
            String username = ra.edu.validate.Validator.validateUsername(MainApplication.sc);
            String password = ra.edu.validate.Validator.validatePassword(MainApplication.sc);
            Object user = authService.login(username, password);
            if (user == null) {
                System.out.println(Color.RED + "Login failed. Please try again!" + Color.RESET);
            } else {
                loggedIn = true;
                if (user instanceof Admin) {
                    System.out.println(Color.GREEN + "Chào mừng Admin!" + Color.RESET);
                    AdminUI.displayMenuAdmin();
                } else if (user instanceof Student) {
                    System.out.println(Color.GREEN + "Chào mừng Student!" + Color.RESET);
                    StudentUI.displayMenuStudent();
                }
            }
        }
    }
}
