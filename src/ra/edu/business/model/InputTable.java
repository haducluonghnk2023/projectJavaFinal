package ra.edu.business.model;

import ra.edu.business.model.course.Course;

import java.util.List;
import java.util.Scanner;

public interface InputTable<T> {
    void inputData(Scanner sc, List<T> list);
}
