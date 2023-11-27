package tm;

/**
 * does not make any sense in parts
 */

import org.junit.jupiter.api.Test;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.junit.platform.commons.logging.Logger;
import org.sqlite.SQLiteException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.ArrayList;

import tm.model.SQLiteBuddy;
import tm.model.classes.Student;
import tm.model.daos.StudentDAO;

public class StudentDAOTest {

    // mock db w 10 entries
    private static final String DATABASE_PATH = "src/test/resources/test_students.db";
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    private final Student student = new Student(
        "Barbara",
        "Salesch",
        111119,
        "bs111111");

    private SQLiteBuddy sqLiteBuddy = new SQLiteBuddy(DATABASE_PATH);
    private StudentDAO studentDAO = new StudentDAO(sqLiteBuddy);

    public void removeBarbara() {
        try {
            Connection connection = sqLiteBuddy.establishConnection();
            statement = connection.createStatement();
            String id = String.valueOf(student.getMatriculationNumber());
            statement.execute("DELETE FROM Students WHERE matriculation_number=" + id);
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addBarbara() {
        try {
            Connection connection = sqLiteBuddy.establishConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO Students (first_name, surname, matriculation_number, fh_identifier) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getSurname());
            preparedStatement.setInt(3, student.getMatriculationNumber());
            preparedStatement.setString(4, student.getFhIdentifier());

            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllStudents() {
        ArrayList<Student> allStudents = studentDAO.getAll();
        assertEquals(10, allStudents.size());
    }

    @Test
    public void testAddStudent() {
        int numberOfStudentsBefore = studentDAO.getAll().size();
        System.out.println(numberOfStudentsBefore);
        studentDAO.add(student);
        int numberOfStudentsAfter = studentDAO.getAll().size();
        assertEquals(numberOfStudentsAfter, numberOfStudentsBefore + 1);
        removeBarbara();
    }
    
    @Test
    public void testAddDuplicateStudent() {
        studentDAO.add(student);
        assertThrows(SQLiteException.class, () -> { studentDAO.add(student); });
        removeBarbara();
    }

    @Test
    public void testRemoveById() {
        addBarbara();
        int numberOfStudentsBefore = studentDAO.getAll().size();
        studentDAO.removeById(student.getMatriculationNumber());
        int numberOfStudentsAfter = studentDAO.getAll().size();
        assertEquals(numberOfStudentsBefore - 1, numberOfStudentsAfter);
        removeBarbara();
    }

    @Test
    public void testRemoveByIdNotExistent() {
        assertEquals(false, studentDAO.removeById(999999));
    }
}
