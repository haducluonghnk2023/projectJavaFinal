package ra.edu.presentation.student;

import ra.edu.MainApplication;
import ra.edu.presentation.auth.AuthUI;
import ra.edu.utils.Color;

public class StudentUI {
    // Hiển thị menu cho sinh viên
    public static void displayMenuStudent() {
        boolean continueProgram = true;
        do {
            System.out.println(Color.MAGENTA + "=== STUDENT UI ===" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Xem danh sách khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Tìm kiếm khóa học theo tên" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Đăng ký khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Xem khóa học đã đăng ký" + Color.RESET);
            System.out.println(Color.YELLOW + "5. Sắp xếp khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "6. Hủy đăng ký khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "7. Đổi mật khẩu tài khoản" + Color.RESET);
            System.out.println(Color.YELLOW + "8. Đăng xuất" + Color.RESET);
            System.out.print(Color.RESET + "Chọn tùy chọn: " + Color.RESET);
            int choice = MainApplication.sc.nextInt();
            MainApplication.sc.nextLine();

            switch (choice) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7 :
                    break;
                case 8:
                    System.out.println(Color.GREEN + "Đăng xuất thành công!" + Color.RESET);
                    continueProgram = false;
                    AuthUI.login();
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
            }
        } while (continueProgram);
    }
}
