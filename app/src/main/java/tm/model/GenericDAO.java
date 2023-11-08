package tm.model;

import java.util.List;

public interface GenericDAO<T> {
    
    List<T> getAll();
    // T get();
    boolean removeById(int id);
    boolean add(T entity);
}
