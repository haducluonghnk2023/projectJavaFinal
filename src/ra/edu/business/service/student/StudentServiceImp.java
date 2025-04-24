package ra.edu.business.service.student;

import ra.edu.business.dao.student.StudentDAOImp;
import ra.edu.business.model.course.Course;
import ra.edu.business.model.enrollment.Enrollment;
import ra.edu.business.model.student.Student;
import ra.edu.utils.PageInfo;

import java.util.List;

public class StudentServiceImp implements  StudentService {
    StudentDAOImp studentDAOImp = new StudentDAOImp();

    public StudentServiceImp() {
        this.studentDAOImp = studentDAOImp;
    }

    @Override
    public PageInfo<Student> getPageData(int page, int size) {
        List<Student> students = studentDAOImp.getPageData(page, size);
        int totalRecords = studentDAOImp.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        PageInfo<Student> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(students);
        return pageInfo;
    }

    @Override
    public List<Student> getAll() {
        return studentDAOImp.findAll();
    }

    @Override
    public Student getById(int id) {
        return (Student) studentDAOImp.findById(id);
    }

    @Override
    public boolean save(Student student) {
        return studentDAOImp.save(student);
    }

    @Override
    public boolean update(Student student) {
        return studentDAOImp.update(student);
    }

    @Override
    public boolean delete(int id) {
        return studentDAOImp.delete(id);
    }

    @Override
    public PageInfo<Student> searchByKeyword(String keyword, int page, int size) {
        // Lấy danh sách học viên khớp với từ khóa từ DAO
        List<Student> matchedStudents = studentDAOImp.searchByKeyword(keyword);

        // Tổng số bản ghi tìm được
        int totalRecords = matchedStudents.size();

        // Tính chỉ số bắt đầu và kết thúc của trang hiện tại
        int fromIndex = Math.min((page - 1) * size, totalRecords);
        int toIndex = Math.min(page * size, totalRecords);

        // Lấy danh sách học viên theo trang
        List<Student> pageData = matchedStudents.subList(fromIndex, toIndex);

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Đóng gói dữ liệu vào PageInfo
        PageInfo<Student> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(pageData);

        return pageInfo;
    }

    @Override
    public PageInfo<Student> getSortedPagedData(String sortColumn, String sortOrder, int page, int size) {
        List<Student> student = studentDAOImp.getSortedPaged(sortColumn, sortOrder, page, size);
        int totalRecords = studentDAOImp.getTotalRecords();

        int totalPages = (int) Math.ceil((double) totalRecords / size);
        PageInfo<Student> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(student);

        return pageInfo;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        return studentDAOImp.changePassword(email,oldPassword,newPassword);
    }

    @Override
    public Boolean checkOldPassword(String email, String oldPassword) {
        String result = studentDAOImp.checkOldPassword(email, oldPassword);
        return "SUCCESS".equals(result);
    }

    @Override
    public PageInfo<Enrollment> getPagedEnrollmentsByStudentId(int studentId, int page, int pageSize) {
        return studentDAOImp.getPagedEnrollmentsByStudentId(studentId, page, pageSize);
    }

}
