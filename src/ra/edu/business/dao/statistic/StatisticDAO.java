package ra.edu.business.dao.statistic;

import java.util.List;

public interface StatisticDAO {
    int getTotalCourseCount();
    List<Object[]> countStudentsGroupByCourse();
    List<Object[]> getTop5CoursesByStudentCount();
    List<Object[]> getCoursesWithMoreThan10Students();
}
