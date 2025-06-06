package ra.edu.business.dao.student;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.account.Account;
import ra.edu.business.model.account.Role;
import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.enrollment.Status;
import ra.edu.business.model.student.Student;
import ra.edu.utils.Color;
import ra.edu.utils.PageInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImp implements StudentDAO{
    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "{CALL GetAllStudents()}";
        try(Connection conn = ConnectionDB.openConnection();
            CallableStatement callSt = conn.prepareCall(sql);
            ResultSet rs = callSt.executeQuery()){

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setBirthday(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setStatus(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setCreated_at(rs.getString("create_at"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi truy xuất tất cả học viên: " + e.getMessage() + Color.RESET);
        }

        return students;
    }

    @Override
    public Object findById(int id) {
        String sql = "{CALL FindStudentById(?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)){

            callSt.setInt(1, id);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setBirthday(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setStatus(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setCreated_at(rs.getString("create_at"));
                return student;
            }
        } catch (SQLException e) {
            System.out.println(Color.RED + "Lỗi khi tìm khóa học theo ID: " + e.getMessage() + Color.RESET);
        }
        return null;
    }

    @Override
    public boolean save(Student student) {
        String sql = "{CALL AddStudent(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, student.getName());
            callSt.setDate(2, Date.valueOf(student.getBirthday()));
            callSt.setString(3, student.getEmail());
            callSt.setBoolean(4, student.isStatus());
            callSt.setString(5, student.getPhone());

            callSt.registerOutParameter(6, Types.INTEGER); // OUT parameter để lấy rows affected

            boolean hasResult = callSt.execute();

            int rowsAffected = callSt.getInt(6); // lấy rows affected

            if (rowsAffected > 0) {
                // Sau khi thêm thành công, lấy id mới nhất
                try (PreparedStatement ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        student.setId(newId); // Gán ID mới vào student
                    }
                }

                // Lấy kết quả trả về password, email, message
                if (hasResult) {
                    try (ResultSet rs = callSt.getResultSet()) {
                        if (rs.next()) {
                            String password = rs.getString("password");
                            String email = rs.getString("email");
                            String message = rs.getString("message");

                            System.out.println(Color.GREEN + "Tài khoản tạo thành công cho email: " + email + Color.RESET);
                            System.out.println(Color.YELLOW + "Mật khẩu tạm thời: " + password + Color.RESET);

                            student.setGeneratedPassword(password);
                        }
                    }
                }

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Email đã tồn tại trong hệ thống")) {
                System.out.println(Color.RED + "Email này đã tồn tại trong hệ thống. Vui lòng chọn email khác." + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi khi thêm sinh viên: " + e.getMessage() + Color.RESET);
            }
            return false;
        }
    }

    @Override
    public boolean update(Student student) {
        String sql = "{CALL UpdateStudent(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, student.getId());
            callSt.setString(2, student.getName());
            callSt.setDate(3, Date.valueOf(student.getBirthday()));
            callSt.setString(4, student.getEmail());
            callSt.setString(5, student.getPhone());
            callSt.setBoolean(6, student.isStatus());

            int rowsAffected = callSt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println(Color.RED + "Lỗi khi cập nhật sinh viên: " + e.getMessage() + Color.RESET);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "{CALL deactivate_and_delete_student(?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, id);
            callSt.execute();

            return true;

        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.out.println(Color.YELLOW+ e.getMessage() + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi khi xóa học viên: " + e.getMessage() + Color.RESET);
            }
            return false;
        }
    }


    @Override
    public List<Student> getPageData(int page, int size) {
        List<Student> students = new ArrayList<>();
        String sql = "{CALL GetStudentsPaginated(?,?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, page);
            callSt.setInt(2, size);
            ResultSet rs = callSt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setBirthday(rs.getDate("dob").toLocalDate());
                student.setStatus(rs.getBoolean("sex"));
                student.setCreated_at(rs.getString("create_at"));
                students.add(student);
            }

        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi truy xuất sinh viên phân trang: " + e.getMessage() + Color.RESET);
        }
        return students;
    }

    @Override
    public int getTotalRecords() {
        int total = 0;
        String sql = "{CALL GetTotalStudents()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total_students");
            }

        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi gọi GetTotalStudents: " + e.getMessage() + Color.RESET);
        }

        return total;
    }

    @Override
    public List<Student> searchByKeyword(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "{CALL SearchStudentsByKeyword(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, keyword);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    student.setBirthday(rs.getDate("dob").toLocalDate());
                    student.setStatus(rs.getBoolean("sex"));
                    student.setCreated_at(rs.getString("create_at"));

                    students.add(student);
                }
            }
        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi tìm kiếm học viên: " + e.getMessage() + Color.RESET);
        }

        return students;
    }

    @Override
    public List<Student> getSortedPaged(String sortColumn, String sortOrder, int page, int pageSize) {
        List<Student> students = new ArrayList<>();
        String sql = "{CALL GetStudentsSortedPaged(?, ?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, sortColumn);
            callSt.setString(2, sortOrder);
            callSt.setInt(3, page);
            callSt.setInt(4, pageSize);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    student.setBirthday(rs.getDate("dob").toLocalDate());
                    student.setStatus(rs.getBoolean("sex"));
                    student.setCreated_at(rs.getString("create_at"));

                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Connection conn = null;
        CallableStatement callableStatement = null;
        boolean status = false;

        try {
            conn = ConnectionDB.openConnection();
            callableStatement = conn.prepareCall("{CALL sp_changePassword(?, ?, ?, ?)}");
            callableStatement.setString(1, email);
            callableStatement.setString(2, oldPassword);
            callableStatement.setString(3, newPassword);
            callableStatement.registerOutParameter(4, Types.BOOLEAN);

            callableStatement.execute();
            status = callableStatement.getBoolean(4);

            return status;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public String checkOldPassword(String email, String oldPassword) {
        String result = null;
        String sql = "{CALL CheckOldPassword(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, oldPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("result");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PageInfo<Enrollment> getPagedEnrollmentsByStudentId(int studentId, int page, int pageSize) {
        List<Enrollment> enrollments = new ArrayList<>();
        int totalRecords = 0;
        int totalPages;

        String dataSql = "{CALL GetEnrollmentsByStudentIdPaged(?, ?, ?)}";
        String countSql = "{CALL GetEnrollmentCountByStudentId(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection()) {

            // 1. Gọi procedure lấy dữ liệu phân trang
            try (CallableStatement stmt = conn.prepareCall(dataSql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, page);
                stmt.setInt(3, pageSize);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setCourseId(rs.getInt("course_id"));
                    e.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                    e.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));

                    Course course = new Course();
                    course.setId(rs.getInt("course_id"));
                    course.setName(rs.getString("course_name"));
                    e.setCourse(course);

                    enrollments.add(e);
                }
            }

            // 2. Gọi procedure lấy tổng số bản ghi
            try (CallableStatement countStmt = conn.prepareCall(countSql)) {
                countStmt.setInt(1, studentId);
                countStmt.registerOutParameter(2, Types.INTEGER);
                countStmt.execute();
                totalRecords = countStmt.getInt(2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 3. Trả về đối tượng PageInfo
        PageInfo<Enrollment> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(enrollments);

        return pageInfo;
    }
}
