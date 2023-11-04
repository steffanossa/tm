package tm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

import tm.model.SQLiteBuddy;
import tm.model.StudentDAO;

public class StudentDAOTest {


    
    private SQLiteBuddy sqLiteBuddy = new SQLiteBuddy();
    private StudentDAO studentDAO = new StudentDAO(sqLiteBuddy);


    @Test
    public void testGetAllStudents() {

    }

    @Test
    public void testAddStudent() {

    }

    @Test
    public void testRemoveStudentByMatrikelnummer() {

    }
}
