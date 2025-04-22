package ra.edu.presentation.admin;

import ra.edu.MainApplication;
import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.enrollment.Status;
import ra.edu.business.model.student.Student;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.auth.AuthUI;
import ra.edu.utils.Color;
import ra.edu.utils.PageInfo;
import ra.edu.validate.Validator;
import ra.edu.validate.course.CourseValidator;
import ra.edu.validate.student.StudentValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class AdminUI {
    public static void displayMenuAdmin() {
        boolean continueProgram = true;
        do {
            System.out.println(Color.CYAN + "=== ADMIN UI ===" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Quản lý khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Quản lý sinh viên" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Quản lý đăng ký khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Thống kê" + Color.RESET);
            System.out.println(Color.RED + "5. Đăng xuất" + Color.RESET);
            String choice = Validator.validateNonEmptyString(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET,MainApplication.sc);

            switch (choice) {
                case "1":
                    displayMenuCourse();
                    break;
                case "2":
                     displayMenuStudent();
                    break;
                case "3":
                    displayMenuRegisterCourse();
                    break;
                case "4":
                    displayMenuStatistics();
                    break;
                case "5":
                    continueProgram = false;
                    AuthUI.logout();
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn từ 1 - 5." + Color.RESET);
            }
        } while (continueProgram);
    }

    public static void displayMenuCourse() {
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        boolean continueProgram = true;
        do {
            System.out.println(Color.CYAN + "=== ADMIN UI ===" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Hiển thị danh sách khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Thêm mới khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Chỉnh sửa khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Xóa khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "5. Tìm kiếm khóa học theo tên" + Color.RESET);
            System.out.println(Color.YELLOW + "6. Sắp xếp khóa học" + Color.RESET);
            System.out.println(Color.RED + "7. Thoát" + Color.RESET);
            int choice = Validator.validateInteger(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET,MainApplication.sc);

            switch (choice) {
                case 1:
                    displayListCourse(MainApplication.sc, courseServiceImp);
                    break;
                case 2:
                    addCourse(courseServiceImp);
                    break;
                case 3:
                    updateCourse(courseServiceImp);
                    break;
                case 4:
                    deleteCourse(MainApplication.sc, courseServiceImp);
                    break;
                case 5:
                    searchCourseByName(MainApplication.sc, courseServiceImp);
                    break;
                case 6:
                    sortCoursesWithPaging(MainApplication.sc, courseServiceImp);
                    break;
                case 7:
                    System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                    continueProgram = false;
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn từ 1 - 7." + Color.RESET);
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

                System.out.println(Color.MAGENTA + "\n--- Danh sách khóa học (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " khóa học" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.println(Color.RED + "Không có khóa học nào ở trang này." + Color.RESET);
                } else {
                    System.out.printf(Color.CYAN + "%-5s | %-30s | %-10s | %-25s | %-12s\n" + Color.RESET,
                            "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                    System.out.println(Color.YELLOW + "----------------------------------------------------------------------" + Color.RESET);

                    for (Course course : courses) {
                        System.out.printf(Color.WHITE + "%-5d | %-30s | %-10d | %-25s | %-12s\n" + Color.RESET,
                                course.getId(), course.getName(), course.getDuration(),
                                course.getInstructor(), course.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }

                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                int choiceInput = Validator.validateInteger(Color.MAGENTA + "Lựa chọn: " + Color.RESET,sc);
                switch (choiceInput) {
                    case 1:
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case 2:
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case 3:
                        System.out.print(Color.WHITE + "Nhập số trang (1 - " + pageInfo.getTotalPages() + "): " + Color.RESET);
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case 4:
                        continueList = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
            sc.nextLine();
        }
    }

    public static void addCourse(CourseServiceImp courseServiceImp) {
        // Lấy danh sách khóa học hiện tại từ service
        List<Course> courseList = courseServiceImp.getAll();

        // Tạo một đối tượng khóa học mới
        Course newCourse = new Course();

        // Nhập dữ liệu cho khóa học, truyền vào courseList để kiểm tra tên trùng
        newCourse.inputData(MainApplication.sc, courseList);

        // Lưu khóa học và kiểm tra kết quả
        boolean isAdded = courseServiceImp.save(newCourse);
        if (isAdded) {
            System.out.println(Color.GREEN + "Thêm khóa học thành công." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Thêm khóa học thất bại." + Color.RESET);
        }
    }

    public static void updateCourse(CourseServiceImp courseServiceImp) {
        int updateId = Validator.validateInteger(Color.WHITE + "Nhập ID khóa học muốn chỉnh sửa: " + Color.RESET, MainApplication.sc);

        Course existingCourse = (Course) courseServiceImp.getById(updateId);

        if (existingCourse == null) {
            System.out.println(Color.RED + "Không tìm thấy khóa học với ID này." + Color.RESET);
            return;
        }

        System.out.println(Color.MAGENTA + "\n--- Thông tin hiện tại ---" + Color.RESET);
        System.out.println(Color.WHITE + "Tên khóa học: " + existingCourse.getName() + Color.RESET);
        System.out.println(Color.WHITE + "Thời lượng: " + existingCourse.getDuration() + " giờ" + Color.RESET);
        System.out.println(Color.WHITE + "Giảng viên: " + existingCourse.getInstructor() + Color.RESET);

        // Lấy danh sách khóa học hiện tại từ service
        List<Course> courseList = courseServiceImp.getAll();

        Course updatedCourse = new Course();
        updatedCourse.setId(updateId);
        updatedCourse.setCreateAt(existingCourse.getCreateAt());

        boolean updated = false;
        boolean exit = false;

        while (!exit) {
            System.out.println(Color.MAGENTA + "\nChọn thông tin cần cập nhật:" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Tên khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Thời lượng khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Giảng viên" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Cập nhật tất cả" + Color.RESET);
            System.out.println(Color.YELLOW + "0. Lưu và thoát" + Color.RESET);

            int choice = Validator.validateInteger(Color.WHITE + "Lựa chọn của bạn: " + Color.RESET, MainApplication.sc);

            switch (choice) {
                case 1:
                    // Kiểm tra trùng tên khóa học khi cập nhật
                    updatedCourse.setName(CourseValidator.validateCourseName(MainApplication.sc, courseList));
                    updated = true;
                    break;
                case 2:
                    updatedCourse.setDuration(CourseValidator.validateDuration(MainApplication.sc));
                    updated = true;
                    break;
                case 3:
                    updatedCourse.setInstructor(CourseValidator.validateInstructor(MainApplication.sc));
                    updated = true;
                    break;
                case 4:
                    // Kiểm tra trùng tên khóa học khi cập nhật tất cả thông tin
                    updatedCourse.setName(CourseValidator.validateCourseName(MainApplication.sc, courseList));
                    updatedCourse.setDuration(CourseValidator.validateDuration(MainApplication.sc));
                    updatedCourse.setInstructor(CourseValidator.validateInstructor(MainApplication.sc));
                    updated = true;
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
            }
        }

        // Nếu người dùng không cập nhật trường nào thì giữ nguyên giá trị cũ
        if (updatedCourse.getName() == null) {
            updatedCourse.setName(existingCourse.getName());
        }
        if (updatedCourse.getDuration() == 0) {
            updatedCourse.setDuration(existingCourse.getDuration());
        }
        if (updatedCourse.getInstructor() == null) {
            updatedCourse.setInstructor(existingCourse.getInstructor());
        }

        if (updated) {
            boolean isUpdated = courseServiceImp.update(updatedCourse);
            if (isUpdated) {
                System.out.println(Color.GREEN + "Cập nhật thành công." + Color.RESET);
            } else {
                System.out.println(Color.RED + "Cập nhật thất bại." + Color.RESET);
            }
        } else {
            System.out.println(Color.WHITE + "Không có thay đổi nào được thực hiện." + Color.RESET);
        }
    }

    public static void deleteCourse(Scanner sc, CourseServiceImp courseServiceImp) {
        int deleteId = Validator.validateInteger(Color.WHITE + "Nhập ID khóa học muốn xóa: " + Color.RESET,sc);
        boolean isDeleted = courseServiceImp.delete(deleteId);
        if (isDeleted) {
            System.out.println(Color.GREEN + "Xóa khóa học thành công." + Color.RESET);
        }
    }

    public static void searchCourseByName(Scanner sc, CourseServiceImp courseServiceImp) {
        String keyword = Validator.validateNonEmptyString(Color.WHITE + "Nhập tên khóa học cần tìm: " + Color.RESET,sc);

        int currentPage = 1;
        int pageSize = 5;
        boolean continueSearch = true;

        try {
            while (continueSearch) {
                PageInfo<Course> pageInfo = courseServiceImp.searchByName(keyword, currentPage, pageSize);
                List<Course> courses = pageInfo.getRecords();

                System.out.println(Color.MAGENTA + "\n--- Kết quả tìm kiếm (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " khóa học" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.println(Color.RED + "Không tìm thấy khóa học nào với tên chứa: " + keyword + Color.RESET);
                } else {
                    System.out.printf(Color.CYAN + "%-5s | %-30s | %-10s | %-25s | %-12s\n" + Color.RESET,
                            "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                    System.out.println(Color.YELLOW + "----------------------------------------------------------------------" + Color.RESET);

                    for (Course course : courses) {
                        System.out.printf(Color.WHITE + "%-5d | %-30s | %-10d | %-25s | %-12s\n" + Color.RESET,
                                course.getId(), course.getName(), course.getDuration(),
                                course.getInstructor(), course.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }

                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                System.out.print(Color.MAGENTA + "Lựa chọn: " + Color.RESET);

                String choiceInput = sc.nextLine();
                switch (choiceInput) {
                    case "1":
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case "2":
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case "3":
                        System.out.print(Color.WHITE + "Nhập số trang (1 - " + pageInfo.getTotalPages() + "): " + Color.RESET);
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case "4":
                        continueSearch = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
        }
    }

    public static void sortCoursesWithPaging(Scanner sc, CourseServiceImp courseServiceImp) {
        String sortColumn = CourseValidator.validateSortColumn(sc);
        String sortOrder = CourseValidator.validateSortOrder(sc);
        int pageSize = CourseValidator.validatePageSize(sc);

        int currentPage = 1;
        boolean continuePaging = true;

        try {
            while (continuePaging) {
                PageInfo<Course> pageInfo = courseServiceImp.getSortedPagedData(sortColumn, sortOrder, currentPage, pageSize);
                List<Course> courses = pageInfo.getRecords();

                System.out.println(Color.MAGENTA + "\n--- Danh sách sắp xếp (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " khóa học" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.println(Color.RED + "Không có khóa học nào." + Color.RESET);
                } else {
                    System.out.printf(Color.CYAN + "%-5s | %-20s | %-10s | %-15s | %-12s\n" + Color.RESET,
                            "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                    System.out.println(Color.YELLOW + "----------------------------------------------------------------------" + Color.RESET);

                    for (Course course : courses) {
                        System.out.printf(Color.WHITE + "%-5d | %-20s | %-10d | %-15s | %-12s\n" + Color.RESET,
                                course.getId(), course.getName(), course.getDuration(),
                                course.getInstructor(), course.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }

                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                System.out.print(Color.MAGENTA + "Lựa chọn: " + Color.RESET);

                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case "2":
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case "3":
                        System.out.print(Color.WHITE + "Nhập số trang (1 - " + pageInfo.getTotalPages() + "): " + Color.RESET);
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case "4":
                        continuePaging = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
        }
    }

    public static void displayMenuStudent() {
        StudentServiceImp studentServiceImp = new StudentServiceImp();
        boolean continueProgram = true;
        do {
            System.out.println(Color.MAGENTA + "=== ADMIN UI ===" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Hiển thị danh sách sinh viên" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Thêm mới sinh viên" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Chỉnh sửa sinh viên" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Xóa sinh viên theo id" + Color.RESET);
            System.out.println(Color.YELLOW + "5. Tìm kiếm sinh viên theo tên,email,mã id" + Color.RESET);
            System.out.println(Color.YELLOW + "6. Sắp xếp sinh viên" + Color.RESET);
            System.out.println(Color.RED + "7. Thoát" + Color.RESET);
            int choice = Validator.validateInteger(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET,MainApplication.sc);

            switch (choice) {
                case 1:
                    displayListStudent(MainApplication.sc,studentServiceImp);
                    break;
                case 2:
                    addStudent(studentServiceImp);
                    break;
                case 3:
                    updateStudent(studentServiceImp);
                    break;
                case 4:
                    deleteStudent(MainApplication.sc,studentServiceImp);
                    break;
                case 5:
                    searchStudentByKeyword(MainApplication.sc,studentServiceImp);
                    break;
                case 6:
                    sortStudentsWithPaging(MainApplication.sc,studentServiceImp);
                    break;
                case 7:
                    continueProgram = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (continueProgram);
    }

    public static void displayListStudent(Scanner sc, StudentServiceImp studentServiceImp) {
        int currentPage = 1;
        int pageSize = 5;
        boolean continueList = true;

        try {
            while (continueList) {
                PageInfo<Student> pageInfo = studentServiceImp.getPageData(currentPage, pageSize);

                List<Student> students = pageInfo.getRecords();

                System.out.println(Color.MAGENTA + "\n--- Danh sách sinh viên (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " sinh viên" + Color.RESET);

                if (students.isEmpty()) {
                    System.out.println(Color.RED + "Không có sinh viên nào ở trang này." + Color.RESET);
                } else {
                    System.out.printf(Color.CYAN + "%-5s | %-20s | %-12s | %-18s | %-25s | %-7s | %-12s\n" + Color.RESET,
                            "ID", "Tên sinh viên", "Ngày sinh", "Email", "Số điện thoại", "Giới tính", "Ngày tạo");
                    System.out.println(Color.YELLOW + "-----------------------------------------------------------------------------------" + Color.RESET);

                    for (Student student : students) {
                        String gender = student.isStatus() ? "Nam" : "Nữ";
                        System.out.printf(Color.WHITE + "%-5d | %-20s | %-12s | %-18s | %-25s | %-7s | %-12s\n" + Color.RESET,
                                student.getId(), student.getName(), student.getBirthday(), student.getEmail(),
                                student.getPhone(), gender, student.getCreated_at());
                    }
                }

                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                int choiceInput = Validator.validateInteger(Color.MAGENTA + "Lựa chọn: " + Color.RESET, sc);
                switch (choiceInput) {
                    case 1:
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case 2:
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case 3:
                        System.out.print(Color.WHITE + "Nhập số trang (1 - " + pageInfo.getTotalPages() + "): " + Color.RESET);
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case 4:
                        continueList = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
            sc.nextLine();
        }
    }

    public static void addStudent(StudentServiceImp studentServiceImp) {
        List<Student> studentList = studentServiceImp.getAll();

        // Tạo một đối tượng khóa học mới
        Student newStudent = new Student();

        // Nhập dữ liệu cho khóa học, truyền vào courseList để kiểm tra tên trùng
        newStudent.inputData(MainApplication.sc, studentList);

        // Lưu khóa học và kiểm tra kết quả
        boolean isAdded = studentServiceImp.save(newStudent);
        if (isAdded) {
            System.out.println(Color.GREEN + "Thêm sinh viên thành công." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Thêm sinh viên thất bại." + Color.RESET);
        }
    }

    public static void updateStudent(StudentServiceImp studentServiceImp) {
        int updateId = Validator.validateInteger(Color.WHITE + "Nhập ID học viên muốn chỉnh sửa: " + Color.RESET, MainApplication.sc);

        Student existingStudent = studentServiceImp.getById(updateId);
        if (existingStudent == null) {
            System.out.println(Color.RED + "Không tìm thấy học viên với ID này." + Color.RESET);
            return;
        }

        System.out.println(Color.MAGENTA + "\n--- Thông tin hiện tại ---" + Color.RESET);
        System.out.println(Color.WHITE + "ID          : " + existingStudent.getId() + Color.RESET);
        System.out.println(Color.WHITE + "Họ tên       : " + existingStudent.getName() + Color.RESET);
        System.out.println(Color.WHITE + "Ngày sinh    : " + existingStudent.getBirthday() + Color.RESET);
        System.out.println(Color.WHITE + "Email        : " + existingStudent.getEmail() + Color.RESET);
        System.out.println(Color.WHITE + "Trạng thái   : " + (existingStudent.isStatus() ? "Đang hoạt động" : "Không hoạt động") + Color.RESET);
        System.out.println(Color.WHITE + "SĐT          : " + existingStudent.getPhone() + Color.RESET);
        System.out.println(Color.WHITE + "Ngày tạo     : " + existingStudent.getCreated_at() + Color.RESET);

        List<Student> studentList = studentServiceImp.getAll();

        Student updatedStudent = new Student();
        updatedStudent.setId(updateId);
        updatedStudent.setCreated_at(existingStudent.getCreated_at());
        updatedStudent.setName(existingStudent.getName());
        updatedStudent.setBirthday(existingStudent.getBirthday());
        updatedStudent.setEmail(existingStudent.getEmail());
        updatedStudent.setPhone(existingStudent.getPhone());
        updatedStudent.setStatus(existingStudent.isStatus());

        boolean updated = false;
        boolean exit = false;

        while (!exit) {
            System.out.println(Color.MAGENTA + "\nChọn thông tin cần cập nhật:" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Họ tên" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Ngày sinh" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Email" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Trạng thái" + Color.RESET);
            System.out.println(Color.YELLOW + "5. Số điện thoại" + Color.RESET);
            System.out.println(Color.YELLOW + "6. Cập nhật tất cả" + Color.RESET);
            System.out.println(Color.YELLOW + "0. Lưu và thoát" + Color.RESET);

            int choice = Validator.validateInteger(Color.WHITE + "Lựa chọn của bạn: " + Color.RESET, MainApplication.sc);

            switch (choice) {
                case 1:
                    updatedStudent.setName(StudentValidator.validateName("Nhập họ tên mới: ", MainApplication.sc));
                    updated = true;
                    break;
                case 2:
                    updatedStudent.setBirthday(StudentValidator.validateDob("Nhập ngày sinh mới", MainApplication.sc));
                    updated = true;
                    break;
                case 3:
                    updatedStudent.setEmail(StudentValidator.validateEmail("Nhập email mới: ", MainApplication.sc, studentList));
                    updated = true;
                    break;
                case 4:
                    updatedStudent.setStatus(StudentValidator.validateSex("Nhập giới tính mới", MainApplication.sc));
                    updated = true;
                    break;
                case 5:
                    updatedStudent.setPhone(StudentValidator.validatePhone("Nhập số điện thoại mới", MainApplication.sc,studentList));
                    updated = true;
                    break;
                case 6:
                    updatedStudent.setName(StudentValidator.validateName("Nhập họ tên mới: ", MainApplication.sc));
                    updatedStudent.setBirthday(StudentValidator.validateDob("Nhập ngày sinh mới", MainApplication.sc));
                    updatedStudent.setEmail(StudentValidator.validateEmail("Nhập email mới: ", MainApplication.sc, studentList));
                    updatedStudent.setStatus(StudentValidator.validateSex("Nhập giới tính mới", MainApplication.sc));
                    updatedStudent.setPhone(StudentValidator.validatePhone("Nhập số điện thoại mới", MainApplication.sc,studentList));
                    updated = true;
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng chọn lại." + Color.RESET);
            }
        }

        if (updated) {
            boolean isUpdated = studentServiceImp.update(updatedStudent);
            if (isUpdated) {
                System.out.println(Color.GREEN + "Cập nhật học viên thành công." + Color.RESET);
            } else {
                System.out.println(Color.RED + "Cập nhật thất bại." + Color.RESET);
            }
        } else {
            System.out.println(Color.WHITE + "Không có thay đổi nào được thực hiện." + Color.RESET);
        }
    }

    public static void deleteStudent(Scanner sc, StudentServiceImp studentServiceImp) {
        int deleteId = Validator.validateInteger(Color.WHITE + "Nhập ID sinh viên muốn xóa: " + Color.RESET,sc);
        boolean isDeleted = studentServiceImp.delete(deleteId);
        if (isDeleted) {
            System.out.println(Color.GREEN + "Xóa sinh viên thành công." + Color.RESET);
        }
    }

    public static void searchStudentByKeyword(Scanner sc, StudentServiceImp studentServiceImp) {
        String keyword = Validator.validateNonEmptyString(Color.WHITE + "Nhập tên, email hoặc mã học viên cần tìm: " + Color.RESET, sc);

        int currentPage = 1;
        int pageSize = 5;
        boolean continueSearch = true;

        try {
            while (continueSearch) {
                PageInfo<Student> pageInfo = studentServiceImp.searchByKeyword(keyword, currentPage, pageSize);
                List<Student> students = pageInfo.getRecords();

                System.out.println(Color.MAGENTA + "\n--- Kết quả tìm kiếm học viên (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " học viên" + Color.RESET);

                if (students.isEmpty()) {
                    System.out.println(Color.RED + "Không tìm thấy học viên nào với từ khóa: " + keyword + Color.RESET);
                } else {
                    System.out.printf(Color.CYAN + "%-5s | %-25s | %-25s | %-15s\n" + Color.RESET,
                            "ID", "Họ tên", "Email", "Ngày tạo");
                    System.out.println(Color.YELLOW + "--------------------------------------------------------------------" + Color.RESET);

                    for (Student student : students) {
                        System.out.printf(Color.WHITE + "%-5d | %-25s | %-25s | %-15s\n" + Color.RESET,
                                student.getId(), student.getName(), student.getEmail(),
                                student.getCreated_at());
                    }
                }

                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                System.out.print(Color.MAGENTA + "Lựa chọn: " + Color.RESET);

                String choiceInput = sc.nextLine();
                switch (choiceInput) {
                    case "1":
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case "2":
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case "3":
                        System.out.print(Color.WHITE + "Nhập số trang (1 - " + pageInfo.getTotalPages() + "): " + Color.RESET);
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case "4":
                        continueSearch = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
        }
    }

    public static void sortStudentsWithPaging(Scanner sc, StudentServiceImp studentServiceImp) {
        String sortColumn = StudentValidator.validateSortColumn(sc);
        String sortOrder = StudentValidator.validateSortOrder(sc);
        int pageSize = StudentValidator.validatePageSize(sc);

        int currentPage = 1;
        boolean continuePaging = true;

        try {
            while (continuePaging) {
                PageInfo<Student> pageInfo = studentServiceImp.getSortedPagedData(sortColumn, sortOrder, currentPage, pageSize);
                List<Student> students = pageInfo.getRecords();

                System.out.println(Color.MAGENTA + "\n--- Danh sách sắp xếp (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " sinh viên" + Color.RESET);

                if (students.isEmpty()) {
                    System.out.println(Color.RED + "Không có sinh viên nào." + Color.RESET);
                } else {
                    // Điều chỉnh tiêu đề cột cho phù hợp với bảng sinh viên
                    System.out.printf(Color.CYAN + "%-5s | %-20s | %-12s | %-20s | %-15s | %-15s | %-10s\n" + Color.RESET,
                            "ID", "Tên sinh viên", "Ngày sinh", "Email", "Số điện thoại", "Giới tính", "Ngày tạo");
                    System.out.println(Color.YELLOW + "----------------------------------------------------------------------" + Color.RESET);

                    // Hiển thị thông tin sinh viên
                    for (Student student : students) {
                        System.out.printf(Color.WHITE + "%-5d | %-20s | %-12s | %-20s | %-15s | %-15s | %-10s\n" + Color.RESET,
                                student.getId(), student.getName(), student.getBirthday(),
                                student.getEmail(), student.getPhone(),student.isStatus(),student.getCreated_at());
                    }
                }

                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                System.out.print(Color.MAGENTA + "Lựa chọn: " + Color.RESET);

                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case "2":
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case "3":
                        System.out.print(Color.WHITE + "Nhập số trang (1 - " + pageInfo.getTotalPages() + "): " + Color.RESET);
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case "4":
                        continuePaging = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
        }
    }

    public static void displayMenuRegisterCourse() {
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        EnrollmentServiceImp enrollmentServiceImp = new EnrollmentServiceImp();
        boolean continueProgram = true;
        do {
            System.out.println(Color.MAGENTA + "=== ADMIN UI ===" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Hiển thị danh sách đăng ký khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Duyệt sinh viên đăng ký khóa học" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Xóa sinh viên khỏi khóa học" + Color.RESET);
            System.out.println(Color.RED + "4. Thoát" + Color.RESET);
            System.out.print("Chọn tùy chọn: ");
            int choice = MainApplication.sc.nextInt();
            MainApplication.sc.nextLine();

            switch (choice) {
                case 1:
                    showStudentsByCoursePaginated(MainApplication.sc, courseServiceImp, enrollmentServiceImp);
                    break;
                case 2:
                    approveStudentEnrollment(MainApplication.sc, enrollmentServiceImp);
                    break;
                case 3:
                    rejectStudentEnrollment(MainApplication.sc, enrollmentServiceImp);
                    break;
                case 4:
                    continueProgram = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (continueProgram);
    }

    public static void showStudentsByCoursePaginated(Scanner sc, CourseServiceImp courseServiceImp, EnrollmentServiceImp enrollmentServiceImp) {
        System.out.println(Color.MAGENTA + "\n=== DANH SÁCH SINH VIÊN ĐĂNG KÝ THEO KHÓA HỌC ===" + Color.RESET);

        List<Course> courses = courseServiceImp.getAll();
        if (courses.isEmpty()) {
            System.out.println(Color.RED + "Không có khóa học nào." + Color.RESET);
            return;
        }

        System.out.printf(Color.CYAN + "%-5s | %-30s\n" + Color.RESET, "ID", "Tên khóa học");
        for (Course c : courses) {
            System.out.printf("%-5d | %-30s\n", c.getId(), c.getName());
        }

        int courseId = Validator.validateInteger("Nhập ID khóa học: ", sc);
        int currentPage = 1;
        final int pageSize = 5;
        boolean continuePaging = true;

        try {
            while (continuePaging) {
                PageInfo<Enrollment> pageInfo = enrollmentServiceImp.getStudentsByCoursePaginated(courseId, currentPage, pageSize);
                List<Enrollment> enrollments = pageInfo.getRecords();

                System.out.printf(Color.MAGENTA + "\n--- Trang %d/%d --- Tổng cộng %d sinh viên đăng ký ---\n" + Color.RESET,
                        pageInfo.getCurrentPage(), pageInfo.getTotalPages(), pageInfo.getTotalRecords());

                if (enrollments.isEmpty()) {
                    System.out.println(Color.RED + "Không có sinh viên nào ở trang này." + Color.RESET);
                } else {
                    System.out.printf(Color.CYAN + "%-10s | %-25s | %-20s | %-15s\n" + Color.RESET,
                            "Mã SV", "Tên sinh viên", "Ngày đăng ký", "Trạng thái");

                    for (Enrollment e : enrollments) {
                        System.out.printf("%-10d | %-25s | %-20s | %-15s\n",
                                e.getStudentId(),
                                e.getStudentName(),
                                e.getRegisteredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                                e.getStatus());
                    }
                }

                // Menu điều hướng
                System.out.println(Color.MAGENTA + "\n== Tùy chọn điều hướng ==" + Color.RESET);
                System.out.println(Color.YELLOW + "1. Trang tiếp" + Color.RESET);
                System.out.println(Color.YELLOW + "2. Trang trước" + Color.RESET);
                System.out.println(Color.YELLOW + "3. Đến trang cụ thể" + Color.RESET);
                System.out.println(Color.YELLOW + "4. Quay lại menu chính" + Color.RESET);
                int choice = Validator.validateInteger(Color.MAGENTA + "Lựa chọn: " + Color.RESET, sc);

                switch (choice) {
                    case 1:
                        if (currentPage < pageInfo.getTotalPages()) {
                            currentPage++;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                        }
                        break;
                    case 2:
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                        }
                        break;
                    case 3:
                        System.out.print("Nhập số trang (1 - " + pageInfo.getTotalPages() + "): ");
                        try {
                            int targetPage = Integer.parseInt(sc.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Vui lòng nhập số nguyên." + Color.RESET);
                        }
                        break;
                    case 4:
                        continuePaging = false;
                        System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                        break;
                    default:
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Color.RED + "Đã xảy ra lỗi: " + e.getMessage() + Color.RESET);
            sc.nextLine();
        }
    }

    public static void approveStudentEnrollment(Scanner sc, EnrollmentServiceImp enrollmentServiceImp) {
        System.out.println(Color.MAGENTA + "\n=== DUYỆT SINH VIÊN ĐĂNG KÝ KHÓA HỌC ===" + Color.RESET);

        List<Enrollment> waitingList = enrollmentServiceImp.getEnrollmentsByStatus("waiting");

        if (waitingList.isEmpty()) {
            System.out.println(Color.RED + "Không có đơn đăng ký nào đang chờ duyệt." + Color.RESET);
            return;
        }

        // In thông tin các đơn đăng ký
        System.out.printf(Color.CYAN + "%-10s | %-25s | %-25s | %-20s | %-15s\n" + Color.RESET,
                "Enroll ID", "Tên sinh viên", "Tên khóa học", "Ngày đăng ký", "Trạng thái");
        for (Enrollment e : waitingList) {
            System.out.printf("%-10d | %-25s | %-25s | %-20s | %-15s\n",
                    e.getId(),
                    e.getStudentName(),
                    e.getCourseName(),
                    e.getRegisteredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    e.getStatus());
        }

        int enrollmentId = Validator.validateInteger("Nhập ID đăng ký (Enrollment ID) cần duyệt: ", sc);

        // Kiểm tra xem ID có tồn tại trong danh sách không
        Enrollment enrollmentToApprove = null;
        for (Enrollment e : waitingList) {
            if (e.getId() == enrollmentId) {
                enrollmentToApprove = e;
                break;
            }
        }

        if (enrollmentToApprove == null) {
            System.out.println(Color.RED + "ID đăng ký không hợp lệ hoặc không tồn tại." + Color.RESET);
            return;
        }

        // Duyệt đơn đăng ký
        boolean success = enrollmentServiceImp.approveEnrollment(enrollmentId);

        if (success) {
            System.out.println(Color.GREEN + "Duyệt thành công! Trạng thái đã chuyển sang 'confirm'." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Duyệt thất bại! Chỉ có thể duyệt các đơn có trạng thái 'waiting'." + Color.RESET);
        }
    }


    public static void rejectStudentEnrollment(Scanner sc, EnrollmentServiceImp enrollmentServiceImp) {
        System.out.println(Color.MAGENTA + "\n=== TỪ CHỐI SINH VIÊN ĐĂNG KÝ KHÓA HỌC ===" + Color.RESET);

        List<Enrollment> waitingList = enrollmentServiceImp.getEnrollmentsByStatus("waiting");

        if (waitingList.isEmpty()) {
            System.out.println(Color.RED + "Không có đơn đăng ký nào đang chờ duyệt." + Color.RESET);
            return;
        }

        // In thông tin các đơn đăng ký
        System.out.printf(Color.CYAN + "%-10s | %-25s | %-25s | %-20s | %-15s\n" + Color.RESET,
                "Enroll ID", "Tên sinh viên", "Tên khóa học", "Ngày đăng ký", "Trạng thái");
        for (Enrollment e : waitingList) {
            System.out.printf("%-10d | %-25s | %-25s | %-20s | %-15s\n",
                    e.getId(),
                    e.getStudentName(),
                    e.getCourseName(),
                    e.getRegisteredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    e.getStatus());
        }

        int enrollmentId = Validator.validateInteger("Nhập ID đăng ký (Enrollment ID) cần từ chối: ", sc);

        // Kiểm tra xem ID có tồn tại trong danh sách không
        Enrollment enrollmentToReject = null;
        for (Enrollment e : waitingList) {
            if (e.getId() == enrollmentId) {
                enrollmentToReject = e;
                break;
            }
        }

        if (enrollmentToReject == null) {
            System.out.println(Color.RED + "ID đăng ký không hợp lệ hoặc không tồn tại." + Color.RESET);
            return;
        }

        // Từ chối đơn đăng ký
        boolean success = enrollmentServiceImp.denyEnrollment(enrollmentId);

        if (success) {
            System.out.println(Color.GREEN + "Từ chối thành công! Trạng thái đã chuyển sang 'denied'." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Từ chối thất bại! Chỉ có thể từ chối các đơn có trạng thái 'waiting'." + Color.RESET);
        }
    }


    public static void displayMenuStatistics() {
        boolean continueProgram = true;
        do {
            System.out.println(Color.MAGENTA + "=== ADMIN UI ===" + Color.RESET);
            System.out.println(Color.YELLOW + "1. Thống kê tổng số lượng khóa học và tổng số học viên" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Thống kê tổng số học viên theo từng khóa" + Color.RESET);
            System.out.println(Color.YELLOW + "3. Thống kê top 5 khóa học đông sinh viên nhất" + Color.RESET);
            System.out.println(Color.YELLOW + "4. Liệt kê các khóa học có trên 10 học viên" + Color.RESET);
            System.out.println(Color.RED + "7. Thoát" + Color.RESET);
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
                    continueProgram = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (continueProgram);
    }
}
