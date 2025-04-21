package ra.edu.validate.enrollment;

import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.util.Scanner;

public class EnrollmentValidator {
    public static String validateSortColumn(Scanner sc) {
        while (true) {
            System.out.print(Color.WHITE + "Chọn trường sắp xếp (name / registered_at): " + Color.RESET);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("name") || input.equals("registered_at")) {
                return input;
            }
            System.out.println(Color.RED + "Trường sắp xếp không hợp lệ. Vui lòng chọn 'name' hoặc 'registered_at'." + Color.RESET);
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

}
