package tm.model.daos;

import java.util.ArrayList;

import java.sql.SQLException;

public interface GenericDAO<T>
{    
    ArrayList<T> getAll() throws SQLException;
    // T get();
    boolean removeById(int id) throws SQLException;
    boolean add(T entity) throws SQLException;
}
