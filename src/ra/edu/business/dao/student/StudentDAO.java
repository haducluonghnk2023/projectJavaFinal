package ra.edu.business.dao.student;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.student.Student;
import ra.edu.utils.PageInfo;

import java.util.List;

public interface StudentDAO extends AppDAO<Student> {
    List<Student> getPageData(int page, int size);
    int getTotalRecords();
    List<Student> searchByKeyword(String keyword);
    List<Student> getSortedPaged(String sortColumn, String sortOrder, int page, int pageSize);
    boolean changePassword(String email, String oldPassword, String newPassword);
    String checkOldPassword(String email, String oldPassword);
    PageInfo<Enrollment> getPagedEnrollmentsByStudentId(int studentId, int page, int pageSize);
}
