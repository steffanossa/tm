package tm.presenter;

import java.io.File;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import tm.model.MainModel;
import tm.model.dtos.StudentDTO;
import tm.presenter.interfaces.InputDialogPresenterInterface;
import tm.presenter.interfaces.MainPresenterInterface;
import tm.view.AboutView;
import tm.view.HelpView;
import tm.view.MainView;
import tm.view.alerts.BadDatabaseAlertView;
import tm.view.alerts.ConfirmDeletionAlertView;
import tm.view.alerts.ExceptionAlert;
import tm.customcontrols.CheckBoxTableCell;

/**
 * Presenter for the main window
 */
public class MainPresenter implements MainPresenterInterface {
    
    private MainView mainView;
    private MainModel mainModel;

    private String separator;
    private ObservableList<StudentDTO> students;
    private ObservableList<StudentDTO> selectedStudents;
    private boolean toggle;

    public MainPresenter()
    {
        mainModel = new MainModel();
        separator = ",";
        toggle = false;
        mainView = new MainView();
        selectedStudents = FXCollections.observableArrayList();
        prepareAll();
    }

    public void initialise(Stage stage) {
        mainView.initialise(stage);
    }

    /**
     * Prepares the widgets of the MainView
     */
    private void prepareAll() {
        showOpenDatabaseFileWindow();
        prepareTableView(mainView.getTableView());

        selectedStudents.addListener((ListChangeListener.Change<? extends StudentDTO> change) -> updateButtonStates());

        //actions
        addAddButtonAction();
        addEditButtonAction();
        addRemoveButtonAction();
        addClipboardButtonAction();
        addSaveToFileButtonAction();
        //meun
        addReloadMenuiItemAction();
        addHelpMenuItemAction();
        addAboutMenuItemAction();

        updateButtonStates();

        mainView.getComboBox().getItems().setAll(mainModel.getSeparatorKeySet());
        mainView.getComboBox().setValue("Comma");

        updatePreviewString();

        mainView.getComboBox().setOnAction(event ->
        {
            separator = mainModel.getSeparator(mainView.getComboBox().getValue());
            updatePreviewString();
        });

        configContextMenu(mainView.getTableView());
    }

