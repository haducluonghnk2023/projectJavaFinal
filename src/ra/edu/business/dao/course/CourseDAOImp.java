package ra.edu.business.dao.course;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.course.Course;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImp implements CourseDAO {
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Object findById(int id) {
        String sql = "{CALL FindCourseById(?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, id);
            ResultSet rs = callSt.executeQuery();

            if (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
                return course;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khóa học theo ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(Object o) {
        Course course = (Course) o;
        String sql = "{CALL AddCourse(?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, course.getName());
            callSt.setInt(2, course.getDuration());
            callSt.setString(3, course.getInstructor());

            int rowsAffected = callSt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Object o) {
        Course course = (Course) o;
        String sql = "{CALL UpdateCourse(?, ?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, course.getId());
            callSt.setString(2, course.getName());
            callSt.setInt(3, course.getDuration());
            callSt.setString(4, course.getInstructor());

            int rowsAffected = callSt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<Course> getPageData(int page, int size) {
        List<Course> courses = new ArrayList<>();
        String sql = "{CALL GetCoursesPaginated(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, page);
            callSt.setInt(2, size);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getInt("id"));
                    course.setName(rs.getString("name"));
                    course.setDuration(rs.getInt("duration"));
                    course.setInstructor(rs.getString("instructor"));
                    course.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());

                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving paginated courses: " + e.getMessage());
        }

        return courses;
    }

    @Override
    public int getTotalCourses() {
        int total = 0;
        String sql = "{CALL GetTotalCourses()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total_courses");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi gọi GetTotalCourses: " + e.getMessage());
        }

        return total;
    }
}
