package tm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tm.model.InputDialogModel;
import tm.model.SQLiteBuddy;
import tm.model.daos.StudentDAO;

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

}
