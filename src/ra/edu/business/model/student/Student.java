package ra.edu.business.model.student;

import ra.edu.business.model.InputTable;
import ra.edu.business.model.account.Account;
import ra.edu.validate.student.StudentValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Student implements InputTable<Student> {
    private int id;
    private String name;
    private LocalDate birthday;
    private String email;
    private boolean status;
    private String phone;
    private String created_at;
    private String generatedPassword;

    public Student() {

    }

    public Student(String name, LocalDate birthday, String email, boolean status, String phone, String created_at) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.status = status;
        this.phone = phone;
        this.created_at = created_at;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

    public void inputDataS(Scanner sc, List<Student> studentList, Object obj) {
        Student currentStudent = (obj instanceof Student) ? (Student) obj : null;

        this.name = StudentValidator.validateName("Nhập họ tên: ", sc);
        this.email = StudentValidator.validateEmail("Nhập email sinh viên: ", sc, studentList, currentStudent);
        this.birthday = StudentValidator.validateDob("Nhập ngày sinh sinh viên: ", sc);
        this.phone = StudentValidator.validatePhone("Nhập số điện thoại sinh viên: ", sc, studentList);
        this.status = StudentValidator.validateSex("Nhập giới tính sinh viên (true/false): ", sc);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.created_at = now.format(formatter);
    }

    @Override
    public void inputData(Scanner sc, List<Student> studentList) {

    }

    public void inputDataStudent(Scanner sc, List<Student> studentList, List<Account> accountList) {
        this.name = StudentValidator.validateName("Nhập họ tên: ", sc);
        this.email = StudentValidator.validateEmailA("Nhập email sinh viên: ", sc, accountList);
        this.birthday = StudentValidator.validateDob("Nhập ngày sinh sinh viên: ", sc);
        this.phone = StudentValidator.validatePhone("Nhập số điện thoại sinh viên: ", sc, studentList);
        this.status = StudentValidator.validateSex("Nhập giới tính sinh viên (true/false): ", sc);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.created_at = now.format(formatter);
    }

}
