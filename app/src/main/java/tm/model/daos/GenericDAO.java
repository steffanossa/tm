package tm.model.daos;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    
    List<T> getAll() throws SQLException;
    // T get();
    boolean removeById(int id) throws SQLException;
    boolean add(T entity) throws SQLException;
}
