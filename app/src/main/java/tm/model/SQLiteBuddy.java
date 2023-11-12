package tm.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLiteBuddy
{
    private String url;
    private Connection connection;

    public SQLiteBuddy(String url) { this.url = "jdbc:sqlite:" + url; }

    public SQLiteBuddy() {}

    public Connection establishConnection()
    {
        try { connection = DriverManager.getConnection(url); }
        catch (SQLException e) { e.printStackTrace(); }
        return connection;
    }

    public Connection establishConnection(String url)
    {
        try { connection = DriverManager.getConnection("jdbc:sqlite:" + url); }
        catch (SQLException e) { e.printStackTrace(); }
        return connection;
    }

    public void closeDatabase()
    {
        if (connection != null)
        {
            try { connection.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void setUrl(String url) {
        this.url = "jdbc:sqlite:" + url;
    }
    
    public boolean isAcceptedDatabase() throws SQLException
    {
        Connection connection = establishConnection();
        boolean isValid;
    
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, null, null);

        isValid = false;
    
        while (rs.next())
        {
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("TYPE_NAME");
    
            if ((columnName.equals("first_name") && dataType.equals("TEXT")) ||
                (columnName.equals("surname") && dataType.equals("TEXT")) ||
                (columnName.equals("matriculation_number") && dataType.equals("INTEGER")) ||
                (columnName.equals("fh_identifier") && dataType.equals("TEXT"))
            ) {
                isValid = true;
            }
            else
            {
                isValid = false;
                break;
            }
        }
    
        closeDatabase();
        return isValid;
    }
}
