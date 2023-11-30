package tm.presenter;

import java.io.File;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableCell;
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

/**
 * Presenter for the main window
 */
public class MainPresenter implements MainPresenterInterface {
    
    private MainView mainView;
    private MainModel mainModel;

    private String separator;
    private ObservableList<StudentDTO> students;
    private Map<StudentDTO, BooleanProperty> checkBoxMap;

    public MainPresenter()
    {
        mainModel = new MainModel();
        separator = ",";
        checkBoxMap = new HashMap<>();
        mainView = new MainView();
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
        students.forEach(student -> checkBoxMap.put(student, new SimpleBooleanProperty(false)));

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

        mainView.getComboBox().setOnAction( _event ->
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
        
        TableColumn<StudentDTO, Boolean> checkBoxColumn = createCheckBoxColumn();
        checkBoxColumn.setReorderable(false);
        
        columns.add(checkBoxColumn);

        tableView.getColumns().addAll(columns);

        updateTableView();

        tableView.getSelectionModel().setCellSelectionEnabled(false);
        //TableColumn<?,?> oder eine Unterklasse davon
        tableView.getColumns().addListener((Change<? extends TableColumn<?, ?>> change) ->
        {
            updatePreviewString();
        });
    }

    /**
     * Creates a Column with CheckBoxes, adds listeners
     * @return
     */
    private TableColumn<StudentDTO, Boolean> createCheckBoxColumn() {
        TableColumn<StudentDTO, Boolean> checkBoxColumn = new TableColumn<>("Selected");
        checkBoxColumn.setCellValueFactory(param -> {
            StudentDTO student = param.getValue();
            BooleanProperty observable = checkBoxMap.get(student);
            if (observable == null) {
                observable = new SimpleBooleanProperty(false);
            }
            observable.addListener((_observable, _oldValue, newValue) -> {
                    updateButtonStates();
                });
            return observable;
        });
        checkBoxColumn.setCellFactory(column ->  {
            return new TableCell<StudentDTO, Boolean>() {
                private final CheckBox checkBox = new CheckBox();
                {
                    checkBox.setOnAction( _event -> {
                        BooleanProperty selectedProperty = (BooleanProperty) getTableColumn().getCellObservableValue(getIndex());
                        if (selectedProperty != null) selectedProperty.set(checkBox.isSelected());
                    });
                }

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }
            };
        });
        return checkBoxColumn;
    }

