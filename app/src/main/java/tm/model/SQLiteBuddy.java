package tm.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLiteBuddy {
    private String url;
    private Connection connection;

    public SQLiteBuddy(
        String url
    )
    {
        this.url = "jdbc:sqlite:" + url;
    }

    public SQLiteBuddy() {}

    public Connection establishConnection()
    {
        try {
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Connection establishConnection(String url)
    {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + url);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeDatabase()
    {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            // no connection to close, hehe
        }
    }

    public void setUrl(String url) {
        this.url = "jdbc:sqlite:" + url;
    }

    public void getPragma() throws SQLException {
        Connection connection = establishConnection();
        
        try {
            PreparedStatement prepStmt = connection.prepareStatement("SELECT sql FROM sqlite_master WHERE type='table'");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next())
                System.out.println(rs.getString(1)); // Use index 1 to retrieve the SQL statement
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public boolean isAcceptedDatabase2() throws SQLException {
        Connection connection = establishConnection();
    
    try {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, "Students", null);

        boolean isValid = true;

        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("TYPE_NAME");
            //TODO:check for nullability && uniquenesS?

            System.out.println("Column name: " + columnName + "\n" + "Data type: " + dataType);

            if ((columnName.equals("firstname") && dataType.equals("TEXT")) ||
                (columnName.equals("surname") && dataType.equals("TEXT")) ||
                (columnName.equals("matrikelnr") && dataType.equals("INTEGER")) ||
                (columnName.equals("fhkennung") && dataType.equals("TEXT"))) {
                
            } else {
                
                isValid = false;
                break;
            }
        }

        return isValid;

    } catch (SQLException e) {
        System.out.println("HIER");
        e.printStackTrace();
    } finally {
        closeDatabase();
    }
    return false;
    }
    
    public boolean isAcceptedDatabase() throws SQLException {
        Connection connection = establishConnection();
        boolean isValid;
    
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, "Students", null);
    
        isValid = true;
    
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("TYPE_NAME");
            // TODO: Check for nullability && uniqueness?
    
            // System.out.println("Column name: " + columnName + "\n" + "Data type: " + dataType);
    
            if ((columnName.equals("firstname") && dataType.equals("TEXT")) ||
                (columnName.equals("surname") && dataType.equals("TEXT")) ||
                (columnName.equals("matrikelnr") && dataType.equals("INTEGER")) ||
                (columnName.equals("fhkennung") && dataType.equals("TEXT"))) {

            } else {
                isValid = false;
                break;
            }
        }
    
        closeDatabase();
        return isValid;
    }
}
