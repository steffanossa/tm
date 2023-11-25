package tm;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteException;

import tm.model.SQLiteBuddy;

public class SQLiteBuddyTest {

    String url;
    
    @Test
    public void testEstablishConnection() {

    }

    @Test
    public void testCloseDatabase() {
        
    }

    @Test
    public void testIsAcceptedDatabase() {
        url = "something/non/existent.db";
        SQLiteBuddy sqLiteBuddy = new SQLiteBuddy(url);
        assertThrows(NullPointerException.class, () -> { sqLiteBuddy.isAcceptedDatabase(); });
        url = "src/test/java/tm/AppTest.java";
        sqLiteBuddy.setUrl(url);
        assertFalse(sqLiteBuddy.isAcceptedDatabase());
    }


}
