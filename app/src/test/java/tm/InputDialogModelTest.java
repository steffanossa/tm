package tm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tm.model.InputDialogModel;
import tm.model.MainModel;
import tm.model.SQLiteBuddy;
import tm.model.StudentDAO;

public class InputDialogModelTest {

    private static InputDialogModel inputDialogModel;

    private String name;
    private String fhKennung;
    private String matrikelnummer;

    private boolean result;
    

    @BeforeAll
    public static void initMainModel()
    {
        inputDialogModel = new InputDialogModel(new StudentDAO(new SQLiteBuddy()));
    }
    @Test
    public void TestValidateName() {
        name = "Peter";
        result = inputDialogModel.validateName(name);
        assertEquals(true, result);

        name = "äÄöÖáôì-Îù üÜß";
        result = inputDialogModel.validateName(name);
        assertEquals(true, result);

        name = "Aligator9";
        result = inputDialogModel.validateName(name);
        assertEquals(false, result);
    }

    @Test
    public void TestValidateFhKennung() {
        fhKennung = "ab111111";
        result = inputDialogModel.validateFhKennung(fhKennung);
        assertEquals(true, result);

        fhKennung = "ab123";
        result = inputDialogModel.validateFhKennung(fhKennung);
        assertEquals(false, result);

        fhKennung = "309jafj";
        result = inputDialogModel.validateFhKennung(fhKennung);
        assertEquals(false, result);

        fhKennung = "AB123456";
        result = inputDialogModel.validateFhKennung(fhKennung);
        assertEquals(true, result);
    }

    @Test
    public void TestValidateMatrikelnummer() {
        matrikelnummer = "1234567";
        result = inputDialogModel.validateMatrikelnummer(matrikelnummer);
        assertEquals(true, result);

        matrikelnummer = "123456";
        result = inputDialogModel.validateMatrikelnummer(matrikelnummer);
        assertEquals(true, result);

        matrikelnummer = "12345678";
        result = inputDialogModel.validateMatrikelnummer(matrikelnummer);
        assertEquals(false, result);

        matrikelnummer = "fünfhundert";
        result = inputDialogModel.validateMatrikelnummer(matrikelnummer);
        assertEquals(false, result);

        matrikelnummer = "123456 ";
        result = inputDialogModel.validateMatrikelnummer(matrikelnummer);
        assertEquals(false, result);
    }

}
