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
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          student_id INT,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role ENUM('admin', 'student'),
                          status ENUM('active', 'inactive', 'block'),
                          FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE SET NULL
);
# ALTER TABLE ACCOUNTS DROP FOREIGN KEY accounts_ibfk_1;
# ALTER TABLE ACCOUNTS ADD CONSTRAINT accounts_ibfk_1 FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE SET NULL;
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
    SELECT id,student_id, email, role, status
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
-- STUDENT
--
DELIMITER //

CREATE PROCEDURE AddNewStudent(
    IN p_name VARCHAR(100),
    IN p_dob DATE,
    IN p_email VARCHAR(100),
    IN p_sex TINYINT(1),
    IN p_phone VARCHAR(20),
    OUT p_rows_affected INT
)
BEGIN
    DECLARE v_password VARCHAR(255);
    DECLARE v_student_id INT;

    INSERT INTO student (name, dob, email, sex, phone, create_at)
    VALUES (p_name, p_dob, p_email, p_sex, p_phone, CURRENT_TIMESTAMP());


    SET p_rows_affected = ROW_COUNT();

    SET v_student_id = LAST_INSERT_ID();

    SET v_password = SUBSTRING(UUID(), 1, 8);

    INSERT INTO accounts (student_id, email, password, role, status)
    VALUES (v_student_id, p_email, v_password, 'student', 'active');

    SELECT
        p_email AS email,
        v_password AS password,
        'Account created successfully!' AS message;

END //

DELIMITER ;

-- phan trang
--
DELIMITER //

CREATE PROCEDURE GetStudentsPaginated(IN page INT, IN pageSize INT)
BEGIN
    DECLARE offsetValue INT;

    -- Tính toán giá trị offset dựa trên page và pageSize
    SET offsetValue = (page - 1) * pageSize;

    -- Truy vấn danh sách sinh viên với phân trang
    SELECT * FROM student
    ORDER BY id ASC
    LIMIT offsetValue, pageSize;
END //

DELIMITER ;

-- all
DELIMITER //

CREATE PROCEDURE GetAllStudents()
BEGIN
    SELECT id, name, dob, email, sex, phone, create_at
    FROM student;
END //

DELIMITER ;
-- delete
DELIMITER //

CREATE PROCEDURE deactivate_and_delete_student(IN p_student_id INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM student WHERE id = p_student_id) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Không tìm thấy sinh viên có ID này.';
    ELSE
        UPDATE accounts
        SET student_id = NULL,
            status = 'inactive'
        WHERE student_id = p_student_id;

        -- Xóa sinh viên
        DELETE FROM student WHERE id = p_student_id;
    END IF;
END //

DELIMITER ;

-- tim kiem
DELIMITER //

CREATE PROCEDURE SearchStudentsByKeyword(IN keyword VARCHAR(100))
BEGIN
    SELECT *
    FROM student
    WHERE name LIKE CONCAT('%', keyword, '%')
       OR email LIKE CONCAT('%', keyword, '%')
       OR CAST(id AS CHAR) = keyword;
END //

DELIMITER ;
-- update
DELIMITER $$

CREATE PROCEDURE UpdateStudent (
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_birthday DATE,
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_sex bit
)
BEGIN
    UPDATE student
    SET
        name = p_name,
        dob = p_birthday,
        email = p_email,
        phone = p_phone,
        sex = p_sex
    WHERE id = p_id;
END $$

DELIMITER ;
-- find id
DELIMITER //
CREATE PROCEDURE FindStudentById(IN studentId INT)
BEGIN
    SELECT * FROM student WHERE id = studentId;
END //
DELIMITER ;
--
DELIMITER $$
CREATE PROCEDURE GetTotalStudents()
BEGIN
    DECLARE total INT;

    -- Tính tổng số khóa học bằng cách đếm giá trị của cột id
    SELECT COUNT(id) INTO total FROM student;

    -- Trả về tổng số khóa học
    SELECT total AS total_students;
END$$
DELIMITER ;
-- sap xep
DELIMITER //

CREATE PROCEDURE GetStudentsSortedPaged(
    IN sort_column VARCHAR(20),
    IN sort_order VARCHAR(4),
    IN page INT,
    IN page_size INT
)
BEGIN
    SET @offset := (page - 1) * page_size;
    SET @limit := page_size;

    SET @sql = CONCAT(
            'SELECT * FROM student ',
            'ORDER BY ', sort_column, ' ', sort_order, ' ',
            'LIMIT ?, ?'
               );

    PREPARE stmt FROM @sql;
    EXECUTE stmt USING @offset, @limit;
    DEALLOCATE PREPARE stmt;
END
//
DELIMITER ;
-- enrollment
-- dang ki khoa hoc

DELIMITER //

