package ra.edu.validate.student;

import ra.edu.business.model.student.Student;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StudentValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?\\d{9,15}$");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static int validateInteger(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Không được để trống. Vui lòng nhập lại.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Số nguyên không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    public static String validateName(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Họ tên không được để trống.");
            } else if (name.length() > 100) {
                System.out.println("Họ tên không được vượt quá 100 ký tự.");
            } else {
                return name;
            }
        }
    }

    public static LocalDate validateDob(String message, Scanner sc) {
        while (true) {
            System.out.print(message + " (yyyy-MM-dd): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Ngày sinh không được để trống.");
                continue;
            }
            try {
                LocalDate dob = LocalDate.parse(input, DATE_FORMAT);
                if (dob.isAfter(LocalDate.now())) {
                    System.out.println("Ngày sinh không hợp lệ (trong tương lai).");
                } else {
                    return dob;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ. Vui lòng nhập yyyy-MM-dd.");
            }
        }
    }

    public static String validateEmailA(String message, Scanner sc, List<Student> studentList) {
        while (true) {
            System.out.print(message);
            String email = sc.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email không được để trống.");
            } else if (email.length() > 100) {
                System.out.println("Email không được vượt quá 100 ký tự.");
            } else if (!isValidEmailFormat(email)) {
                System.out.println("Email không hợp lệ. Vui lòng nhập lại.");
            } else if (isEmailExistsA(email, studentList)) {
                System.out.println(Color.RED + "Email này đã tồn tại trong hệ thống. Vui lòng chọn email khác." + Color.RESET);
            } else {
                return email;
            }
        }
    }

    public static String validateEmail(String message, Scanner sc, List<Student> studentList, Student currentStudent) {
        while (true) {
            System.out.print(message);
            String email = sc.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email không được để trống.");
            } else if (email.length() > 100) {
                System.out.println("Email không được vượt quá 100 ký tự.");
            } else if (!isValidEmailFormat(email)) {
                System.out.println("Email không hợp lệ. Vui lòng nhập lại.");
            } else if (isEmailExists(email, studentList, currentStudent)) {
                System.out.println(Color.RED + "Email này đã tồn tại trong hệ thống. Vui lòng chọn email khác." + Color.RESET);
            } else {
                return email;
            }
        }
    }

    public static boolean isEmailExists(String email, List<Student> studentList, Student currentStudent) {
        for (Student s : studentList) {
            if (s.getEmail().equalsIgnoreCase(email) && s.getId() != currentStudent.getId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmailExistsA(String email, List<Student> studentList) {
        for (Student s : studentList) {
            if (s.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmailFormat(String email) {
        // Kiểm tra định dạng email cơ bản
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public static boolean validateSex(String message, Scanner sc) {
        while (true) {
            System.out.print(message + " (1-Nam, 0-Nữ): ");
            String input = sc.nextLine().trim();

            if (!input.equals("0") && !input.equals("1")) {
                System.out.println("Giới tính không hợp lệ. Vui lòng nhập 1 hoặc 0.");
            } else {
                return input.equals("1");
            }
        }
    }

    public static String validatePhone(String message, Scanner sc, List<Student> studentList) {
        while (true) {
            System.out.print(message + " (bỏ trống nếu không có): ");
            String phone = sc.nextLine().trim();

            if (phone.isEmpty()) {
                return null;
            }

            if (!PHONE_PATTERN.matcher(phone).matches()) {
                System.out.println("Số điện thoại không hợp lệ.");
            }
            else if (isPhoneNumberExists(phone, studentList)) {
                System.out.println("Số điện thoại này đã tồn tại trong hệ thống. Vui lòng nhập số khác.");
            }
            else {
                return phone;
            }
        }
    }

    public static boolean isPhoneNumberExists(String phone, List<Student> studentList) {
        for (Student student : studentList) {
            if (phone.equals(student.getPhone())) {
                return true;
            }
        }
        return false;
    }

    public static LocalDate validateCreatedAt(String message, Scanner sc) {
        while (true) {
            System.out.print(message + " (yyyy-MM-dd, bỏ trống để lấy ngày hiện tại): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return LocalDate.now();
            }
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMAT);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Ngày tạo không được sau ngày hiện tại.");
                } else {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ. Vui lòng nhập yyyy-MM-dd.");
            }
        }
    }

    public static String validateSortColumn(Scanner sc) {
        while (true) {
            System.out.print(Color.WHITE + "Chọn trường sắp xếp (course_name / registered_at): " + Color.RESET);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("course_name") || input.equals("registered_at")) {
                return input;
            }
            System.out.println(Color.RED + "Trường sắp xếp không hợp lệ. Vui lòng chọn 'course_name' hoặc 'registered_at'." + Color.RESET);
        }
    }

    public static String validateSortOrder(Scanner sc) {
        while (true) {
            System.out.print(Color.WHITE + "Chọn thứ tự sắp xếp (asc / desc): " + Color.RESET);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("asc") || input.equals("desc")) {
                return input;
            }
            System.out.println(Color.RED + "Thứ tự không hợp lệ. Vui lòng chọn 'asc' hoặc 'desc'." + Color.RESET);
        }
    }

    public static int validatePageSize(Scanner sc) {
        while (true) {
            String input = Validator.validateNonEmptyString(Color.WHITE + "Nhập số lượng hiển thị mỗi trang (1 - 100): " + Color.RESET, sc);
            try {
                int pageSize = Integer.parseInt(input);
                if (pageSize >= 1 && pageSize <= 100) {
                    return pageSize;
                }
                System.out.println(Color.RED + "Số lượng phải nằm trong khoảng từ 1 đến 100." + Color.RESET);
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Vui lòng nhập một số nguyên hợp lệ." + Color.RESET);
            }
        }
    }
    public static boolean isStrongPassword(String password) {
        // Mật khẩu phải có ít nhất 8 ký tự
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        // Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số, và một ký tự đặc biệt
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

}
