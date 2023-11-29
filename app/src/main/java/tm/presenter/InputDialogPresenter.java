package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;

import java.sql.SQLException;

import java.util.Optional;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import tm.customcontrols.PatternTextField;
import tm.model.InputDialogModel;
import tm.model.daos.GenericDAO;
import tm.model.dtos.StudentDTO;
import tm.presenter.interfaces.InputDialogPresenterInterface;
import tm.view.alerts.ErrorAlert;
import tm.view.alerts.ExceptionAlert;
import tm.view.dialogs.InputDialogView;


/**
 * Presenter for the input dialog window
 */
public class InputDialogPresenter implements InputDialogPresenterInterface
{
    private InputDialogView inputDialogView;
    private InputDialogModel inputDialogModel;
    private MainPresenterInterface mainPresenter;
    
    public InputDialogPresenter(
            GenericDAO<StudentDTO> studentDAO,
            MainPresenterInterface mainPresenter,
            String dialogTitle) {
        inputDialogModel = new InputDialogModel(studentDAO);
        inputDialogView = new InputDialogView(dialogTitle);
        this.mainPresenter = mainPresenter;
        addOkButtonAction();
    }

    /**
     * Fills PatternTextFields of the view with the data of the student object provided
     * @param student
     */
    private void fillTextFields(StudentDTO student)
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

    /**
     * TODO: hmm
     */
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
     * @return {@code true} if valid
     */
    private boolean validateAndAppendToErrorMessage(PatternTextField patternTextField, String string, StringBuilder stringBuilder)
    {
        //walross?
        boolean isValid = patternTextField.validate();
        if (!isValid) stringBuilder.append("\n- ").append(string); 
        return isValid;
    }

    /**
     * 
     * @return Title string as key, PatternTexfield as value
     */
    private LinkedHashMap<String, PatternTextField> createPatternTextFieldMap() {
        return new LinkedHashMap<>()
        {{
            put("First Name", inputDialogView.getPatternTextFieldByName("firstName"));
            put("Surname", inputDialogView.getPatternTextFieldByName("surname"));
            put("Matriculation Nr.", inputDialogView.getPatternTextFieldByName("matriculationNumber"));
            put("FH Identifier", inputDialogView.getPatternTextFieldByName("fhIdentifier"));
        }};
    }

    /**
     * validate user input + add data to database if valid
     * @return {@code true} if successful
     */
    private boolean handleOkButtonClick() {
        LinkedHashMap<String, PatternTextField> patternTextFieldMap = createPatternTextFieldMap();
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
            boolean isMatriculationNumberUnique = checkMatriculationNumberUniqueness(Integer.valueOf(patternTextFieldMap.get("Matriculation Nr.").getText()));
            boolean isFhIdentifierUnique = checkFhIdentifierUniqueness(patternTextFieldMap.get("FH Identifier").getText());
            String badInputMessage = "Value intended to unique already exists in the database:";

            if (!isMatriculationNumberUnique) badInputMessage += "\n- Matriculation Nr.";

            if (!isFhIdentifierUnique) badInputMessage += "\n- FH Identifier";
            
            if (!isMatriculationNumberUnique || !isFhIdentifierUnique) {
                showBadInputAlert(badInputMessage);
                return !inputIsValid.get();
            }
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
                ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
                alert.show();
                return !inputIsValid.get();
            }
        }
    }

    /**
     * Shows the BadInputDialogView
     * @param message
     */
    private void showBadInputAlert(String message)
    {
        ErrorAlert alert = new ErrorAlert("Bad input", message);
        alert.show();
    }

    /**
     * Shows the InputDialogView and fills it with the data of the provided student
     */
    @Override
    public void showAndWaitWithData(StudentDTO student)
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
            catch (SQLException e) {
                ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
                alert.show();
            }
        }
    }

    private boolean checkMatriculationNumberUniqueness(int matriculationNumber) {
        boolean isUnique = !mainPresenter.getStudentDTOs().stream().anyMatch(student -> student.getMatriculationNumber() == matriculationNumber);
        return isUnique;
    }

    private boolean checkFhIdentifierUniqueness(String fhIdentifier) {
        boolean isUnique = !mainPresenter.getStudentDTOs().stream().anyMatch(student -> student.getFhIdentifier().equals(fhIdentifier));
        return isUnique;
    }
}
