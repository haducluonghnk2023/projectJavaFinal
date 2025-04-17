package ra.edu.business.service.course;

import ra.edu.business.dao.course.CourseDAO;
import ra.edu.business.dao.course.CourseDAOImp;
import ra.edu.business.model.course.Course;
import ra.edu.utils.PageInfo;

import java.util.List;

public class CourseServiceImp implements CourseService{
    CourseDAOImp courseDAOImp = new CourseDAOImp();

    public CourseServiceImp() {
        this.courseDAOImp = courseDAOImp;
    }

    @Override
    public PageInfo<Course> getPageData(int page, int size) {
        List<Course> courses = courseDAOImp.getPageData(page, size);
        int totalRecords = courseDAOImp.getTotalCourses();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        PageInfo<Course> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(courses);
        return pageInfo;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public Object getById(int id) {
        return courseDAOImp.findById(id);
    }

    @Override
    public boolean save(Object o) {
        return courseDAOImp.save(o);
    }

    @Override
    public boolean update(Object o) {
        return courseDAOImp.update(o);
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
