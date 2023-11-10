package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import tm.model.InputDialogModel;
import tm.model.Student;
import tm.view.BadInputAlertView;
import tm.view.InputDialogView;

import java.sql.SQLException;
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

    /**
     * validate user input + add data to database if validation succeeded
     * @return
     */
    private boolean handleOkButtonClick()
    {
        try {
            String firstname = inputDialogView.getFirstNameTextField().getText();
            String lastname = inputDialogView.getLastNameTextField().getText();
            String fhKennung = inputDialogView.getFhKennungTextField().getText();
            String matrikelnummer = inputDialogView.getMatrikelnummerTextField().getText();

            String errorMessage = "Bitte folgende Eingabe/n prüfen:";
            
            if (!inputDialogModel.validateName(firstname))
                errorMessage += "\n- Vorname";
            if (!inputDialogModel.validateName(lastname))
                errorMessage += "\n- Nachname";
            if (!inputDialogModel.validateMatrikelnummer(matrikelnummer))
                errorMessage += "\n- Matrikel-Nr.";
            if (!inputDialogModel.validateFhKennung(fhKennung)) {
                errorMessage += "\n- FH-Kennung";

                this.showBadInputAlert(errorMessage.toString());

                return false;
            } else {
                try {
                    this.inputDialogModel.addStudent(
                        firstname,
                        lastname,
                        fhKennung.toLowerCase(),
                        Integer.valueOf(matrikelnummer));
                    return true;
                }
                catch (SQLException e)
                {
                    if (!notUniqueMessageBuilder(e))
                        e.printStackTrace();
                    return false;
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean notUniqueMessageBuilder(SQLException exception)
    {
        String message = exception.getMessage();
        if (message.contains("UNIQUE constraint failed"))
        {
            String matrikelNrPattern = "Students.matrikelnr";
            String fhKennungPattern = "Students.fhkennung";
            String badInputMessage = "Wert muss einzigartig sein, existiert aber bereits in der Datenbank:";
            if (message.contains(matrikelNrPattern))
                badInputMessage += "\n- Matrikel-Nr.";
            if (message.contains(fhKennungPattern))
                badInputMessage += "\n- FH-Kennung";
            showBadInputAlert(badInputMessage);
            return true;
        }
        return false;
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
            try {
                inputDialogModel.addStudent(
                    student.getFirstname(),
                    student.getSurname(),
                    student.getFhKennung().toLowerCase(),
                    Integer.valueOf(student.getMatrikelnummer()));
            } 
            catch (SQLException e)
            {
                if (!notUniqueMessageBuilder(e))
                    e.printStackTrace();
            }
            
        }
    }
}
