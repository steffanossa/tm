package tm.view;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class InputDialogView extends Dialog<ButtonType> {
    private DialogPane dialogPane;
    private GridPane gridPane;
    private Label firstNameLabel;
    private Label lastNameLabel;
    private Label matrikelnummerLabel;
    private Label fhKennungLabel;
    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField matrikelnummerTextField;
    private TextField fhKennungTextField;
    private ButtonType okButtonType;
    private ButtonType cancelButtonType;

    public InputDialogView() {
        dialogPane = new DialogPane();
        gridPane = new GridPane();
        //
        this.firstNameLabel = new Label("Vorname:");
        this.lastNameLabel = new Label("Nachname:");
        this. matrikelnummerLabel = new Label("Matrikel-Nr.:");
        this.fhKennungLabel = new Label("FH-Kennung:");
        ////textfields
        this.firstNameTextField = new TextField();
        this.lastNameTextField = new TextField();
        this.matrikelnummerTextField = new TextField();
        this.fhKennungTextField = new TextField();
        this.firstNameTextField.setPromptText("Erika");
        this.lastNameTextField.setPromptText("Mustermann");
        this.matrikelnummerTextField.setPromptText("1234567");
        this.fhKennungTextField.setPromptText("em123456");
        //buttons
        this.okButtonType = new ButtonType("OK", ButtonBar.ButtonData.APPLY);
        this.cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        //
        gridPane.addColumn(0, firstNameLabel, lastNameLabel, matrikelnummerLabel, fhKennungLabel);
        gridPane.addColumn(1, firstNameTextField, lastNameTextField, matrikelnummerTextField, fhKennungTextField);
        dialogPane.setContent(gridPane);
        dialogPane.getButtonTypes().setAll(okButtonType, cancelButtonType);
        //
        setDialogPane(dialogPane);
        setTitle("Entität hinzufügen");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
    }

    public ButtonType getOkButtonType() {
        return okButtonType;
    }

    public ButtonType getCancelButtonType() {
        return cancelButtonType;
    }

    public TextField getFirstNameTextField() {
        return firstNameTextField;
    }

    public TextField getLastNameTextField() {
        return lastNameTextField;
    }

    public TextField getMatrikelnummerTextField() {
        return matrikelnummerTextField;
    }

    public TextField getFhKennungTextField() {
        return fhKennungTextField;
    }
}

