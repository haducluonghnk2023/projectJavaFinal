use project_java;

-- PROCEDURE: Kiểm tra thông tin đăng nhập student
DELIMITER //
CREATE PROCEDURE student_login(
    IN p_name VARCHAR(100),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT * FROM student
    WHERE name = p_name AND password = p_password;
END //
DELIMITER ;

-- PROCEDURE: Kiểm tra thông tin đăng nhập admin
DELIMITER //
CREATE PROCEDURE admin_login(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT * FROM admin
    WHERE username = p_username AND password = p_password;
END //
DELIMITER ;

-- phan trang khoa học
DELIMITER $$

CREATE PROCEDURE GetCoursesPaginated(IN page INT, IN pageSize INT)
BEGIN
    DECLARE offset_value INT;

    -- Tính toán offset dựa trên page và pageSize
    SET offset_value = (page - 1) * pageSize;

    -- Truy vấn danh sách khóa học với phân trang
    SELECT * FROM course
    ORDER BY create_at DESC
    LIMIT offset_value, pageSize;
END$$

DELIMITER ;

-- them moi khoa hoc
DELIMITER $$

CREATE PROCEDURE AddCourse(IN course_name VARCHAR(100), IN course_duration INT, IN course_instructor VARCHAR(100))
BEGIN
    -- Thêm khóa học mới vào bảng course
    INSERT INTO course (name, duration, instructor)
    VALUES (course_name, course_duration, course_instructor);
END$$

DELIMITER ;

-- cap nhat khoa hoc
DELIMITER $$

CREATE PROCEDURE UpdateCourse(IN course_id INT, IN course_name VARCHAR(100), IN course_duration INT, IN course_instructor VARCHAR(100))
BEGIN
    -- Cập nhật thông tin khóa học theo ID
    UPDATE course
    SET name = course_name,
        duration = course_duration,
        instructor = course_instructor
    WHERE id = course_id;
END$$

DELIMITER ;

-- tong
DELIMITER $$
CREATE PROCEDURE GetTotalCourses()
BEGIN
    DECLARE total INT;

    -- Tính tổng số khóa học bằng cách đếm giá trị của cột id
    SELECT COUNT(id) INTO total FROM course;

    -- Trả về tổng số khóa học
    SELECT total AS total_courses;
END$$
DELIMITER ;
-- lay theo id
DELIMITER //
CREATE PROCEDURE FindCourseById(IN courseId INT)
BEGIN
    SELECT * FROM course WHERE id = courseId;
END //
DELIMITER ;


