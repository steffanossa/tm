package tm.customcontrols;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class CheckBoxTableCell<S, T> extends TableCell<S, T> {
    private final CheckBox checkBox;

    public CheckBoxTableCell() {
        checkBox = new CheckBox();
        setGraphic(checkBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setEditable(true);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
