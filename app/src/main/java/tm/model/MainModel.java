package tm.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import tm.model.daos.GenericDAO;
import tm.model.daos.StudentDAO;
import tm.model.dtos.StudentDTO;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;


/**
 * 
 */
public class MainModel {

    /**
     * TreeMap with most common separator names and corresponding characters
     */
    private static final TreeMap<String, String> separatorMap = new TreeMap<>()
    {{
        put("Comma", ",");
        put("Space", " ");
        put("Semicolon", ";");
        put("Tab", "\t");
    }};

    /**
     * HashMap with column headers and corresponding getters
     */
    private static final Map<String, Function<StudentDTO, ?>> COLUMN_GETTER_MAP = new HashMap<String, Function<StudentDTO, ?>>()
    {{
        put("First name", StudentDTO::getFirstName);
        put("Surname", StudentDTO::getSurname);
        put("Matriculation Nr.", StudentDTO::getMatriculationNumber);
        put("FH Identifier", StudentDTO::getFhIdentifier);
    }};
    private GenericDAO<StudentDTO> studentDAO;
    private SQLiteBuddy sqLiteBuddy;

    public MainModel() {
        this.sqLiteBuddy = new SQLiteBuddy();
        this.studentDAO = new StudentDAO(this.sqLiteBuddy);
    }

    public GenericDAO<StudentDTO> getStudentDAO() {
        return studentDAO;
    }

    /**
     * 
     * @param separator
     * @param visibleColumns
     * @return String representing the order of visible columns using the separator given
     */
    public String createPreviewString(
        String separator,
        ArrayList<String> visibleColumns
        ) {
        String previewString = "";
        String previewSeparator = separator;
        Map<String, String> previewMap = Map.of(
            "First name", "Erika",
            "Surname", "Mustermann",
            "Matriculation Nr.", "1234567",
            "FH Identifier", "AB123456" );
        for (String columnName : visibleColumns)
            previewString += previewMap.get(columnName) + previewSeparator;

        return previewString.isEmpty() ? "Nothing to show" : previewString.substring(0, previewString.length() - previewSeparator.length());
    }

    public String getSeparator(String separatorKey) {
        return separatorMap.get(separatorKey);
    }

    public Set<String> getSeparatorKeySet() {
        return separatorMap.keySet();
    }

    public static Map<String, Function<StudentDTO, ?>> getColumnGetterMap() {
        return COLUMN_GETTER_MAP;
    }

    public boolean removeStudent(StudentDTO student)  throws SQLException
    {
        return studentDAO.removeById(student.getMatriculationNumber());
    }

    /**
     * Copies the string provided to the clipboard
     * @param string
     */
    public void copyToClipboard(String string)
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, null);
    }

    public ArrayList<StudentDTO> retrieveStudents() throws SQLException
    {
        return studentDAO.getAll();
    }

    /**
     * 
     * @param students
     * @param visibleColumns
     * @param separator
     * @return A string of students' attributes in the order provided by visibleColumns separated by the separator given
     */
    public String concatenate(
        StudentDTO[] students,
        ArrayList<String> visibleColumns,
        String separator
        ) {
        String concatenatedString = "";

        for (StudentDTO student : students)
        {
            for (String columnName : visibleColumns)
            {
                Function<StudentDTO, ?> function = COLUMN_GETTER_MAP.get(columnName);
                concatenatedString += function.apply(student) + separator;
            }
            concatenatedString = concatenatedString.substring(0, concatenatedString.length() - separator.length()) + "\n";
        }
        return concatenatedString.trim();
    }

    /**
     * Writes String to file
     * @param file
     * @param string
     */
    public void writeStringToFile( String string, File file ) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(string);
            writer.close();
        }
        catch (IOException e) { System.out.println(e.getMessage()); }
    }

    /**
     * 
     * @param dbFile
     * @return {@code true} if file could be opened correctly
     * @throws SQLException
     */
    public boolean openDatabase(File dbFile) throws SQLException
    {
        String url = dbFile.getAbsolutePath();
        sqLiteBuddy.setUrl(url);
        if (sqLiteBuddy.isAcceptedDatabase())
            return true;
        return false;
    }
}
