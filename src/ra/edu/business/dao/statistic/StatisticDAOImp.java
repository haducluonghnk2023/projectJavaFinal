package ra.edu.business.dao.statistic;

import ra.edu.business.config.ConnectionDB;
import ra.edu.exception.login.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAOImp implements  StatisticDAO {
    @Override
    public int getTotalCourseCount() {
        String sql = "{CALL GetTotalCourseCount(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.registerOutParameter(1, Types.INTEGER);
            callSt.execute();

            return callSt.getInt(1);
        } catch (SQLException e) {
            throw new AppException("Lỗi khi lấy tổng số lượng khóa học", e);
        }
    }

    @Override
    public List<Object[]> countStudentsGroupByCourse() {
        List<Object[]> result = new ArrayList<>();
        String sql = "{CALL GetStudentCountPerCourse()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            while (rs.next()) {
                String courseName = rs.getString("course_name");
                int studentCount = rs.getInt("student_count");
                result.add(new Object[]{courseName, studentCount});
            }
        } catch (SQLException e) {
            throw new AppException("Lỗi khi thống kê số học viên theo khóa học", e);
        }

        return result;
    }

    @Override
    public List<Object[]> getTop5CoursesByStudentCount() {
        List<Object[]> result = new ArrayList<>();
        String sql = "{CALL GetTop5CoursesByStudentCount()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            while (rs.next()) {
                String courseName = rs.getString("name");
                int studentCount = rs.getInt("student_count");
                result.add(new Object[]{courseName, studentCount});
            }
        } catch (SQLException e) {
            throw new AppException("Lỗi khi lấy thông tin top 5 khóa học đông sinh viên nhất", e);
        }

        return result;
    }

    @Override
    public List<Object[]> getCoursesWithMoreThan10Students() {
        List<Object[]> result = new ArrayList<>();
        String sql = "{CALL GetCoursesWithMoreThan10Students()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            while (rs.next()) {
                String courseName = rs.getString("name");
                int studentCount = rs.getInt("student_count");
                result.add(new Object[]{courseName, studentCount});
            }
        } catch (SQLException e) {
            throw new AppException("Lỗi khi lấy danh sách khóa học có hơn 10 học viên", e);
        }

        return result;
    }

}
