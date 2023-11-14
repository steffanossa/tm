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
    private PatternTextField firstNameTextField;
    private PatternTextField surnameTextField;
    private PatternTextField matriculationNumberTextField;
    private PatternTextField fhIdentifierTextField;
    private ButtonType okButtonType;
    private ButtonType cancelButtonType;

    public InputDialogView(String title) {
        DialogPane dialogPane = new DialogPane();
        GridPane gridPane = new GridPane();
        //
        Label firstNameLabel = new Label("First name:");
        Label surnameLabel = new Label("Surname:");
        Label matriculationNumberLabel = new Label("Matriculation Nr.: ");
        Label fhIdentifierLabel = new Label("FH Identifier:");
        ////textfields
        // this.firstNameTextField = new PatternTextField("^[A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+(?: [A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+)?$");
        this.firstNameTextField = new PatternTextField("^[\\p{L}'][ \\p{L}'-]*[\\p{L}]$");
        // this.surnameTextField = new PatternTextField("^[A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+(?: [A-Za-zÄÖÜäöüßÀÁÂàáâÇçÈÉÊèéêËëÌÍÎìíîÏïÑñÒÓÔÕØòóôõøÙÚÛùúûÝýŸÿŴŵ\\-]+)?$");
        this.surnameTextField = new PatternTextField("^[\\p{L}'][ \\p{L}'-]*[\\p{L}]$");
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
        setTitle(title);
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

