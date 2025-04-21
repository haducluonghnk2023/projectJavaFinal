package ra.edu.business.dao.course;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.course.Course;

import java.util.List;

public interface CourseDAO extends AppDAO {
    List<Course> getPageData(int page, int size);
    int getTotalCourses();
    List<Course> searchByName(String keyword);
    List<Course> getSortedPaged(String sortColumn, String sortOrder, int page, int pageSize);
    boolean isCourseRegisteredByStudent(int studentId, int courseId);
    boolean registerCourseForStudent(int studentId, int courseId);
}
