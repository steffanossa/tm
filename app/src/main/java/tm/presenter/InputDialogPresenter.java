package tm.presenter;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;

import java.sql.SQLException;

import java.util.Optional;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import tm.customcontrols.PatternTextField;
import tm.model.InputDialogModel;
import tm.model.daos.GenericDAO;
import tm.model.dtos.StudentDTO;
import tm.model.enums.PattyImages;
import tm.presenter.interfaces.InputDialogPresenterInterface;
import tm.presenter.interfaces.MainPresenterInterface;
import tm.view.alerts.GenericAlert;
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
        inputDialogModel    = new InputDialogModel(studentDAO);
        inputDialogView     = new InputDialogView(dialogTitle);
        this.mainPresenter  = mainPresenter;
        addOkButtonAction();
    }

    /**
     * Fills TextFields of the view with the data of the student object provided
     * @param student
     */
    private void fillTextFields(StudentDTO student)
    {
        TextField firstnameTextField        = inputDialogView.getPatternTextFieldByName("firstName");
        TextField lastnameTextField         = inputDialogView.getPatternTextFieldByName("surname");
        TextField matrikelnummerTextField   = inputDialogView.getPatternTextFieldByName("matriculationNumber");
        TextField fhKennungTextField        = inputDialogView.getPatternTextFieldByName("fhIdentifier");

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
    public boolean showAndWait()
    {
        Optional<ButtonType> result = inputDialogView.showAndWait();
        if (result.isPresent() && result.get().getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
            return false;
        }
        return true;
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
        boolean isValid = patternTextField.validate();
        if (!isValid) stringBuilder.append("\n- ")
                                   .append(string); 
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
     * TODO: DAS IST EINE ABSOLUTE KATASTROPHE HIER; MACH WAS
     * validate user input + add data to database if valid
     * @return {@code true} if successful
     */
    private boolean handleOkButtonClick() {
        LinkedHashMap<String, PatternTextField> pTFMap = createPatternTextFieldMap();
        StringBuilder errorMessage = new StringBuilder("Please revisit the following inputs:");
        AtomicBoolean inputIsValid = new AtomicBoolean(true);

        pTFMap.keySet().forEach(pTFName ->
        { inputIsValid.set(inputIsValid.get() & validateAndAppendToErrorMessage(pTFMap.get(pTFName), pTFName, errorMessage)); });
        if (!inputIsValid.get())
        {
            this.showBadInputAlert(errorMessage.toString());
            return inputIsValid.get();
        }
        else 
        {
            int matriculationNumber             = Integer.valueOf(pTFMap.get("Matriculation Nr.").getText());
            String fhIdentifier                 = pTFMap.get("FH Identifier").getText();
            boolean isMatriculationNumberUnique = checkMatriculationNumberUniqueness(matriculationNumber);
            boolean isFhIdentifierUnique = checkFhIdentifierUniqueness(fhIdentifier);
            String badInputMessage = "Value intended to be unique already exists in the database:";

            if (!isMatriculationNumberUnique) badInputMessage += "\n- Matriculation Nr.";
            if (!isFhIdentifierUnique) badInputMessage += "\n- FH Identifier";
            if (!isMatriculationNumberUnique || !isFhIdentifierUnique) {
                showBadInputAlert(badInputMessage);
                return !inputIsValid.get();
            }

            return addStudent(
                pTFMap.get("First Name").getText(),
                pTFMap.get("Surname").getText(),
                matriculationNumber,
                fhIdentifier.toLowerCase());
        }
    }

    private boolean addStudent(String firstName, String surname, int matriculationNumber, String fhIdentifier) {
        try {
            StudentDTO studentDTO = new StudentDTO(firstName, surname, matriculationNumber, fhIdentifier);
            inputDialogModel.addStudent(studentDTO);
            // mainPresenter.getStudentDTOs().add(studentDTO);
            return true;
        } catch (SQLException e) {
            GenericAlert alert = new GenericAlert(Alert.AlertType.ERROR, e.getSQLState(), e.getMessage(), PattyImages.ERROR);
            alert.show();
            return false;
        }
    }

    /**
     * Shows the BadInputDialogView
     * @param message
     */
    private void showBadInputAlert(String message)
    {
        new GenericAlert( Alert.AlertType.INFORMATION, "Bad input", message, PattyImages.ERROR ).show();
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
                inputDialogModel.addStudent(new StudentDTO(
                    student.getFirstName(),
                    student.getSurname(),
                    Integer.valueOf(student.getMatriculationNumber()),
                    student.getFhIdentifier().toLowerCase()
                    ));
            }
            catch (SQLException e) {
                GenericAlert alert = new GenericAlert(Alert.AlertType.ERROR, e.getSQLState(), e.getMessage(), PattyImages.ERROR);
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
