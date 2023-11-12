package tm.view;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tm.customcontrols.PatternTextField;

public class InputDialogView extends Dialog<ButtonType> {
    private DialogPane dialogPane;
    private GridPane gridPane;
    private Label firstNameLabel;
    private Label surnameLabel;
    private Label matriculationNumberLabel;
    private Label fhIdentifierLabel;
    private PatternTextField firstNameTextField;
    private PatternTextField surnameTextField;
    private PatternTextField matriculationNumberTextField;
    private PatternTextField fhIdentifierTextField;
    private ButtonType okButtonType;
    private ButtonType cancelButtonType;

    public InputDialogView() {
        dialogPane = new DialogPane();
        gridPane = new GridPane();
        //
        this.firstNameLabel = new Label("First name:");
        this.surnameLabel = new Label("Surname:");
        this. matriculationNumberLabel = new Label("Matriculation Nr.: ");
        this.fhIdentifierLabel = new Label("FH Identifier:");
        ////textfields
        this.firstNameTextField = new PatternTextField("^[A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+(?: [A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+)?$");
        this.surnameTextField = new PatternTextField("^[A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+(?: [A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+)?$");
        this.matriculationNumberTextField = new PatternTextField("^\\d{1,9}$");
        this.fhIdentifierTextField = new PatternTextField("^[A-Za-z]{2}\\d{6}$");
        this.firstNameTextField.setPromptText("Erika");
        this.surnameTextField.setPromptText("Mustermann");
        this.matriculationNumberTextField.setPromptText("1234567");
        this.fhIdentifierTextField.setPromptText("em123456");
        //buttons
        this.okButtonType = new ButtonType("OK", ButtonBar.ButtonData.APPLY);
        this.cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        //
        gridPane.addColumn(0,
            firstNameLabel,
            surnameLabel,
            matriculationNumberLabel,
            fhIdentifierLabel
        );
        gridPane.addColumn(1,
            firstNameTextField,
            surnameTextField,
            matriculationNumberTextField,
            fhIdentifierTextField
        );
        dialogPane.setContent(gridPane);
        dialogPane.getButtonTypes().setAll(okButtonType, cancelButtonType);
        //
        setDialogPane(dialogPane);
        setTitle("Add entity");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
    }

    public ButtonType getOkButtonType() {
        return okButtonType;
    }

    public ButtonType getCancelButtonType() {
        return cancelButtonType;
    }

    public PatternTextField getFirstNamePatternTextField() {
        return firstNameTextField;
    }

    public PatternTextField getSurnamePatternTextField() {
        return surnameTextField;
    }

    public PatternTextField getMatriculationNumberPatternTextField() {
        return matriculationNumberTextField;
    }

    public PatternTextField getFhIdentifierPatternTextField() {
        return fhIdentifierTextField;
    }
}

