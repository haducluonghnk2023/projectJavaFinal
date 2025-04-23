package ra.edu.business.service.statistic;

import java.util.List;

public interface StatisticService {
    int getTotalCourseCount();
    List<Object[]> countStudentsGroupByCourse();
    List<Object[]> getTop5CoursesByStudentCount();
    List<Object[]> getCoursesWithMoreThan10Students();
}
