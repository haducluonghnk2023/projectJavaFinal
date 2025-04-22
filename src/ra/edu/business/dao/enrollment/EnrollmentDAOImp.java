package ra.edu.business.dao.enrollment;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.enrollment.Status;
import ra.edu.exception.login.AppException;
import ra.edu.utils.Color;
import ra.edu.utils.PageInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImp implements  EnrollmentDAO {
    @Override
    public boolean registerCourse(Enrollment registration) {
        String sql = "{CALL RegisterCourse(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, registration.getStudentId());
            callSt.setInt(2, registration.getCourseId());

            callSt.execute();

            System.out.println(Color.GREEN + "Đăng ký khóa học thành công!" + Color.RESET);
            return true;

        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.out.println(Color.RED + "Lỗi đăng ký: " + e.getMessage() + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi SQL: " + e.getMessage() + Color.RESET);
            }
        }
        return false;
    }

    @Override
    public boolean isCourseAlreadyRegistered(int studentId, int courseId) {
        String sql = "{CALL IsCourseAlreadyRegistered(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);
            callSt.setInt(2, courseId);

            try (ResultSet rs = callSt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("is_registered") == 1;
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra đăng ký khóa học: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Enrollment> getCoursesByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "{CALL GetCoursesByStudent(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setId(rs.getInt("id"));
                    enrollment.setStudentId(rs.getInt("student_id"));
                    enrollment.setCourseId(rs.getInt("course_id"));
                    enrollment.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());

                    enrollments.add(enrollment);
                }
            }

        } catch (SQLException e) {
            throw new AppException("Lỗi khi lấy danh sách khóa học đã đăng ký cho sinh viên có ID = " + studentId, e);
        }

        return enrollments;
    }


    @Override
    public boolean cancelEnrollment(int studentId, int courseId) {
        String sql = "{CALL CancelEnrollment(?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);
            callSt.setInt(2, courseId);
            callSt.registerOutParameter(3, Types.BOOLEAN);

            callSt.execute();

            return callSt.getBoolean(3);

        } catch (SQLException e) {
            System.err.println("Lỗi khi hủy đăng ký khóa học: " + e.getMessage());
        }

        return false;
    }



    @Override
    public Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId) {
        String sql = "{CALL GetEnrollmentByStudentAndCourse(?, ?)}";
        Enrollment enrollment = null;

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);
            callSt.setInt(2, courseId);

            try (ResultSet rs = callSt.executeQuery()) {
                if (rs.next()) {
                    enrollment = new Enrollment();
                    enrollment.setId(rs.getInt("id"));
                    enrollment.setStudentId(rs.getInt("student_id"));
                    enrollment.setCourseId(rs.getInt("course_id"));
                    enrollment.setStatus(Status.valueOf(rs.getString("status")));
                    enrollment.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin đăng ký khóa học: " + e.getMessage());
        }

        return enrollment;
    }

    @Override
    public List<Enrollment> getSortedPaged(String sortColumn, String sortOrder, int page, int pageSize) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "{CALL GetEnrollmentsSortedPaged(?, ?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, sortColumn);
            callSt.setString(2, sortOrder);
            callSt.setInt(3, page);
            callSt.setInt(4, pageSize);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setId(rs.getInt("id"));
                    enrollment.setStudentId(rs.getInt("student_id"));
                    enrollment.setCourseId(rs.getInt("course_id"));
                    enrollment.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                    enrollment.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                    enrollment.setCourseName(rs.getString("course_name"));

                    enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enrollments;
    }


    @Override
    public int getTotalEnrollment() {
        int total = 0;
        String sql = "{CALL getTotalEnrollments()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total_enrollments");
            }

        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi gọi GetTotalEnrollments: " + e.getMessage() + Color.RESET);
        }

        return total;
    }

    @Override
    public PageInfo<Enrollment> getStudentsByCoursePaginated(int courseId, int page, int pageSize) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "{CALL get_students_by_course_paginated(?, ?, ?)}"; // Gọi procedure lấy danh sách sinh viên
        int totalRecords = 0;

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, courseId);
            callSt.setInt(2, page);
            callSt.setInt(3, pageSize);

            // Thực thi truy vấn lấy danh sách sinh viên
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setStudentId(rs.getInt("student_id"));
                e.setCourseId(courseId);
                e.setCourseName(rs.getString("course_name"));
                e.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                e.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                e.setStudentName(rs.getString("student_name"));

                enrollments.add(e);
            }

            // Gọi stored procedure để lấy tổng số bản ghi
            String countSql = "{CALL get_enrollment_count_by_course(?, ?)}"; // Gọi procedure lấy tổng số bản ghi
            try (CallableStatement countStmt = conn.prepareCall(countSql)) {
                countStmt.setInt(1, courseId);
                countStmt.registerOutParameter(2, Types.INTEGER); // Đăng ký tham số OUT

                countStmt.execute();
                totalRecords = countStmt.getInt(2); // Lấy kết quả từ tham số OUT
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tính toán tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Tạo và trả về PageInfo
        PageInfo<Enrollment> pageInfo = new PageInfo<>();
        pageInfo.setRecords(enrollments);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setCurrentPage(page);

        return pageInfo;
    }

    @Override
    public boolean updateEnrollmentStatus(int enrollmentId, String status) {
        String sql = "{CALL UpdateEnrollmentStatus(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            // Thiết lập tham số cho stored procedure
            callSt.setInt(1, enrollmentId);
            callSt.setString(2, status);

            // Thực thi stored procedure
            int rowsAffected = callSt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new AppException("Lỗi khi cập nhật trạng thái đăng ký sinh viên", e);
        }
    }

    @Override
    public boolean approveEnrollment(int enrollmentId) {
        String sql = "{CALL UpdateEnrollmentStatus(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, enrollmentId);
            callSt.setString(2, "confirm");

            int rowsAffected = callSt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new AppException("Lỗi khi duyệt đơn đăng ký khóa học", e);
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByStatus(String status) {
        List<Enrollment> result = new ArrayList<>();

        String sql = "{CALL GetEnrollmentsByStatus(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, status);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setId(rs.getInt("id"));
                e.setStudentId(rs.getInt("student_id"));
                e.setStudentName(rs.getString("student_name"));
                e.setCourseName(rs.getString("course_name"));
                e.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                e.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                result.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean denyEnrollment(int enrollmentId) {
        String sql = "{CALL UpdateEnrollmentStatus(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, enrollmentId);
            callSt.setString(2, "denied");

            int rowsAffected = callSt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new AppException("Lỗi khi xóa sinh viên đăng ký khóa học", e);
        }
    }


    @Override
    public List<Enrollment> findAll() {
        return List.of();
    }

    @Override
    public Object findById(int id) {
        return null;
    }

    @Override
    public boolean save(Enrollment enrollment) {
        return false;
    }

    @Override
    public boolean update(Enrollment enrollment) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
