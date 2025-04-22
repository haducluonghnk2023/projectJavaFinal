package ra.edu.business.service.enrollment;

import ra.edu.business.dao.course.CourseDAOImp;
import ra.edu.business.dao.enrollment.EnrollmentDAOImp;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.utils.PageInfo;

import java.util.List;

public class EnrollmentServiceImp implements  EnrollmentService {
    EnrollmentDAOImp enrollmentDAOImp = new EnrollmentDAOImp();
    CourseDAOImp courseDAOImp = new CourseDAOImp();
    public EnrollmentServiceImp() {
        this.enrollmentDAOImp = enrollmentDAOImp;
        this.courseDAOImp = courseDAOImp;
    }

    @Override
    public boolean registerCourse(Enrollment registration) {
        return enrollmentDAOImp.registerCourse(registration);
    }

    @Override
    public boolean isCourseAlreadyRegistered(int studentId, int courseId) {
        return enrollmentDAOImp.isCourseAlreadyRegistered(studentId, courseId);
    }

    @Override
    public List<Enrollment> getCoursesByStudent(int studentId) {
        return enrollmentDAOImp.getCoursesByStudent(studentId);
    }

    @Override
    public boolean registerCourseForStudent(int studentId, int courseId) {
        boolean isAlreadyRegistered = courseDAOImp.isCourseRegisteredByStudent(studentId, courseId);
        if (isAlreadyRegistered) {
            return false;
        }

        return courseDAOImp.registerCourseForStudent(studentId, courseId);
    }

    @Override
    public boolean cancelEnrollment(int studentId, int courseId) {
        return  enrollmentDAOImp.cancelEnrollment(studentId, courseId);
    }

    @Override
    public Enrollment getEnrollmentByStudentAndCourse(int studentId, int courseId) {
        return enrollmentDAOImp.getEnrollmentByStudentAndCourse(studentId, courseId);
    }

    @Override
    public PageInfo<Enrollment> getSortedPagedData(String sortColumn, String sortOrder, int page, int size) {
        List<Enrollment> enrollments = enrollmentDAOImp.getSortedPaged(sortColumn, sortOrder, page, size);

        int totalRecords = enrollmentDAOImp.getTotalEnrollment();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        PageInfo<Enrollment> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(enrollments);

        return pageInfo;
    }

    @Override
    public PageInfo<Enrollment> getStudentsByCoursePaginated(int courseId, int page, int pageSize) {
        return enrollmentDAOImp.getStudentsByCoursePaginated(courseId, page, pageSize);
    }

    @Override
    public boolean approveEnrollment(int enrollmentId) {
        return enrollmentDAOImp.approveEnrollment(enrollmentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStatus(String status) {
        return enrollmentDAOImp.getEnrollmentsByStatus(status);
    }

    @Override
    public boolean denyEnrollment(int enrollmentId) {
        return enrollmentDAOImp.denyEnrollment(enrollmentId);
    }

}
