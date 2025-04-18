package ra.edu.business.model.course;

import ra.edu.business.model.InputTable;
import ra.edu.validate.course.CourseValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Course implements InputTable<Course> {
    @Override
    public void inputData(Scanner sc, List<Course> courseList) {
        // Nhập tên khóa học (có kiểm tra trùng tên và chuẩn hóa)
        this.name = CourseValidator.validateCourseName(sc, courseList);

        // Nhập thời lượng khóa học
        this.duration = CourseValidator.validateDuration(sc);

        // Nhập tên giảng viên
        this.instructor = CourseValidator.validateInstructor(sc);

        // Ghi thời điểm tạo
        this.createAt = LocalDateTime.now();
    }


    private int id;
    private String name;
    private int duration;
    private String instructor;
    private LocalDateTime createAt;

    public Course() {
    }

    // Constructor đầy đủ (dùng khi lấy từ DB)
    public Course(int id, String name, int duration, String instructor, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.instructor = instructor;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("| %-5s | %-40s | %-10s | %-15s | %-12s |",
                id,
                name,
                duration + " buổi",
                instructor,
                createAt.format(formatter)
        );
    }
}
