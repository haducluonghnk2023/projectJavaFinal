package ra.edu.business.service;

import java.util.List;

public interface AppService<T> {
    List<T> getAll();
    T getById(int id);
    boolean save(T t);
    boolean update(T t);
    boolean delete(int id);
}
