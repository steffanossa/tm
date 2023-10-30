package tm.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class StudentDAO {
    //Data Access Object
    private SQLiteBuddy sqLiteBuddy;

    public StudentDAO(
        SQLiteBuddy sqLiteBuddy
    ) {
        this.sqLiteBuddy = sqLiteBuddy;
    }
    
    public ObservableList<Student> getAllStudents()
    {
        // Connection connection = SQLiteHelper.establishConnection();
        Connection connection = sqLiteBuddy.establishConnection();
        ObservableList<Student> students = FXCollections.observableArrayList();
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
            return null;
        } finally {
            // SQLiteHelper.closeDatabase();
            this.sqLiteBuddy.closeDatabase();
        }
        return students;
    }

    public boolean addStudent(Student student)
    {
        boolean wasSuccessful = false;
        // Connection connection = SQLiteHelper.establishConnection();
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
            e.printStackTrace();
        } finally {
            // SQLiteHelper.closeDatabase();
            this.sqLiteBuddy.closeDatabase();
        }
        return wasSuccessful;
    }

    public boolean removeStudentByMatrikelnummer(int matrikelnummer)
    {
        boolean wasSuccessful = false;
        // Connection connection = SQLiteHelper.establishConnection();
        Connection connection = sqLiteBuddy.establishConnection();
        try {
            PreparedStatement prepStmt = connection.prepareStatement("DELETE FROM Students WHERE matrikelnr=?");
            prepStmt.setInt(1, matrikelnummer);
            if (prepStmt.executeUpdate() == 1) {
                wasSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // SQLiteHelper.closeDatabase();
            this.sqLiteBuddy.closeDatabase();
        }
        return wasSuccessful;
    }

    public boolean updateStudent(Student student)
    {
        //TODO: ja, schreib halt
        return false;
    }
}
