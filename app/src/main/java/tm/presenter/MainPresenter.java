package tm.presenter;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ArrayList;

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

import tm.model.InputDialogModel;
import tm.model.MainModel;
import tm.model.classes.Student;
import tm.presenter.interfaces.GenericPresenterInterface;
import tm.presenter.interfaces.InputDialogPresenterInterface;
import tm.view.InputDialogView;
import tm.view.MainView;
import tm.view.alerts.BadDatabaseAlertView;
import tm.view.alerts.ConfirmDeletionAlertView;


public class MainPresenter implements GenericPresenterInterface {
    
    private MainView mainView;
    private MainModel mainModel;

    private String separator;
    private ObservableList<Student> students;
    private ObservableList<Student> selectedStudents;

    public MainPresenter(
        MainView mainView,
        MainModel mainModel
    ) {
        this.mainModel = mainModel;
        this.separator = ",";
        this.mainView = mainView;
        this.prepareAll();
    }

    private void prepareAll() {
        showOpenDatabaseFileWindow();
        this.prepareTableView(mainView.getTableView());

        selectedStudents = mainView.getTableView().getSelectionModel().getSelectedItems();

        selectedStudents.addListener((ListChangeListener.Change<? extends Student> change) -> updateButtonStates());

        //actions
        addAddButtonAction();
        addEditButtonAction();
        addRemoveButtonAction();
        addClipboardButtonAction();
        addSaveToFileButtonAction();

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
     * @param header Column header
     * @param property corresponding property
     * @param valueClass datatype of the values
     * @return
     */
    public <T> TableColumn<Student, T> createTableColumn(String header, String property, Class<T> valueClass)
    {
        TableColumn<Student, T> tableColumn = new TableColumn<>(header);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(property));
        return tableColumn;
    }

    private void prepareTableView(TableView<Student> tableView)
    {
        ArrayList<TableColumn<Student, ?>> columns = new ArrayList<>();
        columns.add(createTableColumn("First name", "firstName", String.class));
        columns.add(createTableColumn("Surname", "surname", String.class));
        columns.add(createTableColumn("Matriculation Nr.", "matriculationNumber", Integer.class));
        columns.add(createTableColumn("FH Identifier", "fhIdentifier", String.class));

        tableView.getColumns().addAll(columns);

        updateTableView();

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        tableView.getColumns().addListener((Change<? extends TableColumn<?, ?>> change) ->
        {
            //TableColumn<?,?> oder eine Unterklasse davon
            updatePreviewString();
        });
    }

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

    private void updateTableView()
    {
        ArrayList<Student> studentsArrayList = mainModel.retrieveStudents();
        students = FXCollections.observableArrayList(studentsArrayList);
        mainView.getTableView().setItems(students);
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
        mainView.getPreviewString().setText(previewString);
        if (previewString.equals("Nothing to show"))
        {
            mainView.showImage();
            mainView.getTableView().getSelectionModel().clearSelection();
        }
        else mainView.hideImage();
    }

    /**
     * asks for confirmation on whether or not to remove the selected rows
     * @param selectedStudents amount of rows to be removed
     * @return removal confirmed?
     */
    private boolean showConfirmationDialog(int selectedStudents)
    {
        ConfirmDeletionAlertView alert = new ConfirmDeletionAlertView(selectedStudents);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * adds a context menu to the tableview that enables hiding/showing columns
     * @param tableView
     */
    private void configContextMenu(TableView<Student> tableView)
    {
        for (TableColumn<Student, ?> column : tableView.getColumns())
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
       tableView.setContextMenu(mainView.getContextMenu());
    }

    /**
     * instatiasid the inputdialog from where a row can be added to the database
     */
    private void addAddButtonAction()
    {
        mainView.getAddButton().setOnAction(event ->
        {
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(new InputDialogView("Add entity"), new InputDialogModel(mainModel.getStudentDAO()));
            inputDialogPresenterInterface.showAndWait();
            updateTableView();
        });
    }

    /**
     * starts up inputdialog with prefilled data from the selected row to be eidterd
     */
    private void addEditButtonAction()
    {
        mainView.getEditButton().setOnAction(event ->
        {
            Student tempStudent = selectedStudents.get(0);
            mainModel.removeStudent(tempStudent);
            ArrayList<Student> studentsArrayList = mainModel.retrieveStudents();
            students = FXCollections.observableArrayList(studentsArrayList);
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(new InputDialogView("Edit entity"), new InputDialogModel(mainModel.getStudentDAO()));
            
            inputDialogPresenterInterface.showAndWaitWithData(tempStudent);
            

            
            updateTableView();
        });
    }

    private void addRemoveButtonAction()
    {
        mainView.getRemoveButton().setOnAction(event ->
        {
            if (showConfirmationDialog(selectedStudents.size()))
            {
            selectedStudents.forEach(student ->
            {
                mainModel.removeStudent(student);
            });
            updateTableView();
            }
        });
    }
    
    private void addClipboardButtonAction() 
    {
        mainView.getClipboardButton().setOnAction(event ->
        {
            Student[] selected = selectedStudents.toArray(new Student[selectedStudents.size()]);
            ArrayList<String> visibleColumns = new ArrayList<>();
            visibleColumns = getVisibleColumns();
            mainModel.copyToClipboard(
                selected,
                visibleColumns,
                MainModel.getColumnGetterMap(),
                separator);
        });
    }

    //TODO:relocate?
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
                Student[] selected = selectedStudents.toArray(new Student[selectedStudents.size()]);
                ArrayList<String> visibleColumns = new ArrayList<>();
                visibleColumns = getVisibleColumns();
                mainModel.saveTextToFile(
                    file,
                    selected,
                    visibleColumns,
                    MainModel.getColumnGetterMap(),
                    separator
                );
            }
        });
    }

    private ArrayList<String> getVisibleColumns()
    {
        ArrayList<String> visibleColumns = new ArrayList<>();
        for (TableColumn<Student, ?> column : mainView.getTableView().getColumns())
        {
            if (column.isVisible())
                visibleColumns.add(column.getText());
        }
        return visibleColumns;
    }

    //TODO:relocate?
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
        // unused by now
    };
    public void hide(){
        //unused by now
    };
}

