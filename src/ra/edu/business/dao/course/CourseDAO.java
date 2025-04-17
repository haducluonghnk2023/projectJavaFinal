package ra.edu.business.dao.course;

import ra.edu.business.dao.AppDAO;
import ra.edu.business.model.course.Course;

import java.util.List;

public interface CourseDAO extends AppDAO {
    List<Course> getPageData(int page, int size);
    int getTotalCourses();
}
