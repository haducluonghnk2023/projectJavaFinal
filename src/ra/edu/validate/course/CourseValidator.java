package ra.edu.validate.course;


import ra.edu.business.model.course.Course;
import ra.edu.utils.Color;
import ra.edu.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CourseValidator {

    // Validate tên khóa học: không rỗng, tối đa 100 ký tự
    public static String validateCourseName(Scanner sc, List<Course> courseList) {
        while (true) {
            String rawName = Validator.validateNonEmptyString("Nhập tên khóa học: ", sc);
            String name = normalizeName(rawName);

            if (name.length() > 100) {
                System.out.println(Color.RED + "Tên khóa học không được vượt quá 100 ký tự." + Color.RESET);
                continue;
            }

            boolean isDuplicate = courseList.stream()
                    .anyMatch(course -> normalizeName(course.getName()).equalsIgnoreCase(name));

            if (isDuplicate) {
                System.out.println(Color.RED + "Tên khóa học đã tồn tại. Vui lòng nhập tên khác." + Color.RESET);
                continue;
            }

            return name;
        }
    }

    private static String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }


    // Validate thời lượng khóa học (buổi): số nguyên dương
    public static int validateDuration(Scanner sc) {
        while (true) {
            int duration = Validator.validateInteger("Nhập thời lượng khóa học (buổi): ", sc);
            if (duration > 0) {
                return duration;
            }
            System.out.println(Color.RED + "Thời lượng phải lớn hơn 0." + Color.RESET);
        }
    }

    // Validate tên giảng viên: không rỗng, tối đa 100 ký tự
    public static String validateInstructor(Scanner sc) {
        while (true) {
            String instructor = Validator.validateNonEmptyString("Nhập tên giảng viên: ", sc);
            if (instructor.length() <= 100) {
                return instructor;
            }
            System.out.println(Color.RED + "Tên giảng viên không được vượt quá 100 ký tự." + Color.RESET);
        }
    }

    // Tự động lấy ngày hiện tại định dạng yyyy-MM-dd
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String validateSortColumn(Scanner sc) {
        while (true) {
            System.out.print(Color.WHITE + "Chọn trường sắp xếp (name / id): " + Color.RESET);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("name") || input.equals("id")) {
                return input;
            }
            System.out.println(Color.RED + "Trường sắp xếp không hợp lệ. Vui lòng chọn 'name' hoặc 'id'." + Color.RESET);
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

