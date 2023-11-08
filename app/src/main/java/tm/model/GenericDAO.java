package tm.model;

import javafx.collections.ObservableList;

public interface GenericDAO<T> {
    
    ObservableList<T> getAll();
    // T get();
    boolean removeById(int id);
    boolean add(T entity);
}
