package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;

import tm.customcontrols.PatternTextField;
import tm.model.InputDialogModel;
import tm.model.classes.Student;
import tm.presenter.interfaces.InputDialogPresenterInterface;
import tm.view.InputDialogView;
import tm.view.alerts.BadInputAlertView;

import java.sql.SQLException;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private void fillTextFields(Student student)
    {
        PatternTextField firstnameTextField = inputDialogView.getFirstNamePatternTextField();
        PatternTextField lastnameTextField = inputDialogView.getSurnamePatternTextField();
        PatternTextField matrikelnummerTextField = inputDialogView.getMatriculationNumberPatternTextField();
        PatternTextField fhKennungTextField = inputDialogView.getFhIdentifierPatternTextField();

        firstnameTextField.setText(student.getFirstName());
        lastnameTextField.setText(student.getSurname());
        matrikelnummerTextField.setText(String.valueOf(student.getMatriculationNumber()));
        fhKennungTextField.setText(student.getFhIdentifier());
    }

    private void prepareAll()
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
     */
    private boolean handleOkButtonClick()
    {
        TreeMap<String, PatternTextField> patternTextFieldMap = new TreeMap<>(Comparator.reverseOrder())
        {{
            put("First name", inputDialogView.getFirstNamePatternTextField());
            put("Surname", inputDialogView.getSurnamePatternTextField());
            put("Matriculation Nr.", inputDialogView.getMatriculationNumberPatternTextField());
            put("FH Identifier", inputDialogView.getFhIdentifierPatternTextField());
        }};
        StringBuilder errorMessage = new StringBuilder("Please revisit the following inputs:");
        AtomicBoolean inputIsValid = new AtomicBoolean(true);
        patternTextFieldMap.keySet().forEach(patternTextFieldName ->
        {
            inputIsValid.set(inputIsValid.get() & validateAndAppendToErrorMessage(patternTextFieldMap.get(patternTextFieldName), patternTextFieldName, errorMessage));
        });
        if (!inputIsValid.get()) 
        {
            this.showBadInputAlert(errorMessage.toString());
            return inputIsValid.get();
        }
        else 
        {
            try
            {
                this.inputDialogModel.addStudent(
                    inputDialogView.getFirstNamePatternTextField().getText(),
                    inputDialogView.getSurnamePatternTextField().getText(),
                    inputDialogView.getFhIdentifierPatternTextField().getText(),
                    Integer.valueOf(inputDialogView.getMatriculationNumberPatternTextField().getText()));
                return inputIsValid.get();
            }
            catch (SQLException e)
            {
                if (uniquenessCheckAndAlertShow(e))
                {
                    return !inputIsValid.get();
                }
                System.out.println("unique... = false");
                return !inputIsValid.get();
            }
        }
    }

    private boolean uniquenessCheckAndAlertShow(SQLException exception)
    {
        String message = exception.getMessage();
        if (message.contains("UNIQUE constraint failed")) //wenn NICHT UNIQUE
        {
            String matriculationNumberPattern = "Students.matriculation_number";
            String fhIdentityPattern = "Students.fh_identifier";
            // String badInputMessage = "Wert muss einzigartig sein, existiert aber bereits in der Datenbank:";
            String badInputMessage = "Value intended to be unique already exists in the database:";
            if (message.contains(matriculationNumberPattern))
                badInputMessage += "\n- Matriculation Nr.";
            if (message.contains(fhIdentityPattern))
                badInputMessage += "\n- FH Identifier";
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
                    student.getFirstName(),
                    student.getSurname(),
                    student.getFhIdentifier().toLowerCase(),
                    Integer.valueOf(student.getMatriculationNumber()));
            }
            catch (SQLException e)
            {
                e.getStackTrace();
            }
        }
    }
}
