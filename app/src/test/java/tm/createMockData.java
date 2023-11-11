package tm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class createMockData {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:mock.db";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            String createStatement = "CREATE TABLE \"Students\" (" +
            "\"firstname\" TEXT NOT NULL, " +
            "\"surname\" TEXT NOT NULL, " +
            "\"matrikelnr\" INTEGER NOT NULL UNIQUE, " +
            "\"fhkennung\" TEXT NOT NULL UNIQUE, " +
            "PRIMARY KEY(\"matrikelnr\"))";
        

        statement.execute(createStatement);

            // statement.execute(
            //     "CREATE TABLE Students (" +
            //     "matrikelnr INTEGER PRIMARY KEY UNIQUE, " +
            //     "firstname TEXT NOT NULL, " +
            //     "surname TEXT NOT NULL, " +
            //     "fhkennung TEXT UNIQUE NOT NULL)");

            statement.execute(
                "INSERT INTO Students (firstname, surname, matrikelnr, fhkennung)" +
                "VALUES ('Alfred', 'Tetzlaf', 222222, 'at222222')");

            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }
}