    /**
     * Creates TableColumn to be inserted into TableViews
     * @param <T>
     * @param header column header
     * @param property corresponding property
     * @param valueClass datatype of the values
     * @return TableColumn
     */
    private <T> TableColumn<StudentDTO, T> createTableColumn(String header, String property, Class<T> valueClass)
    {
        TableColumn<StudentDTO, T> tableColumn = new TableColumn<>(header);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(property));
        return tableColumn;
    }

    /**
     * TODO: es werden unendlich viele zeilen in der checkboxcolumn erstellt!
     * Adds TableColumns to the TableView, sets the selection mode to allow multi row selection,
     * adds a listener to the ObservableList of columns of the TableView to update the PreviewString
     * on any changes
     * @param tableView
     */
    private void prepareTableView(TableView<StudentDTO> tableView)
    {
        ArrayList<TableColumn<StudentDTO, ?>> columns = new ArrayList<>();
        
        columns.add(createTableColumn("First name", "firstName", String.class));
        columns.add(createTableColumn("Surname", "surname", String.class));
        columns.add(createTableColumn("Matriculation Nr.", "matriculationNumber", Integer.class));
        columns.add(createTableColumn("FH Identifier", "fhIdentifier", String.class));
        
        TableColumn<StudentDTO, Boolean> checkBoxColumn = createCheckBoxColumn(mainView.getTableView().getItems().size());
        columns.add(checkBoxColumn);

        tableView.getColumns().addAll(columns);

        updateTableView();

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //TableColumn<?,?> oder eine Unterklasse davon
        tableView.getColumns().addListener((Change<? extends TableColumn<?, ?>> change) ->
        {
            updatePreviewString();
        });
    }

    /**
     * TODO: es werden unendlich viele zeilen erstellt!!!
     * Creates a TableColumn with checkboxes
     * @return
     * TODO: too specific
     */
    private TableColumn<StudentDTO, Boolean> createCheckBoxColumn(int numberOfRows) {
        TableColumn<StudentDTO, Boolean> checkBoxColumn = new TableColumn<>("Select");
        checkBoxColumn.setCellValueFactory(cellData -> {
            return null;
        });
        checkBoxColumn.setCellFactory(column -> {
            CheckBoxTableCell<StudentDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.getCheckBox().selectedProperty().addListener((_observale, _oldValue, newValue) -> {
                if (cell.getTableRow() != null && cell.getTableRow().getItem() != null) {
                    handleSelectedRow(
                        cell.getTableRow().getItem(), 
                        newValue);
                }
            });
            return cell;
        });
        checkBoxColumn.setReorderable(false);
        return checkBoxColumn;
    }

    /**
     * @param student
     * @param isSelected
     */
    private void handleSelectedRow(StudentDTO student, boolean isSelected) {
        if (isSelected) selectedStudents.add(student);
        else selectedStudents.remove(student);
    }

    /**
     * En-/Disables the Clipboard-, Remove-, Edit- and SaveToFileButton
     * according to the number of students selected
     */
    private void updateButtonStates()
    {
        boolean isEmpty = selectedStudents.isEmpty();

        mainView.getClipboardButton().setDisable(isEmpty);
        mainView.getRemoveButton().setDisable(isEmpty);
        mainView.getSaveToFileButton().setDisable(isEmpty);

        if (selectedStudents.size() != 1)
            mainView.getEditButton().setDisable(true);
        else mainView.getEditButton().setDisable(false);
    }

    /**
     * Updates the TableView by reretrieving all students from the database etc.
     */
    private void updateTableView()
    {
        ArrayList<StudentDTO> studentsArrayList;
        try {
            studentsArrayList = mainModel.retrieveStudents();
            students = FXCollections.observableArrayList(studentsArrayList);
            mainView.getTableView().setItems(students);
        } catch (SQLException e) {
            ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
            alert.show();
            System.exit(0);
            //TODO
        }
    }

    /**
     * Updates the preview string according to selected separator and columns
     */
    private void updatePreviewString()
    {
        String separator = mainModel.getSeparator(mainView.getComboBox().getValue());
        ArrayList<String> visibleColumns = new ArrayList<>();
        visibleColumns = getVisibleColumns();
        String previewString = mainModel.createPreviewString(separator, visibleColumns);
        mainView.getLabelPreviewString().setText(previewString);
        if (previewString.equals("Nothing to show"))
        {
            mainView.showImage();
            mainView.getTableView().getSelectionModel().clearSelection();
        }
        else mainView.hideImage();
    }

    /**
     * Shows the ShowConfirmDeletionAlert
     * @param amountSelectedStudents amount of rows to be removed
     * @return {@code true} if removal confirmed
     * @see ConfirmDeletionAlertView
     */
    private boolean showConfirmDeletionAlert(int amountSelectedStudents)
    {
        ConfirmDeletionAlertView alert = new ConfirmDeletionAlertView(amountSelectedStudents);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * TODO: toggleAll does not change checkbox states! this could lead to duplicate entries in selectedStudents and is wrong even if not
     * Adds a context menu to the tableview that enables hiding/displaying columns
     * @param tableView
     */
    private void configContextMenu(TableView<StudentDTO> tableView)
    {
        for (TableColumn<StudentDTO, ?> column : tableView.getColumns())
        {
            if (!column.getText().equals("Select")) // meh
            {
                CheckMenuItem menuItem = new CheckMenuItem(column.getText());
                menuItem.setSelected(true);
                menuItem.setOnAction(event ->
                {
                    column.setVisible(menuItem.isSelected());
                    updatePreviewString();
                });
                mainView.getContextMenu().getItems().add(menuItem);
            }
        }
        // dirty, bad, shame, sorry
        CheckMenuItem toggleAll = new CheckMenuItem("Select all");
        toggleAll.setOnAction(event -> {
            if (!toggle) {
                selectedStudents.clear();
                selectedStudents.addAll(students);
            } else {
                selectedStudents.clear();
            }
            toggle = !toggle;
        });
        toggleAll.setSelected(false);
        mainView.getContextMenu().getItems().add(toggleAll);
        tableView.setContextMenu(mainView.getContextMenu());
    }

    /**
     * TODO: direkte datenbank operation
     * instatiasid the inputdialog from where a row can be added to the database
     */
    private void addAddButtonAction() {
        mainView.getAddButton().setOnAction(event -> {
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(
                    mainModel.getStudentDAO(),
                    this,
                    "Add entity");
            inputDialogPresenterInterface.showAndWait();
            mainView.getTableView().refresh();
        });
    }

    /**
     * TODO: hier wird noch direkt aus der datenbank gelöscht
     * Starts up inputdialog with prefilled data from the selected row to be eidterd
     */
    private void addEditButtonAction() {
        mainView.getEditButton().setOnAction(event -> {
            StudentDTO tempStudent = selectedStudents.get(0);
            ArrayList<StudentDTO> studentsArrayList;
            try {
                mainModel.removeStudent(tempStudent);
                studentsArrayList = mainModel.retrieveStudents();
                students = FXCollections.observableArrayList(studentsArrayList);
                InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(
                    mainModel.getStudentDAO(),
                    this,
                    "Edit entity");
            inputDialogPresenterInterface.showAndWaitWithData(tempStudent);
            updateTableView();
            selectedStudents.remove(tempStudent);
            } catch (SQLException e) {
                ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
                alert.show();
                System.exit(0);
            }
            mainView.getTableView().refresh();
        });
    }

    /**
     * Adds button function
     */
    private void addRemoveButtonAction()
    {
        mainView.getRemoveButton().setOnAction(event ->
        {
            if (showConfirmDeletionAlert(selectedStudents.size()))
            {
                ArrayList<StudentDTO> studentsToRemove = new ArrayList<>(selectedStudents);
                studentsToRemove.forEach(student -> {
                    try {
                        mainModel.removeStudent(student);
                        students.remove(student);
                        selectedStudents.remove(student);
                    } catch (SQLException e) {
                        ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
                        alert.show();
                    }
                });
                mainView.getTableView().refresh();
            }
        });
    }

    /**
     * Adds button function
     */
    private void addClipboardButtonAction() 
    {
        mainView.getClipboardButton().setOnAction(event ->
        {
            String concatenatedString = mainModel.concatenate(
                selectedStudents.toArray(new StudentDTO[selectedStudents.size()]),
                getVisibleColumns(),
                separator
            );
            mainModel.copyToClipboard(concatenatedString);
        });
    }

    //TODO:das ziemlcih schei0e?
    private void addSaveToFileButtonAction() 
    {
        mainView.getSaveToFileButton().setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All", "*.*")
            );

            File file = fileChooser.showSaveDialog(mainView.getScene().getWindow());
            if (file != null)
            {
                String concatenatedString = mainModel.concatenate(
                    selectedStudents.toArray(new StudentDTO[selectedStudents.size()]),
                    getVisibleColumns(),
                    separator
                );
                mainModel.writeStringToFile(concatenatedString, file);
            }
        });
    }

    /**
     * Checks all columns of the main TableView for visibility
     * @return the columns that are visible
     */
    private ArrayList<String> getVisibleColumns()
    {
        ArrayList<String> visibleColumns = new ArrayList<>();
        for (TableColumn<StudentDTO, ?> column : mainView.getTableView().getColumns())
        {
            if (!column.getText().equals("Select") && column.isVisible())
                visibleColumns.add(column.getText());
        }
        return visibleColumns;
    }

    //TODO:das ziemlich scheiße
    private void showOpenDatabaseFileWindow ()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("SQLite Database", "*.*")
        );
        File file = fileChooser.showOpenDialog(mainView.getScene().getWindow());
        boolean isAccepted = true;
        if (file != null)
        {
            try
            {
                isAccepted = mainModel.openDatabase(file);
                if (!isAccepted) showBadDatabaseAlert();
            }
            catch (SQLException e) { showBadDatabaseAlert(); }
        }
        else { System.exit(0); }
    }

    /**
     * an ininite loop if you keep on selecting other things than an sqlite database with a structure defiiiinied... elseweher
     */
    private void showBadDatabaseAlert()
    {
        BadDatabaseAlertView alert = new BadDatabaseAlertView();
        alert.showAndWait();
        showOpenDatabaseFileWindow();
    }

    public void showAndWait(){
        System.out.println("unused by now");
    };
    public void hide(){
        System.out.println("unused by now");
    };

    /**
     * Adds button function
     */
    private void addReloadMenuiItemAction() {
        mainView.getReloadMenuItem().setOnAction( unused -> updateTableView() );
    }

    /**
     * Adds button function
     */
    private void addAboutMenuItemAction() {
        mainView.getAboutMenuItem().setOnAction( event -> new AboutView().showAndWait() );
    }

    /**
     * Adds button function
     */
    private void addHelpMenuItemAction() {
        mainView.getHelpMenuItem().setOnAction( event -> new HelpView().showAndWait() );
    }

    @Override
    public ObservableList<StudentDTO> getStudentDTOs() {
        return students;
    }
}

