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


public class MainModel {

    private static final TreeMap<String, String> separatorMap = new TreeMap<>()
    {{
        put("Comma", ",");
        put("Space", " ");
        put("Semicolon", ";");
        put("Tab", "\t");
    }};

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

    public void copyToClipboard(
        Student[] students,
        ArrayList<String> visibleColumns,
        Map<String, Function<Student, ?>> columnGetterMap,
        String separator
        ) {
        String concatenatedString = concatenate(students, visibleColumns, columnGetterMap, separator);

        copyToClipboard(concatenatedString);
    }

    public void copyToClipboard(String string)
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, null);
    }

    public ArrayList<Student> retrieveStudents() {
        return studentDAO.getAll();
    }

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

    public boolean openDatabase(File dbFile) throws SQLException
    {
        String url = dbFile.getAbsolutePath();
        sqLiteBuddy.setUrl(url);
        if (sqLiteBuddy.isAcceptedDatabase())
            return true;
        return false;
    }
}
