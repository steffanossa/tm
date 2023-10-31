package tm.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class MainModel {
    private static final Map<String, String> separatorMap = Map.of(
        "Komma", ",",
        "Leerzeichen", " ",
        "Semikolon", ";",
        "Tab", "\t" );
    private static final Map<String, Function<Student, ?>> COLUMN_GETTER_MAP = new HashMap<String, Function<Student, ?>>() {{
        //Student::getFirstname = Methodenreferenz
        put("Vorname", Student::getFirstname);
        put("Nachname", Student::getSurname);
        put("Matrikel-Nr.", Student::getMatrikelnummer);
        put("FH-Kennung", Student::getFhKennung);
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
    
    public String createPreviewString(String separator, TableView<Student> tableView)  {
        String previewString = "";
        String previewSeparator = separator;
        if (previewSeparator == "\t")
            previewSeparator = "    ";
        Map<String, String> previewMap = Map.of(
            "Vorname", "Erika",
            "Nachname", "Mustermann",
            "Matrikel-Nr.", "1234567",
            "FH-Kennung", "AB123456" );
        for (TableColumn<Student, ?> column : tableView.getColumns()) {
            String columnName = column.getText();
            if (column.isVisible())
                previewString += previewMap.get(columnName) + previewSeparator;
        }

        // return previewString.substring(0, previewString.length() - previewSeparator.length());
        return previewString.isEmpty() ? "Nichts anzuzeigen" : previewString.substring(0, previewString.length() - previewSeparator.length());

    }

    public String getSeparator(String separatorKey) {
        return separatorMap.get(separatorKey);
    }

    public Set<String> getSeparatorKeySet() {
        return separatorMap.keySet();
    }

    public TableView<Student> fillTableView(TableView<Student> tableView, ObservableList<Student> students) {
        //keine elegante schleife, die nicht 3mal mehr Zeilen lang ist. java, ey, asozial
        TableColumn<Student, String> firstNameColumn = new TableColumn<>("Vorname");
        TableColumn<Student, String> lastNameColumn = new TableColumn<>("Nachname");
        TableColumn<Student, Integer> matrikelnummerColumn = new TableColumn<>("Matrikel-Nr.");
        TableColumn<Student, String> fhKennungColumn = new TableColumn<>("FH-Kennung");
        //
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        matrikelnummerColumn.setCellValueFactory(new PropertyValueFactory<>("matrikelnummer"));
        fhKennungColumn.setCellValueFactory(new PropertyValueFactory<>("fhKennung"));
        tableView.getColumns().setAll(firstNameColumn, lastNameColumn, matrikelnummerColumn, fhKennungColumn);
        tableView.setItems(students);
        return tableView;
    }

    public static Map<String, Function<Student, ?>> getColumnGetterMap() {
        return COLUMN_GETTER_MAP;
    }

    public void removeStudent(Student student) {
        studentDAO.removeStudentByMatrikelnummer(student.getMatrikelnummer());
    }

    public void copyToClipboard(
        ObservableList<Student> students,
        TableView<Student> tableView,
        Map<String, Function<Student, ?>> columnGetterMap,
        String separator)
        {
        String concatenatedString = StringBuddy.concatenate(students, tableView, columnGetterMap, separator);

        ClipboardBuddy.copyToClipboard(concatenatedString);
    }

    public ObservableList<Student> extractStudents() {
        return studentDAO.getAllStudents();
    }

    public void saveTextToFile(
        File file,
        ObservableList<Student> students,
        TableView<Student> tableView,
        Map<String, Function<Student, ?>> columnGetterMap,
        String separator
        ) {
        String concatenatedString = StringBuddy.concatenate(students, tableView, columnGetterMap, separator);
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(concatenatedString);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean openDatabase(File dbFile) throws SQLException {
        String url = dbFile.getAbsolutePath();
        sqLiteBuddy.setUrl(url);
        if (sqLiteBuddy.isAcceptedDatabase())
            return true;
        return false;
    }



}
