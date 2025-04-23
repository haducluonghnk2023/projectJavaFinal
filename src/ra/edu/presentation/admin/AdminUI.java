package ra.edu.presentation.admin;

import ra.edu.MainApplication;
import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.student.Student;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.statistic.StatisticServiceImp;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.presentation.auth.AuthUI;
import ra.edu.utils.Color;
import ra.edu.utils.PageInfo;
import ra.edu.validate.Validator;
import ra.edu.validate.course.CourseValidator;
import ra.edu.validate.student.StudentValidator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class AdminUI {
    public static void displayMenuAdmin() {
        boolean continueProgram = true;
        do {
            System.out.println(Color.CYAN + "============================= ADMIN UI ==============================" + Color.RESET);
            System.out.println(Color.CYAN + "╔════════════╦══════════════════════════════════════════════════════╗");
            System.out.printf("║ %-10s ║ %-52s ║\n", "Lựa chọn", "Mô tả");
            System.out.println("╠════════════╬══════════════════════════════════════════════════════╣");

            System.out.printf("║ %-10s ║ %-52s ║\n", "1", "Quản lý khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "2", "Quản lý sinh viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "3", "Quản lý đăng ký khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "4", "Thống kê");
            System.out.printf("║ %-10s ║ %-52s ║\n", "5", "Đăng xuất");

            System.out.println("╚════════════╩══════════════════════════════════════════════════════╝" + Color.RESET);

            int choice = Validator.validateInteger(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET, MainApplication.sc);

            switch (choice) {
                case 1:
                    displayMenuCourse();
                    break;
                case 2:
                    displayMenuStudent();
                    break;
                case 3:
                    displayMenuRegisterCourse();
                    break;
                case 4:
                    displayMenuStatistics();
                    break;
                case 5:
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
            System.out.println(Color.CYAN + "============================= ADMIN UI ==============================" + Color.RESET);
            System.out.println(Color.CYAN + "╔════════════╦══════════════════════════════════════════════════════╗");
            System.out.printf("║ %-10s ║ %-52s ║\n", "Lựa chọn", "Mô tả");
            System.out.println("╠════════════╬══════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s ║ %-52s ║\n", "1", "Hiển thị danh sách khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "2", "Thêm mới khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "3", "Chỉnh sửa khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "4", "Xóa khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "5", "Tìm kiếm khóa học theo tên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "6", "Sắp xếp khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "7", "Thoát");
            System.out.println("╚════════════╩══════════════════════════════════════════════════════╝" + Color.RESET);

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
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.BLUE + "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                System.out.printf(Color.YELLOW + "║ %-104s ║\n" + Color.RESET,
                        "DANH SÁCH KHÓA HỌC ĐÃ SẮP XẾP - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                " (Tổng cộng: " + pageInfo.getTotalRecords() + " khóa học)");
                System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦══════════╦═══════════════════════════════╦════════════╣" + Color.RESET);
                System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-8s ║ %-29s ║ %-10s ║\n" + Color.RESET,
                        "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬══════════╬═══════════════════════════════╬════════════╣" + Color.RESET);

                // Hiển thị danh sách khóa học đã sắp xếp
                if (courses.isEmpty()) {
                    System.out.printf(Color.RED + "║ %-94s ║\n" + Color.RESET, "Không có khóa học nào.");
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
        System.out.println(Color.WHITE + "Thời lượng: " + existingCourse.getDuration() + " buổi" + Color.RESET);
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
                    updatedCourse.setName(CourseValidator.validateCourseNameUpdate(MainApplication.sc, courseList, existingCourse.getName()));
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
                    updatedCourse.setName(CourseValidator.validateCourseNameUpdate(MainApplication.sc, courseList, existingCourse.getName()));
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
                        "KẾT QUẢ TÌM KIẾM - Tên khóa học: " + keyword + " - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                " (Tổng cộng: " + pageInfo.getTotalRecords() + " khóa học)");
                System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦══════════╦═══════════════════════════════╦════════════╣" + Color.RESET);
                System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-8s ║ %-29s ║ %-10s ║\n" + Color.RESET,
                        "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬══════════╬═══════════════════════════════╬════════════╣" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.printf(Color.RED + "║ %-94s ║\n" + Color.RESET, "Không tìm thấy khóa học nào với tên chứa: " + keyword);
                } else {
                    for (Course course : courses) {
                        System.out.printf(Color.WHITE + "║ %-3s ║ %-42s ║ %-8s ║ %-29s ║ %-10s ║\n" + Color.RESET,
                                course.getId(),
                                course.getName(),
                                course.getDuration(),
                                course.getInstructor(),
                                course.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                }

                System.out.println(Color.BLUE + "╚═════╩════════════════════════════════════════════╩══════════╩═══════════════════════════════╩════════════╝" + Color.RESET);

                if (totalPages > 1) {
                    System.out.println(Color.MAGENTA + "\nChọn trang (gõ số trang / n: next / p: prev / 0: quay lại menu chính)" + Color.RESET);
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
                            continueSearch = false;
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
                        System.out.println(Color.YELLOW + "⬅ Quay lại menu chính..." + Color.RESET);
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
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.BLUE + "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                System.out.printf(Color.YELLOW + "║ %-104s ║\n" + Color.RESET,
                        "DANH SÁCH KHÓA HỌC ĐÃ SẮP XẾP - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                " (Tổng cộng: " + pageInfo.getTotalRecords() + " khóa học)");
                System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦══════════╦═══════════════════════════════╦════════════╣" + Color.RESET);
                System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-8s ║ %-29s ║ %-10s ║\n" + Color.RESET,
                        "ID", "Tên khóa học", "Số buổi", "Giảng viên", "Ngày tạo");
                System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬══════════╬═══════════════════════════════╬════════════╣" + Color.RESET);

                if (courses.isEmpty()) {
                    System.out.printf(Color.RED + "║ %-94s ║\n" + Color.RESET, "Không có khóa học nào.");
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

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc);

                    switch (choice) {
                        case "n":
                            if (currentPage < totalPages) {
                                currentPage++;
                            } else {
                                System.out.println(Color.RED + "⚠ Bạn đang ở trang cuối cùng." + Color.RESET);
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
                        continuePaging = false;
                    } else {
                        System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                    }
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

            System.out.println(Color.CYAN + "============================= ADMIN UI ==============================" + Color.RESET);
            System.out.println(Color.CYAN + "╔════════════╦══════════════════════════════════════════════════════╗");
            System.out.printf("║ %-10s ║ %-52s ║\n", "Lựa chọn", "Mô tả");
            System.out.println("╠════════════╬══════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s ║ %-52s ║\n", "1", "Hiển thị danh sách sinh viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "2", "Thêm mới sinh viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "3", "Chỉnh sửa sinh viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "4", "Xóa sinh viên theo id");
            System.out.printf("║ %-10s ║ %-52s ║\n", "5", "Tìm kiếm sinh viên theo tên,email,mã id");
            System.out.printf("║ %-10s ║ %-52s ║\n", "6", "Sắp xếp sinh viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "7", "Thoát");
            System.out.println("╚════════════╩══════════════════════════════════════════════════════╝" + Color.RESET);
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
                    System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                    continueProgram = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (continueProgram);
    }

    public static void displayListStudent(Scanner sc, StudentServiceImp studentServiceImp) {
        int currentPage = 1;
        int pageSize = StudentValidator.validatePageSize(sc);
        boolean continueList = true;

        try {
            while (continueList) {
                PageInfo<Student> pageInfo = studentServiceImp.getPageData(currentPage, pageSize);
                List<Student> students = pageInfo.getRecords();
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.MAGENTA + "\n--- Danh sách sinh viên (Trang " + pageInfo.getCurrentPage() + "/" + totalPages + ") --- Tổng " + pageInfo.getTotalRecords() + " sinh viên" + Color.RESET);

                if (students.isEmpty()) {
                    System.out.println(Color.RED + "Không có sinh viên nào ở trang này." + Color.RESET);
                } else {
                    // Đóng khung bảng
                    System.out.println(Color.BLUE + "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                    System.out.printf(Color.YELLOW + "║ %-105s ║\n" + Color.RESET,
                            "DANH SÁCH SINH VIÊN - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                    " (Tổng cộng: " + pageInfo.getTotalRecords() + " sinh viên)");
                    System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦════════════╦═══════════════════════════╦═══════════════╣" + Color.RESET);
                    System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-10s ║ %-25s ║ %-13s ║\n" + Color.RESET,
                            "ID", "Tên sinh viên", "Ngày sinh", "Email", "Số điện thoại");
                    System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬════════════╬═══════════════════════════╬═══════════════╣" + Color.RESET);

                    for (Student student : students) {
                        String gender = student.isStatus() ? "Nam" : "Nữ";
                        System.out.printf(Color.WHITE + "║ %-3d ║ %-42s ║ %-9s ║ %-25s ║ %-13s ║\n" + Color.RESET,
                                student.getId(), student.getName(), student.getBirthday(), student.getEmail(), student.getPhone());
                    }

                    System.out.println(Color.BLUE + "╚═════╩════════════════════════════════════════════╩════════════╩═══════════════════════════╩═══════════════╝" + Color.RESET);
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

    public static void addStudent(StudentServiceImp studentServiceImp) {
        List<Student> studentList = studentServiceImp.getAll();

        Student newStudent = new Student();
        System.out.println("Danh sách sinh viên hiện có:");
        for (Student s : studentList) {
            System.out.println("- " + s.getEmail());
        }

        newStudent.inputData(MainApplication.sc, studentList);

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
        int deleteId = Validator.validateInteger(Color.WHITE + "Nhập ID sinh viên muốn xóa: " + Color.RESET, sc);
        studentServiceImp.delete(deleteId);
    }

    public static void searchStudentByKeyword(Scanner sc, StudentServiceImp studentServiceImp) {
        String keyword = Validator.validateNonEmptyString(Color.WHITE + "Nhập tên, email hoặc mã học viên cần tìm: " + Color.RESET, sc);

        int currentPage = 1;
        int pageSize = StudentValidator.validatePageSize(sc);
        boolean continueSearch = true;

        try {
            while (continueSearch) {
                PageInfo<Student> pageInfo = studentServiceImp.searchByKeyword(keyword, currentPage, pageSize);
                List<Student> students = pageInfo.getRecords();
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.MAGENTA + "\n--- Kết quả tìm kiếm học viên (Trang " + pageInfo.getCurrentPage() + "/" + totalPages + ") --- Tổng " + pageInfo.getTotalRecords() + " học viên" + Color.RESET);

                if (students.isEmpty()) {
                    System.out.println(Color.RED + "Không tìm thấy học viên nào với từ khóa: " + keyword + Color.RESET);
                } else {
                    // Hiển thị bảng giống form gốc
                    System.out.println(Color.BLUE + "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                    System.out.printf(Color.YELLOW + "║ %-105s ║\n" + Color.RESET,
                            "KẾT QUẢ TÌM KIẾM - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                    " (Tổng cộng: " + pageInfo.getTotalRecords() + " học viên)");
                    System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦════════════╦═══════════════════════════╦═══════════════╣" + Color.RESET);
                    System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-10s ║ %-25s ║ %-13s ║\n" + Color.RESET,
                            "ID", "Tên học viên", "Ngày sinh", "Email", "Số điện thoại");
                    System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬════════════╬═══════════════════════════╬═══════════════╣" + Color.RESET);

                    for (Student student : students) {
                        System.out.printf(Color.WHITE + "║ %-3d ║ %-42s ║ %-9s ║ %-25s ║ %-13s ║\n" + Color.RESET,
                                student.getId(), student.getName(), student.getBirthday(), student.getEmail(), student.getPhone());
                    }

                    System.out.println(Color.BLUE + "╚═════╩════════════════════════════════════════════╩════════════╩═══════════════════════════╩═══════════════╝" + Color.RESET);
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
                            continueSearch = false;
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

    public static void sortStudentsWithPaging(Scanner sc, StudentServiceImp studentServiceImp) {
        String sortColumn = StudentValidator.validateSortColumn(sc);
        String sortOrder = StudentValidator.validateSortOrder(sc);
        int pageSize = StudentValidator.validatePageSize(sc);

        int currentPage = 1;
        boolean continueList = true;

        try {
            while (continueList) {
                PageInfo<Student> pageInfo = studentServiceImp.getSortedPagedData(sortColumn, sortOrder, currentPage, pageSize);
                List<Student> students = pageInfo.getRecords();
                int totalPages = pageInfo.getTotalPages();

                // In bảng kết quả
                System.out.println(Color.BLUE + "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                System.out.printf(Color.YELLOW + "║ %-105s ║\n" + Color.RESET,
                        "KẾT QUẢ SẮP XẾP - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                " (Tổng cộng: " + pageInfo.getTotalRecords() + " học viên)");
                System.out.println(Color.BLUE + "╠═════╦════════════════════════════════════════════╦════════════╦═══════════════════════════╦═══════════════╣" + Color.RESET);
                System.out.printf(Color.CYAN + "║ %-3s ║ %-42s ║ %-10s ║ %-25s ║ %-13s ║\n" + Color.RESET,
                        "ID", "Tên học viên", "Ngày sinh", "Email", "Số điện thoại");
                System.out.println(Color.BLUE + "╠═════╬════════════════════════════════════════════╬════════════╬═══════════════════════════╬═══════════════╣" + Color.RESET);

                if (students.isEmpty()) {
                    System.out.println(Color.RED + "║                                Không có học viên nào để hiển thị.                                 ║" + Color.RESET);
                } else {
                    for (Student student : students) {
                        System.out.printf(Color.WHITE + "║ %-3d ║ %-42s ║ %-10s ║ %-25s ║ %-13s ║\n" + Color.RESET,
                                student.getId(), student.getName(), student.getBirthday(), student.getEmail(), student.getPhone());
                    }
                }

                System.out.println(Color.BLUE + "╚═════╩════════════════════════════════════════════╩════════════╩═══════════════════════════╩═══════════════╝" + Color.RESET);

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
                        System.out.print(Color.CYAN + "/ n: next " + Color.RESET);
                    }
                    if (currentPage > 1) {
                        System.out.print(Color.CYAN + "/ p: prev" + Color.RESET);
                    }

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc).trim().toLowerCase();

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
                    String choice = sc.nextLine().trim();
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
        }
    }

    public static void displayMenuRegisterCourse() {
        CourseServiceImp courseServiceImp = new CourseServiceImp();
        EnrollmentServiceImp enrollmentServiceImp = new EnrollmentServiceImp();
        boolean continueProgram = true;
        do {
            System.out.println(Color.CYAN + "============================= ADMIN UI ==============================" + Color.RESET);
            System.out.println(Color.CYAN + "╔════════════╦══════════════════════════════════════════════════════╗");
            System.out.printf("║ %-10s ║ %-52s ║\n", "Lựa chọn", "Mô tả");
            System.out.println("╠════════════╬══════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s ║ %-52s ║\n", "1", "Hiển thị danh sách đăng ký khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "2", "Duyệt sinh viên đăng ký khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "3", "Từ chối sinh viên khỏi khóa học");
            System.out.printf("║ %-10s ║ %-52s ║\n", "4", "Thoát");
            System.out.println("╚════════════╩══════════════════════════════════════════════════════╝" + Color.RESET);

            int choice = Validator.validateInteger(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET,MainApplication.sc);
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
                    System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                    continueProgram = false;
                    break;
                default:
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ. Vui lòng thử lại." + Color.RESET);
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

        int currentPageCourse = 1;
        int pageSizeCourse = StudentValidator.validatePageSize(sc);
        int totalCourses = courses.size();
        int totalPagesCourse = (int) Math.ceil((double) totalCourses / pageSizeCourse);
        boolean choosingCourse = true;
        int courseId = -1;

        while (choosingCourse) {
            int start = (currentPageCourse - 1) * pageSizeCourse;
            int end = Math.min(start + pageSizeCourse, totalCourses);

            System.out.println(Color.CYAN + "\n╔═════ DANH SÁCH KHÓA HỌC ═════════════════════════════════════╗" + Color.RESET);
            System.out.printf(Color.YELLOW + "║ Trang %d/%d (Hiển thị %d - %d trong tổng %d khóa học)           ║\n" + Color.RESET,
                    currentPageCourse, totalPagesCourse, start + 1, end, totalCourses);
            System.out.println(Color.CYAN + "╠═════╦════════════════════════════════════════════════════════╣" + Color.RESET);
            System.out.printf(Color.CYAN + "║ %-3s ║ %-54s ║\n" + Color.RESET, "ID", "Tên khóa học");
            System.out.println(Color.CYAN + "╠═════╬════════════════════════════════════════════════════════╣" + Color.RESET);

            for (int i = start; i < end; i++) {
                Course c = courses.get(i);
                System.out.printf(Color.WHITE + "║ %-3d ║ %-54s ║\n" + Color.RESET, c.getId(), c.getName());
            }

            System.out.println(Color.CYAN + "╚═════╩════════════════════════════════════════════════════════╝" + Color.RESET);

            // Điều hướng phân trang khóa học
            if (totalPagesCourse > 1) {
                System.out.println(Color.MAGENTA + "\nChọn trang (gõ số trang / 0: chọn ID khóa học)" + Color.RESET);
                System.out.print(Color.YELLOW + "Trang: " + Color.RESET);

                for (int i = 1; i <= totalPagesCourse; i++) {
                    if (i == currentPageCourse) {
                        System.out.print(Color.GREEN + "[" + i + "] " + Color.RESET);
                    } else {
                        System.out.print(Color.GRAY + i + " " + Color.RESET);
                    }
                }

                if (currentPageCourse < totalPagesCourse) {
                    System.out.print(Color.CYAN + "/ n: next " + Color.RESET);
                }
                if (currentPageCourse > 1) {
                    System.out.print(Color.CYAN + "/ p: prev" + Color.RESET);
                }


                String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc).trim().toLowerCase();
                switch (choice) {
                    case "n":
                        if (currentPageCourse < totalPagesCourse) currentPageCourse++;
                        else System.out.println(Color.RED + "Bạn đang ở trang cuối." + Color.RESET);
                        break;
                    case "p":
                        if (currentPageCourse > 1) currentPageCourse--;
                        else System.out.println(Color.RED + "Bạn đang ở trang đầu." + Color.RESET);
                        break;
                    case "0":
                        courseId = Validator.validateInteger("Nhập ID khóa học: ", sc);
                        choosingCourse = false;
                        break;
                    default:
                        try {
                            int page = Integer.parseInt(choice);
                            if (page >= 1 && page <= totalPagesCourse) currentPageCourse = page;
                            else System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                        } catch (NumberFormatException e) {
                            System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                        }
                }
            } else {
                courseId = Validator.validateInteger("Nhập ID khóa học: ", sc);
                choosingCourse = false;
            }
        }

        // ===== Hiển thị danh sách sinh viên đăng ký khóa học đó =====
        int currentPage = 1;
        int pageSize = StudentValidator.validatePageSize(sc);
        boolean continuePaging = true;

        try {
            while (continuePaging) {
                PageInfo<Enrollment> pageInfo = enrollmentServiceImp.getStudentsByCoursePaginated(courseId, currentPage, pageSize);
                List<Enrollment> enrollments = pageInfo.getRecords();
                int totalPages = pageInfo.getTotalPages();

                System.out.println(Color.BLUE + "╔════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
                System.out.printf(Color.YELLOW + "║ %-74s ║\n" + Color.RESET,
                        "DANH SÁCH SINH VIÊN - Khóa học ID: " + courseId + " - Trang " + pageInfo.getCurrentPage() + "/" + totalPages +
                                " (Tổng cộng: " + pageInfo.getTotalRecords() + " sinh viên)");
                System.out.println(Color.BLUE + "╠═══════╦════════════════════════════════╦════════════════════╦══════════════╣" + Color.RESET);
                System.out.printf(Color.CYAN + "║ %-5s ║ %-30s ║ %-18s ║ %-12s ║\n" + Color.RESET,
                        "Mã SV", "Tên sinh viên", "Ngày đăng ký", "Trạng thái");
                System.out.println(Color.BLUE + "╠═══════╬════════════════════════════════╬════════════════════╬══════════════╣" + Color.RESET);

                if (enrollments.isEmpty()) {
                    System.out.println(Color.RED + "║                     Không có sinh viên nào để hiển thị.                    ║" + Color.RESET);
                } else {
                    for (Enrollment e : enrollments) {
                        System.out.printf(Color.WHITE + "║ %-5d ║ %-30s ║ %-18s ║ %-12s ║\n" + Color.RESET,
                                e.getStudentId(),
                                e.getStudentName(),
                                e.getRegisteredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                                e.getStatus());
                    }
                }

                System.out.println(Color.BLUE + "╚═══════╩════════════════════════════════╩════════════════════╩══════════════╝" + Color.RESET);

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
                        System.out.print(Color.CYAN + "/ n: next " + Color.RESET);
                    }
                    if (currentPage > 1) {
                        System.out.print(Color.CYAN + "/ p: prev" + Color.RESET);
                    }

                    String choice = Validator.validateNonEmptyString("\n" + Color.CYAN + "Nhập lựa chọn: " + Color.RESET, sc).trim().toLowerCase();
                    switch (choice) {
                        case "n":
                            if (currentPage < totalPages) currentPage++;
                            else System.out.println(Color.RED + "Bạn đang ở trang cuối cùng." + Color.RESET);
                            break;
                        case "p":
                            if (currentPage > 1) currentPage--;
                            else System.out.println(Color.RED + "Bạn đang ở trang đầu tiên." + Color.RESET);
                            break;
                        case "0":
                            System.out.println(Color.YELLOW + "Quay lại menu chính..." + Color.RESET);
                            continuePaging = false;
                            break;
                        default:
                            try {
                                int page = Integer.parseInt(choice);
                                if (page >= 1 && page <= totalPages) currentPage = page;
                                else System.out.println(Color.RED + "Số trang không hợp lệ." + Color.RESET);
                            } catch (NumberFormatException e) {
                                System.out.println(Color.RED + "Lựa chọn không hợp lệ. Nhập số trang hoặc n / p / 0." + Color.RESET);
                            }
                    }
                } else {
                    System.out.println(Color.MAGENTA + "\nChọn 0 để quay lại menu chính." + Color.RESET);
                    String choice = sc.nextLine().trim();
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
        }
    }

    public static void approveStudentEnrollment(Scanner sc, EnrollmentServiceImp enrollmentServiceImp) {
        System.out.println(Color.MAGENTA + "\n=== DUYỆT SINH VIÊN ĐĂNG KÝ KHÓA HỌC ===" + Color.RESET);

        List<Enrollment> waitingList = enrollmentServiceImp.getEnrollmentsByStatus("waiting");

        if (waitingList.isEmpty()) {
            System.out.println(Color.RED + "Không có đơn đăng ký nào đang chờ duyệt." + Color.RESET);
            return;
        }

        System.out.println(Color.CYAN + "+------------+-------------------------+------------------------+---------------------+-----------------+");
        System.out.printf("| %-10s | %-23s | %-22s | %-19s | %-15s |\n", "Enroll ID", "Tên sinh viên", "Tên khóa học", "Ngày đăng ký", "Trạng thái");
        System.out.println("+------------+-------------------------+------------------------+---------------------+-----------------+");

        for (Enrollment e : waitingList) {
            System.out.printf("| %-10d | %-23s | %-22s | %-19s | %-15s |\n",
                    e.getId(),
                    e.getStudentName(),
                    e.getCourseName(),
                    e.getRegisteredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    e.getStatus());
        }

        System.out.println("+------------+-------------------------+------------------------+---------------------+-----------------+");

        int enrollmentId = Validator.validateInteger("Nhập ID đăng ký (Enrollment ID) cần duyệt: ", sc);

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

        System.out.println(Color.CYAN + "+------------+-------------------------+------------------------+---------------------+-----------------+");
        System.out.printf("| %-10s | %-23s | %-22s | %-19s | %-15s |\n", "Enroll ID", "Tên sinh viên", "Tên khóa học", "Ngày đăng ký", "Trạng thái");
        System.out.println("+------------+-------------------------+------------------------+---------------------+-----------------+");

        for (Enrollment e : waitingList) {
            System.out.printf("| %-10d | %-23s | %-22s | %-19s | %-15s |\n",
                    e.getId(),
                    e.getStudentName(),
                    e.getCourseName(),
                    e.getRegisteredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                    e.getStatus());
        }

        System.out.println("+------------+-------------------------+------------------------+---------------------+-----------------+");

        int enrollmentId = Validator.validateInteger("Nhập ID đăng ký (Enrollment ID) cần từ chối: ", sc);

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

        boolean success = enrollmentServiceImp.denyEnrollment(enrollmentId);

        if (success) {
            System.out.println(Color.GREEN + "Từ chối thành công! Trạng thái đã chuyển sang 'denied'." + Color.RESET);
        } else {
            System.out.println(Color.RED + "Từ chối thất bại! Chỉ có thể từ chối các đơn có trạng thái 'waiting'." + Color.RESET);
        }
    }

    public static void displayMenuStatistics() {
        StatisticServiceImp courseServiceImp = new StatisticServiceImp();
        EnrollmentServiceImp enrollmentServiceImp = new EnrollmentServiceImp();
        boolean continueProgram = true;
        do {
            System.out.println(Color.CYAN + "============================= ADMIN UI ==============================" + Color.RESET);
            System.out.println(Color.CYAN + "╔════════════╦══════════════════════════════════════════════════════╗");
            System.out.printf("║ %-10s ║ %-52s ║\n", "Lựa chọn", "Mô tả");
            System.out.println("╠════════════╬══════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s ║ %-52s ║\n", "1", "Thống kê tổng số lượng khóa học và tổng số học viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "2", "Thống kê tổng số học viên theo từng khóa");
            System.out.printf("║ %-10s ║ %-52s ║\n", "3", "Thống kê top 5 khóa học đông sinh viên nhất");
            System.out.printf("║ %-10s ║ %-52s ║\n", "4", "Liệt kê các khóa học có trên 10 học viên");
            System.out.printf("║ %-10s ║ %-52s ║\n", "5", "Thoát");
            System.out.println("╚════════════╩══════════════════════════════════════════════════════╝" + Color.RESET);

            int choice = Validator.validateInteger(Color.MAGENTA + "Chọn tùy chọn: " + Color.RESET,MainApplication.sc);

            switch (choice) {
                case 1:
                    statisticStudentAndCourse(courseServiceImp, enrollmentServiceImp);
                    break;
                case 2:
                    statisticStudentsPerCourse(MainApplication.sc);
                    break;
                case 3:
                    statisticTop5Courses();
                    break;
                case 4:
                    statisticCoursesWithMoreThan10Students();
                    break;
                case 5:
                    System.out.println(Color.YELLOW + "Đã quay lại menu chính." + Color.RESET);
                    continueProgram = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (continueProgram);
    }

    public static void statisticStudentAndCourse(StatisticServiceImp courseServiceImp, EnrollmentServiceImp enrollmentServiceImp) {
        System.out.println(Color.MAGENTA + "\n=== THỐNG KÊ TỔNG SỐ LƯỢNG KHÓA HỌC & HỌC VIÊN ===" + Color.RESET);

        int totalCourses = courseServiceImp.getTotalCourseCount();
        int totalConfirmedStudents = enrollmentServiceImp.countConfirmedStudents();

        System.out.println(Color.CYAN + "\n===== THỐNG KÊ HỆ THỐNG =====" + Color.RESET);
        System.out.println("Tổng số khóa học      : " + Color.YELLOW + totalCourses + Color.RESET);
        System.out.println("Tổng số học viên    : " + Color.YELLOW + totalConfirmedStudents + Color.RESET);
    }

    public static void statisticStudentsPerCourse(Scanner sc) {
        StatisticServiceImp statisticServiceImp = new StatisticServiceImp();

        List<Object[]> courseStats = statisticServiceImp.countStudentsGroupByCourse();

        if (courseStats.isEmpty()) {
            System.out.println(Color.RED + "Không có khóa học nào có học viên đã xác nhận." + Color.RESET);
            return;
        }

        int currentPage = 1;
        int pageSize = StudentValidator.validatePageSize(sc);
        int totalPages = (int) Math.ceil((double) courseStats.size() / pageSize);

        boolean continueListing = true;

        while (continueListing) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, courseStats.size());

            // In bảng thống kê
            System.out.println(Color.CYAN + "╔════════════════════════════════════════════════════════════════════════════════════╗" + Color.RESET);
            System.out.printf(Color.YELLOW + "║ %-82s ║\n" + Color.RESET,
                    "THỐNG KÊ SỐ LƯỢNG HỌC VIÊN THEO KHÓA HỌC");
            System.out.println(Color.CYAN + "╠═════════════════════════════╦══════════════════════════════════════════════════════╣" + Color.RESET);
            System.out.printf(Color.CYAN + "║ %-27s ║ %-52s ║\n" + Color.RESET, "Tên khóa học", "Số học viên");
            System.out.println(Color.CYAN + "╠═════════════════════════════╬══════════════════════════════════════════════════════╣" + Color.RESET);

            // In dữ liệu khóa học trong trang hiện tại
            for (int i = startIndex; i < endIndex; i++) {
                Object[] stat = courseStats.get(i);
                String courseName = (String) stat[0];
                int studentCount = (int) stat[1];
                System.out.printf(Color.WHITE + "║ %-27s ║ %-52d ║\n" + Color.RESET, courseName, studentCount);
            }

            System.out.println(Color.CYAN + "╚═════════════════════════════╩══════════════════════════════════════════════════════╝" + Color.RESET);

            // Hiển thị phân trang
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
                        continueListing = false;
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
                    continueListing = false;
                } else {
                    System.out.println(Color.RED + "Lựa chọn không hợp lệ." + Color.RESET);
                }
            }
        }
    }

    public static void statisticTop5Courses() {
        StatisticServiceImp statisticServiceImp = new StatisticServiceImp();

        List<Object[]> topCourses = statisticServiceImp.getTop5CoursesByStudentCount();

        if (topCourses.isEmpty()) {
            System.out.println(Color.RED + "Không có khóa học nào có học viên đã xác nhận." + Color.RESET);
            return;
        }

        System.out.println(Color.CYAN + "+-----------------------------+------------------+");
        System.out.printf("| %-27s | %-16s |\n", "Tên khóa học", "Số học viên");
        System.out.println("+-----------------------------+------------------+");

        for (Object[] stat : topCourses) {
            String courseName = (String) stat[0];
            int studentCount = (int) stat[1];
            System.out.printf("| %-27s | %-16d |\n", courseName, studentCount);
        }

        System.out.println("+-----------------------------+------------------+");
    }

    public static void statisticCoursesWithMoreThan10Students() {
        StatisticServiceImp statisticServiceImp = new StatisticServiceImp();

        List<Object[]> courses = statisticServiceImp.getCoursesWithMoreThan10Students();

        if (courses.isEmpty()) {
            System.out.println(Color.RED + "Không có khóa học nào có hơn 10 học viên đã xác nhận." + Color.RESET);
            return;
        }

        System.out.println(Color.CYAN + "+-----------------------------+------------------+");
        System.out.printf("| %-25s | %-16s |\n", "Tên khóa học", "Số học viên");
        System.out.println("+-----------------------------+------------------+");

        for (Object[] stat : courses) {
            String courseName = (String) stat[0];
            int studentCount = (int) stat[1];
            System.out.printf("| %-25s | %-16d |\n", courseName, studentCount);
        }

        System.out.println("+-----------------------------+------------------+");
    }
}
