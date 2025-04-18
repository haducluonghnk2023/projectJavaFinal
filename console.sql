use project_java;

-- Bảng student
CREATE TABLE student (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100) NOT NULL,
     dob DATE NOT NULL,
     email VARCHAR(100) NOT NULL UNIQUE,
     sex BIT NOT NULL,
     phone VARCHAR(20),
     password VARCHAR(255) NOT NULL,
     create_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bảng course
CREATE TABLE course (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(100) NOT NULL,
     duration INT NOT NULL,
     instructor VARCHAR(100) NOT NULL,
     create_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Bảng đăng ký khóa học
CREATE TABLE enrollment (
     id INT PRIMARY KEY AUTO_INCREMENT,
     student_id INT NOT NULL,
     course_id INT NOT NULL,
     registered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
     status ENUM('WAITING','DENIED','CANCEL','CONFIRM') DEFAULT 'WAITING',
     FOREIGN KEY (student_id) REFERENCES student(id) ,
     FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE ACCOUNTS (
     id int ,
     student_id int,
     email varchar(100) not null unique,
     password VARCHAR(255) NOT NULL,
     role enum('admin','student'),
     status enum('active','inactive','block'),
     foreign key (student_id) references student(id)
);


INSERT INTO course (name, duration, instructor)
VALUES
    ('Mathematics 101', 30, 'Dr. John Doe'),
    ('Computer Science 101', 40, 'Dr. Alice Smith'),
    ('Physics 101', 35, 'Prof. Michael Johnson'),
    ('Chemistry 101', 40, 'Dr. Sarah Williams'),
    ('Biology 101', 25, 'Prof. David Brown'),
    ('History 101', 20, 'Dr. Emily Clark'),
    ('Literature 101', 30, 'Dr. William Harris'),
    ('Philosophy 101', 35, 'Prof. James Lewis'),
    ('Economics 101', 40, 'Dr. Patricia Walker'),
    ('Psychology 101', 25, 'Dr. Thomas Green'),
    ('Art History 101', 30, 'Prof. Charles Hall'),
    ('Sociology 101', 35, 'Dr. Elizabeth Allen'),
    ('Political Science 101', 30, 'Dr. Richard Young'),
    ('Engineering 101', 45, 'Prof. Linda King'),
    ('Business Administration 101', 40, 'Dr. Paul Wright'),
    ('Music 101', 25, 'Dr. Jennifer Scott'),
    ('Statistics 101', 30, 'Prof. Kevin Adams'),
    ('Mechanical Engineering 101', 50, 'Dr. Steven Evans'),
    ('Electrical Engineering 101', 45, 'Prof. Karen Turner'),
    ('Civil Engineering 101', 50, 'Dr. Robert Carter');

DELIMITER //

CREATE PROCEDURE account_login(
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT id, email, role, status
    FROM ACCOUNTS
    WHERE email = p_email AND password = p_password;
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
    ORDER BY id ASC
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
 -- xoa

DELIMITER //

CREATE PROCEDURE DeleteCourseById(IN p_course_id INT)
BEGIN
    DECLARE course_exists INT;
    DECLARE enrollment_count INT;

    -- Kiểm tra xem khóa học có tồn tại không
    SELECT COUNT(*) INTO course_exists
    FROM course
    WHERE id = p_course_id;

    IF course_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Khóa học không tồn tại';
    END IF;

    -- Kiểm tra xem khóa học có học viên không
    SELECT COUNT(*) INTO enrollment_count
    FROM enrollment
    WHERE course_id = p_course_id;

    IF enrollment_count > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Không thể xóa khóa học vì có học viên đang đăng ký';
    ELSE
        DELETE FROM course WHERE id = p_course_id;
    END IF;
END //

DELIMITER ;
-- tim kiem
DELIMITER //
CREATE PROCEDURE SearchCoursesByName(IN keyword VARCHAR(100))
BEGIN
    SELECT * FROM course
    WHERE name LIKE CONCAT('%', keyword, '%');
END
//
DELIMITER ;
-- sap xep
DELIMITER //

CREATE PROCEDURE GetCoursesSortedPaged(
    IN sort_column VARCHAR(20),
    IN sort_order VARCHAR(4),
    IN page INT,
    IN page_size INT
)
BEGIN
    SET @offset := (page - 1) * page_size;
    SET @limit := page_size;

    SET @sql = CONCAT(
            'SELECT * FROM course ',
            'ORDER BY ', sort_column, ' ', sort_order, ' ',
            'LIMIT ?, ?'
               );

    PREPARE stmt FROM @sql;
    EXECUTE stmt USING @offset, @limit;
    DEALLOCATE PREPARE stmt;
END
//
DELIMITER ;

-- all
DELIMITER $$

CREATE PROCEDURE GetAllCourses()
BEGIN
    SELECT id, name, duration, instructor, create_at
    FROM course;
END $$

DELIMITER ;





