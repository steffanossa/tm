package tm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import tm.model.MainModel;
import tm.model.classes.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.HashMap;

public class MainModelTest {

    private static MainModel mainModel;
    private static HashMap<String, Function<Student, ?>> COLUMN_GETTER_MAP;
    private String separator;
    private Student[] students;
    private ArrayList<String> visibleColumns;
    private String expectedResultString;
    private String resultString;
    


    @BeforeAll
    public static void initMainModel()
    {
        mainModel = new MainModel();
    }

    @BeforeEach
    public void initThings()
    {
        COLUMN_GETTER_MAP = new HashMap<String, Function<Student, ?>>() {{
            //Student::getFirstname = Methodenreferenz
            put("First name", Student::getFirstName);
            put("Surname", Student::getSurname);
            put("Matriculation Nr.", Student::getMatriculationNumber);
            put("FH Identifier", Student::getFhIdentifier);
        }};

        students = new Student[] {
            new Student("Erika", "Mustermann", 1234567, "AB123456"),
            new Student("Benjamin", "Blümchen", 9876543, "XY987654")
        };

        visibleColumns = new ArrayList<>(List.of("First name", "Surname", "Matriculation Nr.", "FH Identifier"));

    }

    @Test
    public void testCreatePreviewString() {
        separator = "\t";
        visibleColumns = new ArrayList<>(List.of("First name", "FH Identifier"));
        expectedResultString = "Erika\tAB123456";
        resultString = mainModel.createPreviewString(separator, visibleColumns);
        assertEquals(expectedResultString, resultString);
    }

    @Test
    public void testCreatePreviewStringNothingToShow()
    {
        separator = ",";
        visibleColumns = new ArrayList<>();
        expectedResultString = "Nothing to show";
        resultString = mainModel.createPreviewString(separator, visibleColumns);
        assertEquals(expectedResultString, resultString);
    }

    @Test
    public void testConcatenate()
    {
        visibleColumns = new ArrayList<>(List.of("First name", "Surname"));
        separator = ",";
        expectedResultString = "Erika,Mustermann\nBenjamin,Blümchen";
        resultString = mainModel.concatenate(students, visibleColumns, COLUMN_GETTER_MAP, separator);
        assertEquals(expectedResultString, resultString);
    }

    
}
