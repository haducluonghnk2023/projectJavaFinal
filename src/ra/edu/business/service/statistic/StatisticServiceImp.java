package ra.edu.business.service.statistic;

import ra.edu.business.dao.statistic.StatisticDAOImp;

import java.util.List;

public class StatisticServiceImp implements  StatisticService {
    StatisticDAOImp statisticDAOImp = new StatisticDAOImp();

    @Override
    public int getTotalCourseCount() {
        return statisticDAOImp.getTotalCourseCount();
    }

    @Override
    public List<Object[]> countStudentsGroupByCourse() {
        return statisticDAOImp.countStudentsGroupByCourse();
    }

    @Override
    public List<Object[]> getTop5CoursesByStudentCount() {
        return statisticDAOImp.getTop5CoursesByStudentCount();
    }

    @Override
    public List<Object[]> getCoursesWithMoreThan10Students() {
        return statisticDAOImp.getCoursesWithMoreThan10Students();
    }
}
