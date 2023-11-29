package tm.model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tm.model.SQLiteBuddy;
import tm.model.dtos.StudentDTO;


/**
 * Data access object
 */
public class StudentDAO implements GenericDAO<StudentDTO> 
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
    public ArrayList<StudentDTO> getAll() throws SQLException
    {
        Connection connection = sqLiteBuddy.establishConnection();
        ArrayList<StudentDTO> students = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM Students");
            while (resultSet.next()) {
                String firstname = resultSet.getString("first_name");
                String surname = resultSet.getString("surname");
                int matNr = resultSet.getInt("matriculation_number");
                String fhK = resultSet.getString("fh_identifier");
                students.add(new StudentDTO(firstname, surname, matNr, fhK));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // return null;
        } finally {
            this.sqLiteBuddy.closeConnection();
        }
        return students;
    }

    /**
     * Adds a Student object to the database
     * @return {@code true} if successful
     * @throws SQLException
     */
    @Override
    public boolean add(StudentDTO student) throws SQLException
    {
        boolean wasSuccessful = false;
        Connection connection = sqLiteBuddy.establishConnection();
        try {
            PreparedStatement prepStmt = connection.prepareStatement(
                "INSERT INTO Students (first_name, surname, matriculation_number, fh_identifier)" +
                "VALUES (?, ?, ?, ?)"
                );
            prepStmt.setString(1, student.getFirstName());
            prepStmt.setString(2,student.getSurname());
            prepStmt.setInt(3, student.getMatriculationNumber());
            prepStmt.setString(4, student.getFhIdentifier());
            if (prepStmt.executeUpdate() == 1)
                wasSuccessful = true;
        } catch (SQLException e) {
            throw e;
        } finally {
            this.sqLiteBuddy.closeConnection();
        }
        return wasSuccessful;
    }

    /**
     * Removes an element from the database based on the id provided
     * @return {@code true} if successful
     */
    @Override
    public boolean removeById(int id) throws SQLException
    {
        boolean wasSuccessful = false;
        Connection connection = sqLiteBuddy.establishConnection();
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM Students WHERE matriculation_number=?");
            prepStmt.setInt(1, id);
            if (prepStmt.executeUpdate() == 1) {
                wasSuccessful = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.sqLiteBuddy.closeConnection();
        }
        return wasSuccessful;
    }
}
