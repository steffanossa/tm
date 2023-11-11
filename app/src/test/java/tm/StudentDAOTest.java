package tm;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.junit.platform.commons.logging.Logger;
import org.sqlite.SQLiteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import tm.model.SQLiteBuddy;
import tm.model.classes.Student;
import tm.model.daos.StudentDAO;

public class StudentDAOTest {

    private static final Logger logger = LoggerFactory.getLogger(StudentDAOTest.class);

    private final String url = ".." + File.separator + "mock.db";
    private final Student student = new Student(
        111111,
        "Barbara",
        "Salesch",
        "bs111111");

    private SQLiteBuddy sqLiteBuddy = new SQLiteBuddy(url);
    private StudentDAO studentDAO = new StudentDAO(sqLiteBuddy);

    
    @BeforeEach
    public void initMockDb() {
        logger.info("megga");
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

            statement.execute(
                "INSERT INTO Students (firstname, surname, matrikelnummer, fhkennung)" +
                "VALUES ('Alfred', 'Tetzlaf', 222222, 'at222222')");

            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    // @Test
    // public void testGetAllStudents() {
    //     ArrayList<Student> allStudents = studentDAO.getAll();
    //     assertEquals(1, allStudents.size());
    // }

    // @Test
    // public void testAddStudent() {
    //     int numberOfStudentsBefore = studentDAO.getAll().size();
    //     System.out.println(numberOfStudentsBefore);
    //     try { studentDAO.add(student); }
    //     catch (SQLException e) {System.out.println("\n\n\n\n");}
    //     int numberOfStudentsAfter = studentDAO.getAll().size();
    //     assertEquals(numberOfStudentsAfter, numberOfStudentsBefore + 1);
    // }

    @Test
    public void testAddDuplicateStudent() {
        System.out.println("awlasldk");
        try { studentDAO.add(student); }
        catch (SQLException e) {}

        assertThrowsExactly(SQLiteException.class, () -> {
            studentDAO.add(student);
        });
    }

    @Test
    public void testRemoveStudentByMatrikelnummer() {
        try {
            studentDAO.removeById(222222);
        } catch (Exception e) {}
        assertEquals(0, studentDAO.getAll().size());
    }
}
