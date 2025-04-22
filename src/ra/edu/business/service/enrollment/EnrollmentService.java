package ra.edu.business.service.enrollment;

import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.utils.PageInfo;

import java.util.List;

public interface EnrollmentService {
    boolean registerCourse(Enrollment registration);
    boolean isCourseAlreadyRegistered(int studentId, int courseId);
    List<Enrollment> getCoursesByStudent(int studentId);
    boolean registerCourseForStudent(int studentId, int courseId);
    boolean cancelEnrollment(int studentId, int courseId);
    Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId);
    PageInfo<Enrollment> getSortedPagedData(String sortColumn, String sortOrder, int page, int size);
    PageInfo<Enrollment> getStudentsByCoursePaginated(int courseId, int page, int pageSize);
    boolean approveEnrollment(int enrollmentId);
    List<Enrollment> getEnrollmentsByStatus(String status);
    boolean denyEnrollment(int enrollmentId);
}
