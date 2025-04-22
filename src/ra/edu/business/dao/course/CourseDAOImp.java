package ra.edu.business.dao.course;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.course.Course;
import ra.edu.utils.Color;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImp implements CourseDAO {
    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "{CALL GetAllCourses()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));
                course.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
                courses.add(course);
            }

        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi truy xuất tất cả khóa học: " + e.getMessage() + Color.RESET);
        }

        return courses;
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
            System.out.println(Color.RED + "Lỗi khi tìm khóa học theo ID: " + e.getMessage() + Color.RESET);
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
            System.out.println(Color.RED + "Lỗi khi chèn khóa học: " + e.getMessage() + Color.RESET);
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
            System.out.println(Color.RED + "Lỗi khi cập nhật khóa học: " + e.getMessage() + Color.RESET);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "{CALL DeleteCourseById(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, id);
            callSt.execute();

            return true;

        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                System.out.println(Color.YELLOW + e.getMessage() + Color.RESET);
            } else {
                System.out.println(Color.RED + "Lỗi khi xóa khóa học: " + e.getMessage() + Color.RESET);
            }
            return false;
        }
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
            System.err.println(Color.RED + "Lỗi khi truy xuất các khóa học được phân trang: " + e.getMessage() + Color.RESET);
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
            System.err.println(Color.RED + "Lỗi khi gọi GetTotalCourses: " + e.getMessage() + Color.RESET);
        }

        return total;
    }

    @Override
    public List<Course> searchByName(String keyword) {
        List<Course> courses = new ArrayList<>();
        String sql = "{CALL SearchCoursesByName(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, keyword);

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
            System.err.println(Color.RED + "Lỗi khi tìm kiếm khóa học theo tên: " + e.getMessage() + Color.RESET);
        }
        return courses;
    }

    @Override
    public List<Course> getSortedPaged(String sortColumn, String sortOrder, int page, int pageSize) {
        List<Course> courses = new ArrayList<>();
        String sql = "{CALL GetCoursesSortedPaged(?, ?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, sortColumn);
            callSt.setString(2, sortOrder);
            callSt.setInt(3, page);
            callSt.setInt(4, pageSize);

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
            e.printStackTrace();
        }

        return courses;
    }

    @Override
    public boolean isCourseRegisteredByStudent(int studentId, int courseId) {
        String sql = "{CALL CheckCourseRegistration(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);
            callSt.setInt(2, courseId);

            ResultSet rs = callSt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi kiểm tra đăng ký khóa học: " + e.getMessage() + Color.RESET);
            return false;
        }
    }


    @Override
    public boolean registerCourseForStudent(int studentId, int courseId) {
        String sql = "{CALL RegisterCourseForStudent(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, studentId);
            callSt.setInt(2, courseId);

            int rowsAffected = callSt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(Color.RED + "Lỗi khi đăng ký khóa học cho học viên: " + e.getMessage() + Color.RESET);
            return false;
        }
    }

}