CREATE PROCEDURE RegisterCourse(IN IN_student_id INT, IN IN_course_id INT)
BEGIN
    IF EXISTS (
        SELECT 1 FROM enrollment
        WHERE student_id = IN_student_id AND course_id = IN_course_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Sinh viên đã đăng ký khóa học này.';
    ELSE
        INSERT INTO enrollment(student_id, course_id, registered_at)
        VALUES (IN_student_id, IN_course_id, NOW());
    END IF;
END //

DELIMITER ;


-- kiem tra da dang ky
DELIMITER //

CREATE PROCEDURE IsCourseAlreadyRegistered(IN student_id INT, IN course_id INT)
BEGIN
    SELECT
        CASE
            WHEN EXISTS (
                SELECT 1 FROM enrollment
                WHERE student_id = student_id AND course_id = course_id
            )
                THEN 1 ELSE 0
            END AS is_registered;
END //

DELIMITER ;
-- lay danh sach khoa hoc da dang ky

DELIMITER //

CREATE PROCEDURE GetCoursesByStudent(IN student_id INT)
BEGIN
    SELECT * FROM enrollment
    WHERE student_id = student_id AND status = 'waiting';
END //

DELIMITER ;

-- kiem tra khoa hoc da dang ky
DELIMITER //
CREATE PROCEDURE CheckCourseRegistration(IN student_id INT, IN course_id INT)
BEGIN
    SELECT COUNT(*) FROM enrollment
    WHERE student_id = student_id AND course_id = course_id;
END
//
DELIMITER ;
-- Đăng ký khóa học cho sinh viên
DELIMITER //

CREATE PROCEDURE RegisterCourseForStudent(
    IN student_id_in INT,
    IN course_id_in INT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM enrollment
        WHERE student_id = student_id_in AND course_id = course_id_in
    ) THEN
        INSERT INTO enrollment (student_id, course_id, registered_at)
        VALUES (student_id_in, course_id_in, NOW());
    ELSE
        -- Nếu đã đăng ký thì ném lỗi
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Sinh viên đã đăng ký khóa học này.';
    END IF;
END
//
DELIMITER ;

-- xem khoa hoc da dang ky
DELIMITER //
CREATE PROCEDURE GetCoursesForStudent(IN student_id_in INT)
BEGIN
    SELECT c.id, c.name, e.registered_at
    FROM enrollment e
             JOIN course c ON e.course_id = c.id
    WHERE e.student_id = student_id_in
    ORDER BY e.registered_at DESC;
END
//
DELIMITER ;

-- huy dang ki

DELIMITER $$

CREATE PROCEDURE CancelEnrollment(
    IN studentId INT,
    IN courseId INT,
    OUT isSuccess BOOLEAN
)
BEGIN
    DECLARE currentStatus ENUM('waiting', 'confirmed', 'completed', 'cancel');
    SET isSuccess = FALSE;

    IF EXISTS (
        SELECT 1 FROM enrollment
        WHERE student_id = studentId AND course_id = courseId
    ) THEN

        SELECT status INTO currentStatus
        FROM enrollment
        WHERE student_id = studentId AND course_id = courseId;

        IF currentStatus = 'waiting' THEN
            UPDATE enrollment
            SET status = 'cancel'
            WHERE student_id = studentId AND course_id = courseId;

            SET isSuccess = TRUE;
        END IF;

    END IF;
END $$

DELIMITER ;


--
DELIMITER //

CREATE PROCEDURE GetEnrollmentByStudentAndCourse(IN student_id INT, IN course_id INT)
BEGIN
    SELECT * FROM enrollment
    WHERE student_id = student_id AND course_id = course_id;
END //

DELIMITER ;
-- sap xep

DELIMITER $$

CREATE PROCEDURE GetEnrollmentsSortedPaged(
    IN sortColumn VARCHAR(255),
    IN sortOrder VARCHAR(4),
    IN page INT,
    IN pageSize INT
)
BEGIN
    SET @startRow = (page - 1) * pageSize;

    SET @sql = CONCAT('SELECT e.id, e.student_id, e.course_id, e.registered_at, e.status, c.name AS course_name
                       FROM Enrollment e
                       INNER JOIN Course c ON e.course_id = c.id
                       ORDER BY ', sortColumn, ' ', sortOrder, '
                       LIMIT ', @startRow, ', ', pageSize);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$

DELIMITER ;

-- tong
DELIMITER $$
CREATE PROCEDURE GetTotalEnrollments()
BEGIN
    DECLARE total INT;

    SELECT COUNT(id) INTO total FROM enrollment;

    -- Trả về tổng số khóa học
    SELECT total AS total_enrollments;
END$$
DELIMITER ;
-- doi mat khau
-- Procedure đổi mật khẩu
DELIMITER //
CREATE PROCEDURE sp_changePassword(
    IN p_email VARCHAR(50),
    IN p_old_password VARCHAR(255),
    IN p_new_password VARCHAR(255),
    OUT p_status BOOLEAN
)
BEGIN
    DECLARE v_count INT;

    SELECT COUNT(*) INTO v_count
    FROM accounts
    WHERE email = p_email AND password = p_old_password;

    IF v_count > 0 THEN
        IF p_old_password = p_new_password THEN
            SET p_status = FALSE;
        ELSE
            UPDATE accounts
            SET password = p_new_password
            WHERE email = p_email;

            SET p_status = TRUE;
        END IF;
    ELSE
        SET p_status = FALSE;
    END IF;
END //
DELIMITER ;

--
DELIMITER $$

CREATE PROCEDURE CheckOldPassword(IN studentEmail VARCHAR(255), IN oldPassword VARCHAR(255))
BEGIN
    DECLARE dbPassword VARCHAR(255);

    SELECT password INTO dbPassword
    FROM accounts
    WHERE email = studentEmail;

    IF dbPassword = oldPassword THEN
        SELECT 'SUCCESS' AS result;
    ELSE
        SELECT 'FAIL' AS result;
    END IF;
END $$

DELIMITER ;

-- ADMIN ( QUAN LI DANG KY KHOA HOC )
-- lay danh sach dang ky
DELIMITER //

CREATE PROCEDURE get_students_by_course_paginated(
    IN p_course_id INT,
    IN p_page INT,
    IN p_page_size INT
)
BEGIN
    DECLARE offset_val INT;
    SET offset_val = (p_page - 1) * p_page_size;

    SELECT
        s.id AS student_id,
        s.name AS student_name,
        s.email AS student_email,
        e.registered_at,
        e.status
    FROM enrollment e
             JOIN student s ON e.student_id = s.id
    WHERE e.course_id = p_course_id
    ORDER BY e.registered_at DESC
    LIMIT p_page_size OFFSET offset_val;
END //

DELIMITER ;









