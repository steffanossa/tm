package tm.model;

import java.util.Map;
import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StringBuddy {
    
    public static String concatenate(ObservableList<Student> students, TableView<Student> tableView, Map<String, Function<Student, ?>> columnGetterMap, String separator) {
        String concatenatedString = "";
        for (Student student : students) {
            for (TableColumn<Student, ?> column : tableView.getColumns()) {
                String columnName = column.getText();
                if (column.isVisible()) {
                    Function<Student, ?> function = columnGetterMap.get(columnName);
                    concatenatedString += function.apply(student) + separator;
                }
            }
            concatenatedString = concatenatedString.substring(0, concatenatedString.length() - separator.length()) + "\n";
        }
        return concatenatedString;
    }
}
