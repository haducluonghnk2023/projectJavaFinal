package ra.edu.business.service.course;

import ra.edu.business.model.course.Course;
import ra.edu.business.service.AppService;
import ra.edu.utils.PageInfo;

public interface CourseService extends AppService {
    PageInfo<Course> getPageData(int page, int size);
}