    /**
     * En-/Disables the Clipboard-, Remove-, Edit- and SaveToFileButton
     * according to the number of students selected
     */
    private void updateButtonStates() {
        boolean isEmpty = checkBoxMap.entrySet().stream()
                                                .filter(key -> key.getValue().get())
                                                .count() < 1;
        mainView.getClipboardButton().setDisable(isEmpty);
        mainView.getRemoveButton().setDisable(isEmpty);
        mainView.getSaveToFileButton().setDisable(isEmpty);

        boolean isSingleEntitySelected = checkBoxMap.entrySet().stream()
                                                               .filter(key -> key.getValue().get())
                                                               .count() == 1;
        mainView.getEditButton().setDisable(!isSingleEntitySelected);    
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
            refreshCheckBoxMap();
            updateButtonStates();
        } catch (SQLException e) {
            ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
            alert.show();
            //System.exit(0);
        }
    }

    private void refreshCheckBoxMap() {
        checkBoxMap.clear();
        students.forEach(student -> checkBoxMap.put(student, new SimpleBooleanProperty(false)));
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
     * TODO: toggleAll is bugged: select all works; unselect all leaves 1 untouched (only in checkBoxMap, checkBox itself stays unchecked).
     * happened with 1000+ entries, worked fine with 10, idk
     * feature disabled for now :(
     * Adds a context menu to the tableview that enables hiding/displaying columns
     * @param tableView
     */
    private void configContextMenu(TableView<StudentDTO> tableView)
    {
        for (TableColumn<StudentDTO, ?> column : tableView.getColumns())
        {
            if (!column.getText().equals("Selected")) // meh
            {
                CheckMenuItem menuItem = new CheckMenuItem(column.getText());
                menuItem.setSelected(true);
                menuItem.setOnAction( _event ->
                {
                    column.setVisible(menuItem.isSelected());
                    updatePreviewString();
                });
                mainView.getContextMenu().getItems().add(menuItem);
            }
        }
        /*
        CheckMenuItem toggleAll = new CheckMenuItem("Select all");
        toggleAll.setOnAction( _event -> {
            boolean selected = !toggleAll.isSelected();
            checkBoxMap.values().forEach(booleanProperty -> booleanProperty.set(!selected));
            checkBoxMap.forEach((_student, value) -> value.set(!selected));
        });
        tableView.refresh();
        toggleAll.setSelected(false);
        mainView.getContextMenu().getItems().add(toggleAll);*/
        tableView.setContextMenu(mainView.getContextMenu());
    }

    /**
     * instatiasid the inputdialog from where a row can be added to the database
     */
    private void addAddButtonAction() {
        mainView.getAddButton().setOnAction( _event -> {
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(
                    mainModel.getStudentDAO(),
                    this,
                    "Add entity");
             if(inputDialogPresenterInterface.showAndWait()) {
                mainView.getTableView().refresh();
                refreshCheckBoxMap();
                updateButtonStates();
            }
        });
    }

    /**
     * Starts up inputdialog with prefilled data from the selected row to be eidterd
     */
    private void addEditButtonAction() {
        mainView.getEditButton().setOnAction( _event -> {
            StudentDTO tempStudent = checkBoxMap.entrySet().stream()
                                                           .filter(key -> key.getValue().get())
                                                           .map(Map.Entry::getKey)
                                                           .findFirst()
                                                           .orElse(null);
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
                checkBoxMap.remove(tempStudent);
            } catch (SQLException e) {
                ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
                alert.show();
            }
            mainView.getTableView().refresh();
        });
    }

    /**
     * Adds button function
     */
    private void addRemoveButtonAction()
    {
        mainView.getRemoveButton().setOnAction( _event ->
        {
            // danger zone
            int amountSelectedStudents = (int) checkBoxMap.entrySet().stream()
                                                               .filter(key -> key.getValue().get())
                                                               .count();
            if (showConfirmDeletionAlert(amountSelectedStudents))
            {
                ArrayList<StudentDTO> studentsToRemove = checkBoxMap.entrySet().stream()
                                                                               .filter(key -> key.getValue().get())
                                                                               .map(Map.Entry::getKey)
                                                                               .collect(Collectors.toCollection(ArrayList::new));
                studentsToRemove.forEach(student -> {
                    try {
                        mainModel.removeStudent(student);
                        students.remove(student);
                        checkBoxMap.remove(student);
                    } catch (SQLException e) {
                        ExceptionAlert alert = new ExceptionAlert(e.getSQLState(), e.getMessage());
                        alert.show();
                    }
                });
                updateButtonStates();
                mainView.getTableView().refresh();
            }
        });
    }

    /**
     * Adds button function
     */
    private void addClipboardButtonAction() 
    {
        mainView.getClipboardButton().setOnAction( _event ->
        {
            String concatenatedString = mainModel.concatenate(
                
                getSelectedStudentsArray(),
                getVisibleColumns(),
                separator
            );
            mainModel.copyToClipboard(concatenatedString);
        });
    }

    /**
     * @return
     */
    private StudentDTO[] getSelectedStudentsArray() {
        return checkBoxMap.entrySet().stream()
                                     .filter(key -> key.getValue().get())
                                     .map(Map.Entry::getKey)
                                     .toArray(StudentDTO[]::new);
    }

    //TODO: relocate
    private void addSaveToFileButtonAction() 
    {
        mainView.getSaveToFileButton().setOnAction((_event) ->
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
                    getSelectedStudentsArray(),
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
            if (!column.getText().equals("Selected") && column.isVisible())
                visibleColumns.add(column.getText());
        }
        return visibleColumns;
    }

    //TODO: relocate
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
                if (!isAccepted) {
                    // showBadDatabaseAlert();
                    new ExceptionAlert("Bad Database selected.\n"+
                                             "You need a table created like this:",
                    """
                    CREATE TABLE "Students" (
                        "first_name"	TEXT NOT NULL,
                        "surname"	TEXT NOT NULL,
                        "matriculation_number"	INTEGER NOT NULL UNIQUE,
                        "fh_identifier"	TEXT NOT NULL UNIQUE,
                        PRIMARY KEY("matriculation_number")
                    )"""
                ).showAndWait();
                showOpenDatabaseFileWindow();
                }
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

    public boolean showAndWait(){
        System.out.println("unused by now");
        return false;
    };
    public void hide(){
        System.out.println("unused by now");
    };

    /**
     * Adds button function
     */
    private void addReloadMenuiItemAction() {
        mainView.getReloadMenuItem().setOnAction( _event -> updateTableView() );
    }

    /**
     * Adds button function
     */
    private void addAboutMenuItemAction() {
        mainView.getAboutMenuItem().setOnAction( _event -> new AboutView().showAndWait() );
    }

    /**
     * Adds button function
     */
    private void addHelpMenuItemAction() {
        mainView.getHelpMenuItem().setOnAction( _event -> new HelpView().showAndWait() );
    }

    @Override
    public ObservableList<StudentDTO> getStudentDTOs() {
        return students;
    }
}

