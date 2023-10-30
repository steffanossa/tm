package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import tm.model.InputDialogModel;
import tm.view.InputDialogView;

public class InputDialogPresenter implements InputDialogPresenterInterface{
    private InputDialogView inputDialogView;
    private InputDialogModel inputDialogModel;

    public InputDialogPresenter(InputDialogView inputDialogView, InputDialogModel inputDialogModel) {
        this.inputDialogView = inputDialogView;
        this.inputDialogModel = inputDialogModel;
        prepareAll();
    }

    public void prepareAll() {
        Button okButton = (Button) inputDialogView.getDialogPane().lookupButton(inputDialogView.getOkButtonType());
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!okButtonHandler())
                event.consume(); //great
        });
    }
    
    @Override
    public void showAndWait() {
        inputDialogView.showAndWait();
    }

    @Override
    public void hide() {
        inputDialogView.hide();
    }

    private boolean okButtonHandler() {
        //forgot, why I needed the try-block
        try {
            String firstname = inputDialogView.getFirstNameTextField().getText();
            String lastname = inputDialogView.getLastNameTextField().getText();
            String fhKennung = inputDialogView.getFhKennungTextField().getText();
            String matrikelnummer = inputDialogView.getMatrikelnummerTextField().getText();

            StringBuilder errorMessage = new StringBuilder("Bitte folgende Eingabe/n prüfen:");
            if (!inputDialogModel.validateName(firstname))
                errorMessage.append("\nVorname");
            if (!inputDialogModel.validateName(lastname))
                errorMessage.append("\nNachname");
            if (!inputDialogModel.validateMatrikelnummer(matrikelnummer))
                errorMessage.append("\nMatrikelnummer");
            if (!inputDialogModel.validateFhKennung(fhKennung)) {
                errorMessage.append("\nFH-Kennung");

                this.showAlert(errorMessage.toString());

                return false;
            } else {
                this.inputDialogModel.addStudent(
                    firstname,
                    lastname,
                    fhKennung,
                    Integer.valueOf(matrikelnummer));
                return true;
            }
        } catch (Exception e) {
            // TODO: maybeeee
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehlerhafte Eingabe");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        Image image = new Image(getClass().getResourceAsStream("/images/Error.png"));
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.show();
    }

}
