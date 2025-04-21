package ra.edu.business.dao.student;

import com.mysql.cj.jdbc.JdbcConnection;
import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.student.Student;
import ra.edu.utils.Color;

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

            // Set các tham số IN cho thủ tục
            callSt.setString(1, student.getName());
            callSt.setDate(2, Date.valueOf(student.getBirthday()));
            callSt.setString(3, student.getEmail());
            callSt.setBoolean(4, student.isStatus());
            callSt.setString(5, student.getPhone());

            callSt.registerOutParameter(6, Types.INTEGER);

            callSt.executeUpdate();

            int rowsAffected = callSt.getInt(6);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println(Color.RED + "Lỗi khi thêm sinh viên: " + e.getMessage() + Color.RESET);
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
                System.out.println(Color.YELLOW + e.getMessage() + Color.RESET);
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

}
