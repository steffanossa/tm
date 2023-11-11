package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;

import tm.customcontrols.PatternTextField;
import tm.model.InputDialogModel;
import tm.model.classes.Student;
import tm.presenter.interfaces.InputDialogPresenterInterface;
import tm.view.InputDialogView;
import tm.view.alerts.BadInputAlertView;

import java.sql.SQLException;
import java.util.Optional;

// import java.lang.StringBuilder;

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

    public void fillTextFields(Student student)
    {
        TextField firstnameTextField = inputDialogView.getFirstNamePatternTextField();
        TextField lastnameTextField = inputDialogView.getLastNamePatternTextField();
        TextField matrikelnummerTextField = inputDialogView.getMatrikelnummerPatternTextField();
        TextField fhKennungTextField = inputDialogView.getFhKennungPatternTextField();

        firstnameTextField.setText(student.getFirstname());
        lastnameTextField.setText(student.getSurname());
        matrikelnummerTextField.setText(String.valueOf(student.getMatrikelnummer()));
        fhKennungTextField.setText(student.getFhKennung());
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

    private boolean validateAndAppendToErrorMessage(PatternTextField patternTextField, String patternTextFieldName, StringBuilder errorMessage)
    {
        boolean isValid = patternTextField.validate();
        if (!isValid)
        {
            errorMessage.append("\n- ").append(patternTextFieldName);
        }
        return isValid;
    }

    /**
     * validate user input + add data to database if valid
     * @return
     * TODO: die ist mir aus dem ruder gelaufen
     */
    private boolean handleOkButtonClick()
    {
        String firstname = inputDialogView.getFirstNamePatternTextField().getText();
        String lastname = inputDialogView.getLastNamePatternTextField().getText();
        String fhKennung = inputDialogView.getFhKennungPatternTextField().getText();
        String matrikelnummer = inputDialogView.getMatrikelnummerPatternTextField().getText();
        PatternTextField[] patternTextFields = {
            inputDialogView.getFirstNamePatternTextField(),
            inputDialogView.getLastNamePatternTextField(),
            inputDialogView.getMatrikelnummerPatternTextField(),
            inputDialogView.getFhKennungPatternTextField()
        };
        StringBuilder errorMessage = new StringBuilder("Bitte folgende Eingabe/n prüfen:");
        boolean inputIsValid = true;
        for (var patternTextField : patternTextFields) {
            //smart
            inputIsValid &= validateAndAppendToErrorMessage(patternTextField, matrikelnummer, errorMessage);
        }
        if (!inputIsValid) 
        {
            this.showBadInputAlert(errorMessage.toString());
            return false;
        }
        else 
        {
            try
            {
                this.inputDialogModel.addStudent(
                    firstname,
                    lastname,
                    fhKennung.toLowerCase(),
                    Integer.valueOf(matrikelnummer));
                return inputIsValid;
            }
            catch (SQLException e)
            {
                if (uniquenessCheckAndAlertShow(e))
                {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
    }

    private boolean uniquenessCheckAndAlertShow(SQLException exception)
    {
        String message = exception.getMessage();
        if (message.contains("UNIQUE constraint failed")) //wenn NICHT UNIQUE
        {
            String matrikelNrPattern = "Students.matrikelnr";
            String fhKennungPattern = "Students.fhkennung";
            String badInputMessage = "Wert muss einzigartig sein, existiert aber bereits in der Datenbank:";
            if (message.contains(matrikelNrPattern))
                badInputMessage += "\n- Matrikel-Nr.";
            if (message.contains(fhKennungPattern))
                badInputMessage += "\n- FH-Kennung";
            showBadInputAlert(badInputMessage); //TODO:bad spot for this
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
        fillTextFields(student);

        Optional<ButtonType> result = inputDialogView.showAndWait();
        if (result.isPresent() && !result.get().getButtonData().equals(ButtonData.APPLY))
        {
            try
            {
                inputDialogModel.addStudent(
                    student.getFirstname(),
                    student.getSurname(),
                    student.getFhKennung().toLowerCase(),
                    Integer.valueOf(student.getMatrikelnummer()));
            } 
            catch (SQLException e)
            {
                e.getStackTrace();
            }
        }
    }
}
