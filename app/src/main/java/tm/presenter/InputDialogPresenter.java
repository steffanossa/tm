package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import tm.model.InputDialogModel;
import tm.model.Student;
import tm.view.BadInputAlertView;
import tm.view.InputDialogView;

import java.util.Optional;

public class InputDialogPresenter implements InputDialogPresenterInterface
{
    private InputDialogView inputDialogView;
    private InputDialogModel inputDialogModel;

    public InputDialogPresenter(InputDialogView inputDialogView, InputDialogModel inputDialogModel)
    {
        this.inputDialogView = inputDialogView;
        this.inputDialogModel = inputDialogModel;
        prepareAll();
    }

    public void prepareAll()
    {
        Button okButton = (Button) inputDialogView.getDialogPane().lookupButton(inputDialogView.getOkButtonType());
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!handleOkButtonClick())
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

    private boolean handleOkButtonClick()
    {
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

                this.showBadInputAlert(errorMessage.toString());

                return false;
            } else {
                this.inputDialogModel.addStudent(
                    firstname,
                    lastname,
                    fhKennung.toLowerCase(),
                    Integer.valueOf(matrikelnummer));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showBadInputAlert(String message)
    {
        BadInputAlertView alert = new BadInputAlertView(message);
        alert.show();
    }

    @Override
    public void showAndWaitWithData(Student student)
    {
        TextField firstnameTextField = inputDialogView.getFirstNameTextField();
        TextField lastnameTextField = inputDialogView.getLastNameTextField();
        TextField matrikelnummerTextField = inputDialogView.getMatrikelnummerTextField();
        TextField fhKennungTextField = inputDialogView.getFhKennungTextField();

        firstnameTextField.setText(student.getFirstname());
        lastnameTextField.setText(student.getSurname());
        matrikelnummerTextField.setText(String.valueOf(student.getMatrikelnummer()));
        fhKennungTextField.setText(student.getFhKennung());

        Optional<ButtonType> result = inputDialogView.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            handleOkButtonClick();
        } else {
            inputDialogModel.addStudent(
                    student.getFirstname(),
                    student.getSurname(),
                    student.getFhKennung().toLowerCase(),
                    Integer.valueOf(student.getMatrikelnummer()));
        }
    }
}
