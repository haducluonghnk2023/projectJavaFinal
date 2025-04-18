package ra.edu.business.model.student;

import ra.edu.business.model.InputTable;
import ra.edu.business.model.course.Course;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Student implements InputTable<Student> {
    private int id;
    private String name;
    private Date birthday;
    private String email;
    private boolean status;
    private String phone;
    private String password;
    private String created_at;

    public Student() {

    }

    public Student(String name, Date birthday, String email, boolean status, String phone, String password, String created_at) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.status = status;
        this.phone = phone;
        this.password = password;
        this.created_at = created_at;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                ", password='" + password + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }


    @Override
    public void inputData(Scanner sc, List<Student> list) {

    }
}
