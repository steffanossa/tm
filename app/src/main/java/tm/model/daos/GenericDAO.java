package tm.model.daos;

import java.util.ArrayList;


public interface GenericDAO<T>
{    
    ArrayList<T> getAll();
    // T get();
    boolean removeById(int id);
    boolean add(T entity);
}
