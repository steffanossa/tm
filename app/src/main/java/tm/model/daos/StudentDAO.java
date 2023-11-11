package tm.model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tm.model.SQLiteBuddy;
import tm.model.classes.Student;

public class StudentDAO implements GenericDAO<Student> 
{
    //Data Access Object
    private SQLiteBuddy sqLiteBuddy;

    public StudentDAO(
        SQLiteBuddy sqLiteBuddy
    ) {
        this.sqLiteBuddy = sqLiteBuddy;
    }
    
            /**
     * Retrieves all Students from the database
     * @return A list of Students
     */
    @Override
    public ArrayList<Student> getAll()
    {
        Connection connection = sqLiteBuddy.establishConnection();
        ArrayList<Student> students = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM Students");
            while (resultSet.next()) {
                String first = resultSet.getString("firstname");
                String last = resultSet.getString("surname");
                int matNr = resultSet.getInt("matrikelnr");
                String fhK = resultSet.getString("fhkennung");
                students.add(new Student(matNr, first, last, fhK));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // return null;
        } finally {
            this.sqLiteBuddy.closeDatabase();
        }
        return students;
    }

    @Override
    public boolean add(Student student) throws SQLException
    {
        boolean wasSuccessful = false;
        Connection connection = sqLiteBuddy.establishConnection();
        try {
            PreparedStatement prepStmt = connection.prepareStatement(
                "INSERT INTO Students (firstname, surname, matrikelnr, fhkennung)" +
                "VALUES (?, ?, ?, ?)");
            prepStmt.setString(1, student.getFirstname());
            prepStmt.setString(2,student.getSurname());
            prepStmt.setInt(3, student.getMatrikelnummer());
            prepStmt.setString(4, student.getFhKennung());
            if (prepStmt.executeUpdate() == 1)
                wasSuccessful = true;
        } catch (SQLException e) {
            // e.printStackTrace();
            //TODO
            throw e;
        } finally {
            this.sqLiteBuddy.closeDatabase();
        }
        return wasSuccessful;
    }

    @Override
    public boolean removeById(int id)
    {
        boolean wasSuccessful = false;
        Connection connection = sqLiteBuddy.establishConnection();
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM Students WHERE matrikelnr=?");
            prepStmt.setInt(1, id);
            if (prepStmt.executeUpdate() == 1) {
                wasSuccessful = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.sqLiteBuddy.closeDatabase();
        }
        return wasSuccessful;
    }
}
