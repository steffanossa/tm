package tm.model;

import java.sql.Connection;

public interface GenericDatabaseBuddy {
    Connection establishConnection();
    void closeConnection();
    boolean isAcceptedDatabase();
    void setUrl(String url);
}
