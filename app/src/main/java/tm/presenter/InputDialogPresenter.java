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

import java.util.Optional;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputDialogPresenter implements InputDialogPresenterInterface
{
    private InputDialogView inputDialogView;
    private InputDialogModel inputDialogModel;

    public InputDialogPresenter(InputDialogView inputDialogView, InputDialogModel inputDialogModel)
    {
        this.inputDialogView = inputDialogView;
        this.inputDialogModel = inputDialogModel;
        addOkButtonAction();
    }

    private void fillTextFields(Student student)
    {
        PatternTextField firstnameTextField = inputDialogView.getPatternTextFieldByName("firstName");
        PatternTextField lastnameTextField = inputDialogView.getPatternTextFieldByName("surname");
        PatternTextField matrikelnummerTextField = inputDialogView.getPatternTextFieldByName("matriculationNumber");
        PatternTextField fhKennungTextField = inputDialogView.getPatternTextFieldByName("fhIdentifier");

        firstnameTextField.setText(student.getFirstName());
        lastnameTextField.setText(student.getSurname());
        matrikelnummerTextField.setText(String.valueOf(student.getMatriculationNumber()));
        fhKennungTextField.setText(student.getFhIdentifier());
    }

    private void addOkButtonAction()
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
     * checks a PatternTextField object for validity, appends a string to a stringbuilder object if validation fails
     * @param patternTextField
     * @param string
     * @param stringBuilder
     * @return true if valid
     */
    private boolean validateAndAppendToErrorMessage(PatternTextField patternTextField, String string, StringBuilder stringBuilder)
    {
        //walross?
        boolean isValid = patternTextField.validate();
        if (!isValid) { stringBuilder.append("\n- ").append(string); }
        return isValid;
    }

    /**
     * validate user input + add data to database if valid
     * @return boolean whether successful or not
     */
    private boolean handleOkButtonClick()
    {
        LinkedHashMap<String, PatternTextField> patternTextFieldMap = new LinkedHashMap<>()
        {{
            put("First Name", inputDialogView.getPatternTextFieldByName("firstName"));
            put("Surname", inputDialogView.getPatternTextFieldByName("surname"));
            put("Matriculation Nr.", inputDialogView.getPatternTextFieldByName("matriculationNumber"));
            put("FH Identifier", inputDialogView.getPatternTextFieldByName("fhIdentifier"));
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
                    patternTextFieldMap.get("First Name").getText(),
                    patternTextFieldMap.get("Surname").getText(),
                    patternTextFieldMap.get("FH Identifier").getText(),
                    Integer.valueOf(patternTextFieldMap.get("Matriculation Nr.").getText()));
                return inputIsValid.get();
            }
            catch (SQLException e)
            {
                if (uniquenessCheckAndAlertShow(e))
                {
                    return !inputIsValid.get();
                }
                return !inputIsValid.get();
            }
        }
    }

    private boolean uniquenessCheckAndAlertShow(SQLException exception)
    {
        String message = exception.getMessage();
        if (message.contains("UNIQUE constraint failed"))
        {
            String matriculationNumberPattern = "matriculation_number";
            String fhIdentityPattern = "fh_identifier";
            String badInputMessage = "Value intended to be unique already exists in the database:";
            if (message.contains(matriculationNumberPattern))
                badInputMessage += "\n- Matriculation Nr.";
            if (message.contains(fhIdentityPattern))
                badInputMessage += "\n- FH Identifier";
            showBadInputAlert(badInputMessage); //TODO:relcoate
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
            catch (SQLException e) { e.getStackTrace(); }
        }
    }
}
