package tm.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLite helper class used to establish and close a database, verify its structure
 */
public class SQLiteBuddy implements GenericDatabaseBuddy
{
    private String url;
    private Connection connection;

    public SQLiteBuddy(String url) { this.url = "jdbc:sqlite:" + url; }

    public SQLiteBuddy() {}

    /**
     * Establishes a connection to the database specified _earlier_
     * @return A connection object
     */
    @Override
    public Connection establishConnection()
    {
        try { connection = DriverManager.getConnection(url); }
        catch (SQLException e) { e.printStackTrace(); }
        return connection;
    }

    /**
     * Establishes a connection to the database at the url provided 
     * @param url Path to the database to be connected to
     * @return A connection object
     */
    public Connection establishConnection(String url)
    {
        try { connection = DriverManager.getConnection("jdbc:sqlite:" + url); }
        catch (SQLException e) { e.printStackTrace(); }
        return connection;
    }

    /**
     * Closes the connection to the database
     */
    @Override
    public void closeConnection()
    {
        if (connection != null)
        {
            try { connection.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void setUrl(String url) {
        this.url = "jdbc:sqlite:" + url;
    }

    /**
     * Verifies whether connection is of correct structure to be handled. TODO: macht alles wird
     * @return {@code true} if successful
     * @throws SQLException
     */
    @Override
    public boolean isAcceptedDatabase()
    {
        Connection connection = establishConnection();
        boolean isValid = false;

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, null, null);
        
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
        
            closeConnection();
        }
        catch (SQLException e) {}

        return isValid;
    }
}
