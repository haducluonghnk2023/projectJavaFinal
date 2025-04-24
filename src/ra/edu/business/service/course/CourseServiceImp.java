package ra.edu.business.service.course;

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
    public PageInfo<Course> searchByName(String keyword, int page, int size) {
        // Lấy tất cả các khóa học khớp với từ khóa
        List<Course> matchedCourses = courseDAOImp.searchByName(keyword);

        // Tính tổng số kết quả tìm được
        int totalRecords = matchedCourses.size();

        // Tính vị trí bắt đầu và kết thúc cho phân trang
        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(page * size, totalRecords);

        // Lấy danh sách dữ liệu theo trang
        List<Course> pageData = matchedCourses.subList(fromIndex, toIndex);

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Đóng gói vào PageInfo
        PageInfo<Course> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(pageData);

        return pageInfo;
    }


    @Override
    public List getAll() {
        return courseDAOImp.findAll();
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
    public boolean delete(int id) {
        return courseDAOImp.delete(id);
    }

    @Override
    public PageInfo<Course> getSortedPagedData(String sortColumn, String sortOrder, int page, int size) {
        List<Course> courses = courseDAOImp.getSortedPaged(sortColumn, sortOrder, page, size);
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
    public boolean isCourseRegisteredByStudent(int studentId, int courseId) {
        return courseDAOImp.isCourseRegisteredByStudent(studentId, courseId);
    }

    @Override
    public boolean registerCourseForStudent(int studentId, int courseId) {
        return courseDAOImp.registerCourseForStudent(studentId, courseId);
    }

    @Override
    public String getCourseNameById(int courseId) {
        Course course =(Course) courseDAOImp.findById(courseId);
        if (course != null) {
            return course.getName();  // Giả sử khóa học có thuộc tính "name"
        }
        return "Unknown Course";
    }

}
