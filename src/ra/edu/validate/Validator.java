package ra.edu.validate;

import ra.edu.utils.Color;
import ra.edu.validate.student.StudentValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])\\d{7}$");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    static {
        DATE_FORMAT.setLenient(false);
    }

    // Validate số nguyên
    public static int validateInteger(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(Color.RED + "Không được để trống. Vui lòng nhập lại." + Color.RESET);
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Số nguyên không hợp lệ. Vui lòng thử lại."+ Color.RESET);
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
                System.out.println(Color.RED + "Số thực không hợp lệ. Vui lòng thử lại." + Color.RESET);
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
            System.out.println(Color.RED + "Không được để trống. Vui lòng nhập lại." + Color.RESET);
        }
    }

    // Validate email
    public static String validateEmail(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            // Kiểm tra nếu email rỗng
            if (input.isEmpty()) {
                System.out.println(Color.RED + "Email không được để trống." + Color.RESET);
                continue; // Quay lại vòng lặp để người dùng nhập lại
            }

            // Kiểm tra email có đúng định dạng không
            if (EMAIL_PATTERN.matcher(input).matches()) {
                return input;
            }

            System.out.println(Color.RED + "Email không đúng định dạng. Ví dụ: example@gmail.com" + Color.RESET);
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
            System.out.println(Color.RED + "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng di động Việt Nam (10 số, bắt đầu bằng các đầu số hợp lệ như 09, 03, 07, 08, 05)." + Color.RESET);
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
                System.out.println(Color.RED + "Ngày không hợp lệ. Định dạng đúng là dd/MM/yyyy." + Color.RESET);
            }
        }
    }
    public static String validateUsername(Scanner sc) {
        while (true) {
            System.out.print("Nhập tên người dùng: ");
            String username = sc.nextLine().trim();

            // Kiểm tra tên người dùng không được rỗng
            if (username.isEmpty()) {
                System.out.println(Color.RED + "Tên người dùng không được để trống. Vui lòng thử lại." + Color.RESET);
                continue;
            }

            // Kiểm tra tên người dùng không chứa ký tự đặc biệt
            if (!Pattern.matches("^[a-zA-Z0-9_]+$", username)) {
                System.out.println(Color.RED + "Tên người dùng chỉ được chứa chữ cái, số và dấu gạch dưới. Vui lòng thử lại." + Color.RESET);
                continue;
            }

            return username;
        }
    }

    public static String validatePassword(Scanner sc) {
        while (true) {
            System.out.print("Nhập mật khẩu: ");
            String password = sc.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println(Color.RED + "Mật khẩu không được để trống. Vui lòng thử lại." + Color.RESET);
                continue;
            }

            if (password.length() < 6) {
                System.out.println(Color.RED + "Mật khẩu phải có ít nhất 6 ký tự. Vui lòng thử lại."+ Color.RESET);
                continue;
            }

            return password;
        }
    }

    public static String validatePassword(String message, Scanner sc) {
        String password;
        do {
            System.out.print(message);
            password = sc.nextLine();
            if (password.trim().isEmpty()) {
                System.out.println(Color.RED + "Mật khẩu không được để trống." + Color.RESET);
            }
        } while (password.trim().isEmpty());
        return password;
    }

    public static String validateNewPassword(String message, Scanner sc) {
        String password;
        boolean isValid;
        do {
            System.out.print(message);
            password = sc.nextLine();

            isValid = StudentValidator.isStrongPassword(password);

            if (!isValid) {
                System.out.println(Color.RED + "Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt." + Color.RESET);
            }
        } while (!isValid);

        return password;
    }

}
