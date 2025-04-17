package ra.edu.validate.course;


import ra.edu.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CourseValidator {

    // Validate tên khóa học: không rỗng, tối đa 100 ký tự
    public static String validateCourseName(Scanner sc) {
        while (true) {
            String name = Validator.validateNonEmptyString("Nhập tên khóa học: ", sc);
            if (name.length() <= 100) {
                return name;
            }
            System.out.println("\u001B[31mTên khóa học không được vượt quá 100 ký tự.\u001B[0m");
        }
    }

    // Validate thời lượng khóa học (buoi): số nguyên dương
    public static int validateDuration(Scanner sc) {
        while (true) {
            int duration = Validator.validateInteger("Nhập thời lượng khóa học (buổi): ", sc);
            if (duration > 0) {
                return duration;
            }
            System.out.println("\u001B[31mThời lượng phải lớn hơn 0.\u001B[0m");
        }
    }

    // Validate tên giảng viên: không rỗng, tối đa 100 ký tự
    public static String validateInstructor(Scanner sc) {
        while (true) {
            String instructor = Validator.validateNonEmptyString("Nhập tên giảng viên: ", sc);
            if (instructor.length() <= 100) {
                return instructor;
            }
            System.out.println("\u001B[31mTên giảng viên không được vượt quá 100 ký tự.\u001B[0m");
        }
    }

    // Tự động lấy ngày hiện tại định dạng yyyy-MM-dd
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}

