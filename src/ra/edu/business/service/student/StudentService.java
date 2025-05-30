package ra.edu.business.service.student;

import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.student.Student;
import ra.edu.business.service.AppService;
import ra.edu.utils.PageInfo;

import java.util.List;

public interface StudentService extends AppService<Student> {
    PageInfo<Student> getPageData(int page, int size);
    PageInfo<Student> searchByKeyword(String keyword, int page, int size);
    PageInfo<Student> getSortedPagedData(String sortColumn, String sortOrder, int page, int size);
    boolean changePassword(String email, String oldPassword, String newPassword);
    Boolean checkOldPassword(String email, String oldPassword);
    PageInfo<Enrollment> getPagedEnrollmentsByStudentId(int studentId, int page, int pageSize);
}
