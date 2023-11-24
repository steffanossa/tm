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

import tm.model.classes.Student;
import tm.model.daos.StudentDAO;

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
    private static final Map<String, Function<Student, ?>> COLUMN_GETTER_MAP = new HashMap<String, Function<Student, ?>>()
    {{
        put("First name", Student::getFirstName);
        put("Surname", Student::getSurname);
        put("Matriculation Nr.", Student::getMatriculationNumber);
        put("FH Identifier", Student::getFhIdentifier);
    }};
    private StudentDAO studentDAO;
    private SQLiteBuddy sqLiteBuddy;

    public MainModel() {
        this.sqLiteBuddy = new SQLiteBuddy();
        this.studentDAO = new StudentDAO(this.sqLiteBuddy);
    }

    public StudentDAO getStudentDAO() {
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

    public static Map<String, Function<Student, ?>> getColumnGetterMap() {
        return COLUMN_GETTER_MAP;
    }

    public boolean removeStudent(Student student) {
        return studentDAO.removeById(student.getMatriculationNumber());
    }

    /**
     * Calls the concatenate method and copies its return value to the clipboard
     * @param students
     * @param visibleColumns
     * @param columnGetterMap
     * @param separator
     * @see concatenate method
     */
    public void copyToClipboard(
        Student[] students,
        ArrayList<String> visibleColumns,
        Map<String, Function<Student, ?>> columnGetterMap,
        String separator
        ) {
        String concatenatedString = concatenate(students, visibleColumns, columnGetterMap, separator);

        copyToClipboard(concatenatedString);
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

    public ArrayList<Student> retrieveStudents() {
        return studentDAO.getAll();
    }

    /**
     * 
     * @param students
     * @param visibleColumns
     * @param columnGetterMap
     * @param separator
     * @return A string of students' attributes in the order provided by visibleColumns separated by the separator given
     */
    public String concatenate(
        Student[] students,
        ArrayList<String> visibleColumns,
        Map<String, Function<Student, ?>> columnGetterMap,
        String separator
        ) {
        String concatenatedString = "";

        for (Student student : students)
        {
            for (String columnName : visibleColumns)
            {
                Function<Student, ?> function = columnGetterMap.get(columnName);
                concatenatedString += function.apply(student) + separator;
            }
            concatenatedString = concatenatedString.substring(0, concatenatedString.length() - separator.length()) + "\n";
        }
        return concatenatedString.trim();
    }

    /**
     * Calls concatenate to create a string from given arguments and save the result as file
     * @param file
     * @param students
     * @param visibleColumns
     * @param columnGetterMap
     * @param separator
     */
    public void saveTextToFile(
        File file,
        Student[] students,
        ArrayList<String> visibleColumns,
        Map<String, Function<Student, ?>> columnGetterMap,
        String separator
        ) {
        String concatenatedString = concatenate(students, visibleColumns, columnGetterMap, separator);
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(concatenatedString);
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
