package tm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import tm.model.MainModel;
import tm.model.Student;

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
            put("Vorname", Student::getFirstname);
            put("Nachname", Student::getSurname);
            put("Matrikel-Nr.", Student::getMatrikelnummer);
            put("FH-Kennung", Student::getFhKennung);
        }};

        students = new Student[] {
            new Student(1234567, "Erika", "Mustermann", "AB123456"),
            new Student(9876543, "Benjamin", "Blümchen", "XY987654")
        };

        visibleColumns = new ArrayList<>(List.of("Vorname", "Nachname", "Matrikel-Nr.", "FH-Kennung"));

    }

    @Test
    public void testCreatePreviewString() {
        separator = "\t";
        visibleColumns = new ArrayList<>(List.of("Vorname", "FH-Kennung"));
        expectedResultString = "Erika    AB123456";
        resultString = mainModel.createPreviewString(separator, visibleColumns);
        assertEquals(expectedResultString, resultString);
    }

    @Test
    public void testCreatePreviewStringNothingToShow()
    {
        separator = ",";
        visibleColumns = new ArrayList<>();
        expectedResultString = "Nichts anzuzeigen";
        resultString = mainModel.createPreviewString(separator, visibleColumns);
        assertEquals(expectedResultString, resultString);
    }

    @Test
    public void testConcatenate()
    {
        visibleColumns = new ArrayList<>(List.of("Vorname", "Nachname"));
        separator = ",";
        expectedResultString = "Erika,Mustermann\nBenjamin,Blümchen";
        resultString = mainModel.concatenate(students, visibleColumns, COLUMN_GETTER_MAP, separator);
        assertEquals(expectedResultString, resultString);
    }

    
}
