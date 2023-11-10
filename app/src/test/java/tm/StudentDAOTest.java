package tm;

import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;

import tm.model.SQLiteBuddy;
import tm.model.Student;
import tm.model.StudentDAO;

public class StudentDAOTest {

    private final String URL = ".." + File.separator + "students.db";
    private final Student student = new Student(
        999999,
        "Barbara",
        "Salesch",
        "BS999999");

    private SQLiteBuddy sqLiteBuddy = new SQLiteBuddy(URL);
    private StudentDAO studentDAO = new StudentDAO(sqLiteBuddy);

    @Test
    public void testGetAllStudents() {
        ArrayList<Student> allStudents = studentDAO.getAll();
        assertEquals(allStudents.size(), 1000);
    }

    @Test
    public void testAddStudent() {
        int numberOfStudentsBefore = studentDAO.getAll().size();
        studentDAO.add(student);
        int numberOfStudentsAfter = studentDAO.getAll().size();
        assertEquals(numberOfStudentsBefore, numberOfStudentsAfter - 1);
    }

    @Test
    public void testAddDuplicateStudent() {
        
    }

    @Test
    public void testRemoveStudentByMatrikelnummer() {

    }
}
