package tm.view.dialogs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import tm.customcontrols.PatternTextField;
import tm.model.enums.PattyImages;

/**
 * View for the input window
 */
public class InputDialogView extends Dialog<ButtonType> {
    private LinkedHashMap<String, String[]> linkedHashMap = new LinkedHashMap<>();
    private HashMap<String, PatternTextField> patternTextFieldMap = new HashMap<>();

    private ButtonType okButtonType;
    private ButtonType cancelButtonType;

    public InputDialogView(String title) {
        linkedHashMap.put("firstName", new String[]{
            "First Name",
            "^[\\p{L}'][ \\p{L}'-]*[\\p{L}]$",
            "Erika"
        });
        linkedHashMap.put("surname", new String[]{
            "Surname",
            "^[\\p{L}'][ \\p{L}'-]*[\\p{L}]$",
            "Mustermann"
        });
        linkedHashMap.put("matriculationNumber", new String[]{
            "Matriculation Nr.",
            "^\\d{1,9}$",
            "123456"
        });
        linkedHashMap.put("fhIdentifier", new String[]{
            "FH Identifier",
            "^[A-Za-z]{2}\\d{6}$",
            "em123456"
        });
        DialogPane dialogPane = new DialogPane();
        GridPane gridPane = new GridPane(10,1);

        createWidgets(gridPane);

        //buttons
        this.okButtonType = new ButtonType("OK", ButtonBar.ButtonData.APPLY);
        this.cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        //
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        dialogPane.setContent(gridPane);
        dialogPane.getButtonTypes().setAll(cancelButtonType, okButtonType);
        //
        setDialogPane(dialogPane);
        setTitle(title);
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream(PattyImages.LOGO.getPath())));
    }

    /**
     * Creates the widgets necessary and ararnges them in the Gridpane provided
     * @param gridPane
     */
    private void createWidgets(GridPane gridPane) {
        int row = 0;
        for (Map.Entry<String, String[]> entry : linkedHashMap.entrySet()) {
            String widgetName = entry.getKey();
            String[] properties = entry.getValue();

            String labelString = properties[0];
            String pattern = properties[1];
            String example = properties[2];

            Label label = new Label(labelString);
            gridPane.add(label, 0, row);

            PatternTextField patternTextField = new PatternTextField(pattern);
            patternTextField.setPromptText(example);
            gridPane.add(patternTextField, 1, row);
            patternTextFieldMap.put(widgetName, patternTextField);

            row++;
        }
    }

    public ButtonType getOkButtonType() {
        return okButtonType;
    }

    public ButtonType getCancelButtonType() {
        return cancelButtonType;
    }

    public PatternTextField getPatternTextFieldByName(String name) {
        return patternTextFieldMap.get(name);
    }

}

