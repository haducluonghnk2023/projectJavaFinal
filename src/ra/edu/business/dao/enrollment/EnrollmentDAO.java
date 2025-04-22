package ra.edu.business.dao.enrollment;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.utils.PageInfo;

import java.util.List;

public interface EnrollmentDAO extends AppDAO<Enrollment> {
    boolean registerCourse(Enrollment registration);
    boolean isCourseAlreadyRegistered(int studentId, int courseId);
    List<Enrollment> getCoursesByStudent(int studentId);
    boolean cancelEnrollment(int studentId, int courseId);
    Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId);
    List<Enrollment> getSortedPaged(String sortColumn, String sortOrder, int page, int pageSize);
    int getTotalEnrollment();
    PageInfo<Enrollment> getStudentsByCoursePaginated(int courseId, int page, int pageSize);
    boolean updateEnrollmentStatus(int enrollmentId, String status);
    boolean approveEnrollment(int enrollmentId);
    List<Enrollment> getEnrollmentsByStatus(String status);
    boolean denyEnrollment(int enrollmentId);
}
