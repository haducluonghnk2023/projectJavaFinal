package ra.edu.business.dao;

import java.util.List;

public interface AppDAO<T> {
    List<T> findAll();
    Object findById(int id);
    boolean save(T t);
    boolean update(T t);
    boolean delete(int id);
}
