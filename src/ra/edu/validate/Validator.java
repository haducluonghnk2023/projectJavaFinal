package ra.edu.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    static {
        DATE_FORMAT.setLenient(false);
    }

    // Validate số nguyên
    public static int validateInteger(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mSố nguyên không hợp lệ. Vui lòng thử lại.\u001B[0m");
            }
        }
    }

    // Validate số thực
    public static double validateDouble(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mSố thực không hợp lệ. Vui lòng thử lại.\u001B[0m");
            }
        }
    }

    // Validate chuỗi không rỗng
    public static String validateNonEmptyString(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("\u001B[31mKhông được để trống. Vui lòng nhập lại.\u001B[0m");
        }
    }

    // Validate email
    public static String validateEmail(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (EMAIL_PATTERN.matcher(input).matches()) {
                return input;
            }
            System.out.println("\u001B[31mEmail không đúng định dạng. Ví dụ: example@gmail.com\u001B[0m");
        }
    }

    // Validate số điện thoại Việt Nam
    public static String validatePhone(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (PHONE_PATTERN.matcher(input).matches()) {
                return input;
            }
            System.out.println("\u001B[31mSố điện thoại không hợp lệ. Phải có 10 chữ số và bắt đầu bằng số 0.\u001B[0m");
        }
    }

    // Validate ngày theo định dạng dd/MM/yyyy
    public static String validateDate(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                DATE_FORMAT.parse(input);
                return input;
            } catch (ParseException e) {
                System.out.println("\u001B[31mNgày không hợp lệ. Định dạng đúng là dd/MM/yyyy.\u001B[0m");
            }
        }
    }
    public static String validateUsername(Scanner sc) {
        while (true) {
            System.out.print("Nhập tên người dùng: ");
            String username = sc.nextLine().trim();

            // Kiểm tra tên người dùng không được rỗng
            if (username.isEmpty()) {
                System.out.println("\u001B[31mTên người dùng không được để trống. Vui lòng thử lại.\u001B[0m");
                continue;
            }

            // Kiểm tra tên người dùng không chứa ký tự đặc biệt
            if (!Pattern.matches("^[a-zA-Z0-9_]+$", username)) {
                System.out.println("\u001B[31mTên người dùng chỉ được chứa chữ cái, số và dấu gạch dưới. Vui lòng thử lại.\u001B[0m");
                continue;
            }

            return username;
        }
    }

    public static String validatePassword(Scanner sc) {
        while (true) {
            System.out.print("Nhập mật khẩu: ");
            String password = sc.nextLine().trim();

            // Kiểm tra mật khẩu không được rỗng
            if (password.isEmpty()) {
                System.out.println("\u001B[31mMật khẩu không được để trống. Vui lòng thử lại.\u001B[0m");
                continue;
            }

            // Kiểm tra mật khẩu dài ít nhất 6 ký tự
            if (password.length() < 6) {
                System.out.println("\u001B[31mMật khẩu phải có ít nhất 6 ký tự. Vui lòng thử lại.\u001B[0m");
                continue;
            }

            return password;
        }
    }

}
