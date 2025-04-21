package ra.edu.business.service.course;

import ra.edu.business.model.course.Course;
import ra.edu.business.service.AppService;
import ra.edu.utils.PageInfo;

public interface CourseService extends AppService {
    PageInfo<Course> getPageData(int page, int size);
    PageInfo<Course> searchByName(String keyword, int page, int size);
    PageInfo<Course> getSortedPagedData(String sortColumn, String sortOrder, int page, int size);
    boolean isCourseRegisteredByStudent(int studentId, int courseId);
    boolean registerCourseForStudent(int studentId, int courseId);
}
