package ra.edu.business.dao.enrollment;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.enrollment.Status;
import ra.edu.utils.Color;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            int rowsAffected = callSt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi đăng ký khóa học: " + e.getMessage());
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
            System.err.println("Lỗi khi lấy danh sách khóa học đã đăng ký: " + e.getMessage());
        }

        return enrollments;
    }

    @Override
    public boolean cancelEnrollment(int studentId, int courseId) {
        String sql = "{CALL CancelEnrollment(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);
            callSt.setInt(2, courseId);

            callSt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi hủy đăng ký khóa học: " + e.getMessage());
            return false;
        }
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
