package ra.edu.presentation.student;

import ra.edu.MainApplication;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.enrollment.Status;
import ra.edu.business.model.student.Student;
import ra.edu.business.service.course.CourseService;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.student.StudentService;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.auth.AuthUI;
import ra.edu.utils.Color;
import ra.edu.utils.PageInfo;
import ra.edu.validate.Validator;
import ra.edu.validate.course.CourseValidator;
import ra.edu.validate.student.StudentValidator;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentUI {
    public static void displayMenuStudent() {
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        EnrollmentServiceImp enrollmentServiceImp = new EnrollmentServiceImp();
        boolean continueProgram = true;
        do {

            System.out.println(Color.CYAN + "================================ STUDENT UI =================================" + Color.RESET);
            System.out.println(Color.CYAN + "╔════════════╦═══════════════════════════════════════════════════════════════╗");
            System.out.printf("║ %-10s ║ %-61s ║\n", "Lựa chọn", "Mô tả");
            System.out.println("╠════════════╬═══════════════════════════════════════════════════════════════╣");

            System.out.printf("║ %-10s ║ %-61s ║\n", "1", "Xem danh sách khóa học");
            System.out.printf("║ %-10s ║ %-61s ║\n", "2", "Tìm kiếm khóa học theo tên");
            System.out.printf("║ %-10s ║ %-61s ║\n", "3", "Đăng ký khóa học");
            System.out.printf("║ %-10s ║ %-61s ║\n", "4", "Xem khóa học đã đăng ký");
            System.out.printf("║ %-10s ║ %-61s ║\n", "5", "Sắp xếp khóa học theo tên/ ngày đăng ký - tăng dần / giảm dần");
            System.out.printf("║ %-10s ║ %-61s ║\n", "6", "Hủy đăng ký khóa học");
            System.out.printf("║ %-10s ║ %-61s ║\n", "7", "Đổi mật khẩu tài khoản");
            System.out.printf("║ %-10s ║ %-61s ║\n", "8", "Đăng xuất");
            System.out.printf("║ %-10s ║ %-61s ║\n", "9", "Thoát chương trình");

            System.out.println("╚════════════╩═══════════════════════════════════════════════════════════════╝" + Color.RESET);

            int choice = Validator.validateInteger(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET,MainApplication.sc);

            switch (choice) {
                case 1:
                    displayListCourse(MainApplication.sc,courseServiceImp);
                    break;
                case 2:
                    searchCourseByName(MainApplication.sc,courseServiceImp);
                    break;
                case 3:
                    registerCourse(MainApplication.sc,courseServiceImp);
                    break;
                case 4:
                    viewRegisteredCourses(MainApplication.sc);
                    break;
                case 5:
                    sortCoursesWithPaging(MainApplication.sc,enrollmentServiceImp,courseServiceImp);
                    break;
                case 6:
                    cancelCourseRegistration(MainApplication.sc,enrollmentServiceImp,courseServiceImp);
                    break;
                case 7 :
                    changePassword(MainApplication.sc);
                    break;
                case 8:
                    System.out.println(Color.GREEN + "Bạn đã đăng xuất!" + Color.RESET);
                    continueProgram = false;
                    AuthUI.login();
                    break;
                case 9:
                    System.out.println(Color.YELLOW + "Cảm ơn bạn đã sử dụng chương trình!" + Color.RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
            }
        } while (continueProgram);
    }

    public static void displayListCourse(Scanner sc, CourseServiceImp courseServiceImp) {
        int currentPage = 1;
        int pageSize = CourseValidator.validatePageSize(sc);
        boolean continueList = true;

        try {
            while (continueList) {
                PageInfo<Course> pageInfo = courseServiceImp.getPageData(currentPage, pageSize);
                List<Course> courses = pageInfo.getRecords();
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.MAGENTA + "\n--- Danh sách khóa học (Trang " + pageInfo.getCurrentPage() + "/" + totalPages + ") --- Tổng " + pageInfo.getTotalRecords() + " khóa học" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.println(Color.RED + "Không có khóa học nào ở trang này." + Color.RESET);
                } else {
                    System.out.println(Color.BLUE + "╔════════╦══════════════════════════════════════╦══════════════╦════════════════════════╦══════════════════════╗" + Color.RESET);
                    System.out.printf(Color.YELLOW + "║ %-6s ║ %-36s ║ %-12s ║ %-22s ║ %-20s ║\n" + Color.RESET,
                            "ID", "Tên khóa học", "Thời gian", "Giảng viên", "Ngày tạo");
                    System.out.println(Color.BLUE + "╠════════╬══════════════════════════════════════╬══════════════╬════════════════════════╬══════════════════════╣" + Color.RESET);

                    for (Course course : courses) {
                        System.out.printf(Color.WHITE + "║ %-6d ║ %-36s ║ %-12s ║ %-22s ║ %-20s ║\n" + Color.RESET,
                                course.getId(), course.getName(), course.getDuration(), course.getInstructor(), course.getCreateAt());
                    }

                    System.out.println(Color.BLUE + "╚════════╩══════════════════════════════════════╩══════════════╩════════════════════════╩══════════════════════╝" + Color.RESET);
                }

                if (totalPages > 1) {
                    System.out.println(Color.MAGENTA + "\nChọn trang (gõ số trang / 0: quay lại menu chính)" + Color.RESET);
                    System.out.print(Color.YELLOW + "Trang: " + Color.RESET);

                    for (int i = 1; i <= totalPages; i++) {
                        if (i == currentPage) {
                            System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                        } else {
                            System.out.print(Color.GRAY + i + " " + Color.RESET);
                        }
                    }

                    if (currentPage < totalPages) {
                        System.out.print(Color.CYAN + " / n: next" + Color.RESET);
                    }
                    if (currentPage > 1) {
                        System.out.print(Color.CYAN + " / p: prev" + Color.RESET);
                    }

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc);

                    switch (choice) {
                        case "n":
                            if (currentPage < totalPages) {
                                currentPage++;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                            }
                            break;
                        case "p":
                            if (currentPage > 1) {
                                currentPage--;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                            }
                            break;
                        case "0":
                            System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                            continueList = false;
                            break;
                        default:
                            try {
                                int page = Integer.parseInt(choice);
                                if (page >= 1 && page <= totalPages) {
                                    currentPage = page;
                                } else {
                                    System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Nhập số trang hoặc n / p / 0." + Color.RESET);
                            }
                            break;
                    }
                } else {
                    System.out.println(Color.MAGENTA + "\nChọn 0 để quay lại menu chính." + Color.RESET);
                    String choice = sc.nextLine().trim().toLowerCase();
                    if (choice.equals("0")) {
                        System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                        continueList = false;
                    } else {
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
            sc.nextLine();
        }
    }

    public static void searchCourseByName(Scanner sc, CourseServiceImp courseServiceImp) {
        String keyword = Validator.validateNonEmptyString(Color.WHITE + "Nhập tên khóa học cần tìm: " + Color.RESET, sc);

        int currentPage = 1;
        int pageSize = CourseValidator.validatePageSize(sc);
        boolean continueSearch = true;

        try {
            while (continueSearch) {
                PageInfo<Course> pageInfo = courseServiceImp.searchByName(keyword, currentPage, pageSize);
                List<Course> courses = pageInfo.getRecords();
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.BLUE + "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                System.out.printf(Color.YELLOW + "║ %-104s ║\n" + Color.RESET,
                        "KẾT QUẢ TÌM KIẾM TỪ KHÓA '" + keyword + "' - Trang " + currentPage + "/" + totalPages +
                                " (Tổng cộng: " + pageInfo.getTotalRecords() + " khóa học)");
                System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦══════════╦═══════════════════════════════╦════════════╣" + Color.RESET);
                System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-8s ║ %-29s ║ %-10s ║\n" + Color.RESET,
                        "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬══════════╬═══════════════════════════════╬════════════╣" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.printf(Color.RED + "║ %-104s ║\n" + Color.RESET, "Không tìm thấy khóa học nào với tên chứa: " + keyword);
                } else {
                    for (Course course : courses) {
                        System.out.printf(Color.WHITE + "║ %-3d ║ %-42s ║ %-8d ║ %-29s ║ %-10s ║\n" + Color.RESET,
                                course.getId(), course.getName(), course.getDuration(),
                                course.getInstructor(), course.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }

                System.out.println(Color.BLUE + "╚═════╩════════════════════════════════════════════╩══════════╩═══════════════════════════════╩════════════╝" + Color.RESET);

                if (totalPages > 1) {
                    System.out.println(Color.MAGENTA + "\nChọn trang (gõ số trang / 0: quay lại menu chính)" + Color.RESET);
                    System.out.print(Color.YELLOW + "Trang: " + Color.RESET);

                    for (int i = 1; i <= totalPages; i++) {
                        if (i == currentPage) {
                            System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                        } else {
                            System.out.print(Color.GRAY + i + " " + Color.RESET);
                        }
                    }

                    if (currentPage < totalPages) {
                        System.out.print(Color.CYAN + " / n: next" + Color.RESET);
                    }
                    if (currentPage > 1) {
                        System.out.print(Color.CYAN + " / p: prev" + Color.RESET);
                    }

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc).toLowerCase();

                    switch (choice) {
                        case "n":
                            if (currentPage < totalPages) {
                                currentPage++;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                            }
                            break;
                        case "p":
                            if (currentPage > 1) {
                                currentPage--;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                            }
                            break;
                        case "0":
                            continueSearch = false;
                            System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                            break;
                        default:
                            try {
                                int page = Integer.parseInt(choice);
                                if (page >= 1 && page <= totalPages) {
                                    currentPage = page;
                                } else {
                                    System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Nhập số trang hoặc n / p / 0." + Color.RESET);
                            }
                            break;
                    }
                } else {
                    System.out.println(Color.MAGENTA + "\nChọn 0 để quay lại menu chính." + Color.RESET);
                    String choice = sc.nextLine().trim().toLowerCase();
                    if (choice.equals("0")) {
                        System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                        continueSearch = false;
                    } else {
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
        }
    }

    public static void registerCourse(Scanner sc, CourseServiceImp courseServiceImp) {
        StudentServiceImp studentServiceImp = new StudentServiceImp();
        System.out.println(Color.MAGENTA + "\n=== ĐĂNG KÝ KHÓA HỌC ===" + Color.RESET);

        List<Course> allCourses = courseServiceImp.getAll();
        if (allCourses.isEmpty()) {
            System.out.println(Color.RED + "Hiện không có khóa học nào để đăng ký." + Color.RESET);
            return;
        }

        System.out.printf(Color.CYAN + "%-5s | %-30s | %-10s | %-25s\n" + Color.RESET,
                "ID", "Tên khóa học", "Số buổi", "Giảng viên");
        System.out.println(Color.YELLOW + "------------------------------------------------------------" + Color.RESET);

        for (Course course : allCourses) {
            System.out.printf(Color.WHITE + "%-5d | %-30s | %-10d | %-25s\n" + Color.RESET,
                    course.getId(), course.getName(), course.getDuration(), course.getInstructor());
        }

        // Nhập ID khóa học muốn đăng ký
        int courseId = Validator.validateInteger(Color.WHITE + "Nhập ID khóa học bạn muốn đăng ký: " + Color.RESET, sc);

        Course selectedCourse = (Course) courseServiceImp.getById(courseId);
        if (selectedCourse == null) {
            System.out.println(Color.RED + "Không tìm thấy khóa học với ID đã nhập." + Color.RESET);
            return;
        }

        Account account = MainApplication.currentUser;
        Student student = studentServiceImp.getById(account.getStudent_id());
        if (student == null) {
            System.out.println("Không tìm thấy thông tin học viên tương ứng với tài khoản này.");
            return;
        }
        int studentId = student.getId();

        boolean success = courseServiceImp.registerCourseForStudent(studentId, courseId);
        if (success) {
            System.out.println(Color.GREEN + "Đăng ký khóa học thành công!" + Color.RESET);
        }
    }

    public static void viewRegisteredCourses(Scanner sc) {
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        Account account = MainApplication.currentUser;

        if (account == null) {
            System.out.println(Color.RED + "Bạn chưa đăng nhập." + Color.RESET);
            return;
        }

        StudentServiceImp studentServiceImp = new StudentServiceImp();
        Student student = studentServiceImp.getById(account.getStudent_id());

        if (student == null) {
            System.out.println(Color.RED + "Không tìm thấy thông tin sinh viên này." + Color.RESET);
            return;
        }

        int studentId = student.getId();
        int currentPage = 1;
        int pageSize = StudentValidator.validatePageSize(sc);
        boolean continuePaging = true;

        try {
            while (continuePaging) {
                PageInfo<Enrollment> pageInfo = studentServiceImp.getPagedEnrollmentsByStudentId(studentId, currentPage, pageSize);
                List<Enrollment> enrollments = pageInfo.getRecords();

                if (enrollments.isEmpty()) {
                    System.out.println(Color.RED + "Bạn chưa đăng ký khóa học nào." + Color.RESET);
                    break;
                }

                for (Enrollment e : enrollments) {
                    Course course = (Course) courseServiceImp.getById(e.getCourseId());
                    e.setCourse(course);
                }

                System.out.println(Color.MAGENTA + "\n=== DANH SÁCH KHÓA HỌC ĐÃ ĐĂNG KÝ (Trang " +
                        pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") ===" + Color.RESET);

                System.out.println(Color.BLUE + "╔═════╦════════════════════════════════════════╦═══════════════╦════════════╗" + Color.RESET);
                System.out.printf(Color.YELLOW + "║ %-3s ║ %-38s ║ %-13s ║ %-10s ║\n" + Color.RESET,
                        "ID", "Tên khóa học", "Ngày đăng ký", "Trạng thái");
                System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════╬═══════════════╬════════════╣" + Color.RESET);

                for (Enrollment enrollment : enrollments) {
                    Course course = enrollment.getCourse();
                    if (course != null) {
                        System.out.printf(Color.CYAN + "║ %-3d ║ %-38s ║ %-13s ║ %-10s ║\n" + Color.RESET,
                                course.getId(),
                                course.getName(),
                                enrollment.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                enrollment.getStatus());
                    }
                }

                System.out.println(Color.BLUE + "╚═════╩════════════════════════════════════════╩═══════════════╩════════════╝" + Color.RESET);

                // Tuỳ chọn điều hướng
                if (pageInfo.getTotalPages() > 1) {
                    System.out.println(Color.MAGENTA + "\nChọn trang (gõ số trang / 0: quay lại menu chính)" + Color.RESET);
                    System.out.print(Color.YELLOW + "Trang: " + Color.RESET);

                    // In ra các trang
                    for (int i = 1; i <= pageInfo.getTotalPages(); i++) {
                        if (i == currentPage) {
                            System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                        } else {
                            System.out.print(Color.GRAY + i + " " + Color.RESET);
                        }
                    }

                    if (currentPage < pageInfo.getTotalPages()) {
                        System.out.print(Color.CYAN + " / n: next" + Color.RESET);
                    }
                    if (currentPage > 1) {
                        System.out.print(Color.CYAN + " / p: prev" + Color.RESET);
                    }

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc);

                    switch (choice) {
                        case "n":
                            if (currentPage < pageInfo.getTotalPages()) {
                                currentPage++;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                            }
                            break;
                        case "p":
                            if (currentPage > 1) {
                                currentPage--;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                            }
                            break;
                        case "0":
                            System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                            continuePaging = false;
                            break;
                        default:
                            try {
                                int page = Integer.parseInt(choice);
                                if (page >= 1 && page <= pageInfo.getTotalPages()) {
                                    currentPage = page;
                                } else {
                                    System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Nhập số trang hoặc n / p / 0." + Color.RESET);
                            }
                            break;
                    }
                } else {
                    System.out.println(Color.MAGENTA + "\nChọn 0 để quay lại menu chính." + Color.RESET);
                    String choice = sc.nextLine().trim().toLowerCase();
                    if (choice.equals("0")) {
                        System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                        continuePaging = false;
                    } else {
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
            sc.nextLine();
        }
    }

    public static void cancelCourseRegistration(Scanner sc, EnrollmentServiceImp enrollmentServiceImp, CourseServiceImp courseServiceImp) {
        System.out.println(Color.MAGENTA + "\n=== HỦY ĐĂNG KÝ KHÓA HỌC ===" + Color.RESET);

        int studentId = MainApplication.currentUser.getStudent_id();
        Student student = new StudentServiceImp().getById(studentId);

        if (student == null) {
            System.out.println(Color.RED + "Không tìm thấy thông tin học viên." + Color.RESET);
            return;
        }

        List<Enrollment> waitingEnrollments = enrollmentServiceImp.getCoursesByStudent(studentId);

        if (waitingEnrollments.isEmpty()) {
            System.out.println(Color.RED + "Không có khóa học nào đang chờ xác nhận để hủy." + Color.RESET);
            return;
        }

        System.out.println(Color.BLUE + "╔═══════╦════════════════════════════════════════╦═══════════════╦═══════════════════════════╗" + Color.RESET);
        System.out.printf(Color.YELLOW + "║ %-5s ║ %-38s ║ %-13s ║ %-25s ║\n" + Color.RESET,
                "ID", "Tên khóa học", "Số buổi", "Giảng viên");
        System.out.println(Color.BLUE + "╠═══════╬════════════════════════════════════════╬═══════════════╬═══════════════════════════╣" + Color.RESET);

        waitingEnrollments.forEach(e -> {
            Course c = (Course) courseServiceImp.getById(e.getCourseId());
            if (c != null) {
                System.out.printf(Color.CYAN + "║ %-5d ║ %-38s ║ %-13d ║ %-25s ║\n" + Color.RESET,
                        c.getId(), c.getName(), c.getDuration(), c.getInstructor());
            }
        });

        System.out.println(Color.BLUE + "╚═══════╩════════════════════════════════════════╩═══════════════╩═══════════════════════════╝" + Color.RESET);
        int courseId;
        while (true) {
            int inputCourseId = Validator.validateInteger(Color.WHITE + "Nhập ID khóa học bạn muốn hủy đăng ký: " + Color.RESET, sc);

            boolean isCourseIdValid = waitingEnrollments.stream()
                    .anyMatch(e -> e.getCourseId() == inputCourseId);

            if (!isCourseIdValid) {
                System.out.println(Color.RED + "ID khóa học không hợp lệ hoặc không nằm trong danh sách đăng ký đang chờ xác nhận. Vui lòng nhập lại!" + Color.RESET);
            } else {
                courseId = inputCourseId;
                break;
            }
        }


        String confirm = Validator.validateNonEmptyString("Bạn có chắc chắn muốn hủy đăng ký khóa học này? (y/n): ", sc);
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println(Color.YELLOW + "Đã hủy thao tác hủy đăng ký." + Color.RESET);
            return;
        }

        if (enrollmentServiceImp.cancelEnrollment(studentId, courseId)) {
            System.out.println(Color.GREEN + "Hủy đăng ký khóa học thành công!" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Có lỗi xảy ra khi hủy đăng ký khóa học." + Color.RESET);
        }

    }

    public static void sortCoursesWithPaging(Scanner sc, EnrollmentServiceImp enrollmentServiceImp, CourseService courseService) {
        String sortColumn = CourseValidator.validateSortColumn(sc);
        String sortOrder = CourseValidator.validateSortOrder(sc);
        int pageSize = CourseValidator.validatePageSize(sc);

        int currentPage = 1;
        boolean continuePaging = true;

        try {
            while (continuePaging) {
                PageInfo<Enrollment> pageInfo = enrollmentServiceImp.getSortedPagedData(sortColumn, sortOrder, currentPage, pageSize);
                List<Enrollment> enrollments = pageInfo.getRecords();

                // Hiển thị thông tin trang
                System.out.println(Color.MAGENTA + "\n--- Danh sách đăng ký (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " bản ghi" + Color.RESET);

                if (enrollments.isEmpty()) {
                    System.out.println(Color.RED + "Không có dữ liệu." + Color.RESET);
                    break;
                } else {
                    System.out.println(Color.CYAN + "╔═══════╦════════════════════╦═══════════════╦═══════════════════════════╦══════════════════════════════╗" + Color.RESET);
                    System.out.println(Color.YELLOW + "║ ID    ║ StudentID          ║ CourseID      ║ RegisteredAt              ║ Course Name                  ║" + Color.RESET);
                    System.out.println(Color.CYAN + "╠═══════╬════════════════════╬═══════════════╬═══════════════════════════╬══════════════════════════════╣" + Color.RESET);

                    for (Enrollment enrollment : enrollments) {
                        String courseName = courseService.getCourseNameById(enrollment.getCourseId());

                        System.out.printf(Color.WHITE + "║ %-5d ║ %-18d ║ %-13d ║ %-25s ║ %-28s ║\n" + Color.RESET,
                                enrollment.getId(),
                                enrollment.getStudentId(),
                                enrollment.getCourseId(),
                                enrollment.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                courseName);
                    }

                    System.out.println(Color.CYAN + "╚═══════╩════════════════════╩═══════════════╩═══════════════════════════╩══════════════════════════════╝" + Color.RESET);
                }

                // Quản lý điều hướng trang
                if (pageInfo.getTotalPages() > 1) {
                    System.out.println(Color.MAGENTA + "\nChọn trang (gõ số trang / 0: quay lại menu chính)" + Color.RESET);
                    System.out.print(Color.YELLOW + "Trang: " + Color.RESET);

                    // Hiển thị các trang
                    for (int i = 1; i <= pageInfo.getTotalPages(); i++) {
                        if (i == currentPage) {
                            System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                        } else {
                            System.out.print(Color.GRAY + i + " " + Color.RESET);
                        }
                    }

                    if (currentPage < pageInfo.getTotalPages()) {
                        System.out.print(Color.CYAN + " / n: next" + Color.RESET);
                    }
                    if (currentPage > 1) {
                        System.out.print(Color.CYAN + " / p: prev" + Color.RESET);
                    }

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc);

                    switch (choice) {
                        case "n":
                            if (currentPage < pageInfo.getTotalPages()) {
                                currentPage++;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                            }
                            break;
                        case "p":
                            if (currentPage > 1) {
                                currentPage--;
                            } else {
                                System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                            }
                            break;
                        case "0":
                            System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                            continuePaging = false;
                            break;
                        default:
                            try {
                                int page = Integer.parseInt(choice);
                                if (page >= 1 && page <= pageInfo.getTotalPages()) {
                                    currentPage = page;
                                } else {
                                    System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Nhập số trang hoặc n / p / 0." + Color.RESET);
                            }
                            break;
                    }
                } else {
                    System.out.println(Color.MAGENTA + "\nChọn 0 để quay lại menu chính." + Color.RESET);
                    String choice = sc.nextLine().trim().toLowerCase();
                    if (choice.equals("0")) {
                        System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                        continuePaging = false;
                    } else {
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Lỗi: " + e.getMessage() + Color.RESET);
        }
    }

    public static void changePassword(Scanner sc) {
        StudentServiceImp studentServiceImp = new StudentServiceImp();
        System.out.println(Color.MAGENTA + "\n=== ĐỔI MẬT KHẨU ===" + Color.RESET);

        Account currentAccount = MainApplication.currentUser;

        String inputEmail;
        while (true) {
            inputEmail = Validator.validateNonEmptyString(Color.WHITE + "Nhập email của bạn để xác nhận: " + Color.RESET,sc);

            if (inputEmail.equalsIgnoreCase(currentAccount.getEmail())) {
                break;
            } else {
                System.out.println(Color.RED + "Email không khớp với tài khoản hiện tại. Vui lòng thử lại." + Color.RESET);
            }
        }

        String oldPassword;
        while (true) {
            oldPassword = Validator.validatePassword(Color.WHITE + "Nhập mật khẩu cũ: " + Color.RESET, sc);
            if (studentServiceImp.checkOldPassword(inputEmail, oldPassword)) {
                break;
            } else {
                System.out.println(Color.RED + "Mật khẩu cũ không đúng. Vui lòng thử lại." + Color.RESET);
            }
        }

        String newPassword;
        while (true) {
            newPassword = Validator.validateNewPassword(Color.WHITE + "Nhập mật khẩu mới: " + Color.RESET, sc);

            if (oldPassword.equals(newPassword)) {
                System.out.println(Color.RED + "Mật khẩu mới không được trùng với mật khẩu cũ." + Color.RESET);
                continue;
            }

            if (!StudentValidator.isStrongPassword(newPassword)) {
                System.out.println(Color.RED + "Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số." + Color.RESET);
                continue;
            }

            break;
        }

        String confirmPassword;
        while (true) {
            confirmPassword = Validator.validatePassword(Color.WHITE + "Xác nhận mật khẩu mới: " + Color.RESET, sc);
            if (newPassword.equals(confirmPassword)) {
                break;
            } else {
                System.out.println(Color.RED + "Mật khẩu xác nhận không khớp. Vui lòng nhập lại." + Color.RESET);
            }
        }

        boolean success = studentServiceImp.changePassword(inputEmail, oldPassword, newPassword);
        if (success) {
            System.out.println(Color.GREEN + "Đổi mật khẩu thành công!" + Color.RESET);
        } else {
            System.out.println(Color.RED + "Đổi mật khẩu thất bại. Vui lòng thử lại." + Color.RESET);
        }
    }

}
