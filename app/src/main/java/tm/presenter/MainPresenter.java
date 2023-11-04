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
import tm.model.Student;
import tm.view.BadDatabaseAlertView;
import tm.view.ConfirmDeletionAlertView;
import tm.view.InputDialogView;
import tm.view.MainView;


public class MainPresenter {
    
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
        //TODO:move separator to model?
        this.separator = ",";
        this.mainView = mainView;
        this.prepareAll();
    }

    private void prepareAll() {
        showOpenDatabaseFileWindow();
        this.prepareTableView(mainView.getTableView());

        //TODO:move
        selectedStudents = mainView.getTableView().getSelectionModel().getSelectedItems();

        selectedStudents.addListener((ListChangeListener.Change<? extends Student> change) -> {
            updateButtonStates();
        });

        //handlers
        addAddButtonAction();
        addEditButtonActoin();
        addRemoveButtonAction();
        addClipboardButtonAction();
        addSaveToFileButtonAction();

        updateButtonStates();

        mainView.getComboBox().getItems().setAll(mainModel.getSeparatorKeySet());
        mainView.getComboBox().setValue("Komma");

        updatePreviewString();

        mainView.getComboBox().setOnAction(event -> {
            separator = mainModel.getSeparator(mainView.getComboBox().getValue());
            updatePreviewString();
        });

        configContextMenu(mainView.getTableView());
    }

    //java is ugly
    public <T> TableColumn<Student, T> createTableColumn(String header, String property, Class<T> valueClass)
    {
        TableColumn<Student, T> tableColumn = new TableColumn<>(header);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(property));
        return tableColumn;
    }

    private void prepareTableView(TableView<Student> tableView)
    {
        ArrayList<TableColumn<Student, ?>> columns = new ArrayList<>();
        columns.add(createTableColumn("Vorname", "firstname", String.class));
        columns.add(createTableColumn("Nachname", "surname", String.class));
        columns.add(createTableColumn("Matrikel-Nr.", "matrikelnummer", Integer.class));
        columns.add(createTableColumn("FH-Kennung", "fhKennung", String.class));

        tableView.getColumns().addAll(columns);

        updateTableView();

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        tableView.getColumns().addListener((Change<? extends TableColumn<?, ?>> change) -> {
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

    private void updatePreviewString()
    {
        String separator = mainModel.getSeparator(mainView.getComboBox().getValue());
        ArrayList<String> visibleColumns = new ArrayList<>();
        visibleColumns = getVisibleColumns();
        String previewString = mainModel.createPreviewString(separator, visibleColumns);
        mainView.getPreviewString().setText(previewString);
        if (previewString == "Nichts anzuzeigen") {
            mainView.showImage();
            mainView.getTableView().getSelectionModel().clearSelection();
        } else mainView.hideImage();
    }

    private boolean showConfirmationDialog(int selectedStudents)
    {
        ConfirmDeletionAlertView alert = new ConfirmDeletionAlertView(selectedStudents);
        Optional<ButtonType> result = alert.showAndWait();
        //lol
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void configContextMenu(TableView<Student> tableView)
    {
        for (TableColumn<Student, ?> column : tableView.getColumns()) {
            CheckMenuItem menuItem = new CheckMenuItem(column.getText());
            menuItem.setSelected(true);
            menuItem.setOnAction(event -> {
                column.setVisible(menuItem.isSelected());
                updatePreviewString();
            });
            mainView.getContextMenu().getItems().add(menuItem);
        }
       tableView.setContextMenu(mainView.getContextMenu());
    }

    private void addAddButtonAction()
    {
        mainView.getAddButton().setOnAction(event -> {
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(new InputDialogView(), new InputDialogModel(mainModel.getStudentDAO()));
            inputDialogPresenterInterface.showAndWait();
            updateTableView();
        });
    }

    private void addEditButtonActoin()
    {
        mainView.getEditButton().setOnAction(event -> {
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(new InputDialogView(), new InputDialogModel(mainModel.getStudentDAO()));
            //TODO:fill textFields with selectedStudent's (can only be 1) values
        });
    }

    private void addRemoveButtonAction()
    {
        mainView.getRemoveButton().setOnAction(event -> {
            if (showConfirmationDialog(selectedStudents.size())){
            selectedStudents.forEach(student -> {
                mainModel.removeStudent(student);
            });
            updateTableView();
            }
        });
    }
    
    private void addClipboardButtonAction() 
    {
        mainView.getClipboardButton().setOnAction(event -> {
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
        mainView.getSaveToFileButton().setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text (*.txt)", "*.txt"),
                //TODO:csv option if separator in (",", ";")
                // new FileChooser.ExtensionFilter("CSV-Datei (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("All", "*.*")
            );

            File file = fileChooser.showSaveDialog(mainView.getScene().getWindow());
            if (file != null){
                Student[] selected = selectedStudents.toArray(new Student[selectedStudents.size()]);
                ArrayList<String> visibleColumns = new ArrayList<>();
                visibleColumns = getVisibleColumns();
                mainModel.saveTextToFile(
                    file,
                    selected,
                    visibleColumns,
                    MainModel.getColumnGetterMap(),
                    separator
                );}
        });
    }

    private ArrayList<String> getVisibleColumns()
    {
        ArrayList<String> visibleColumns = new ArrayList<>();
        for (TableColumn<Student, ?> column : mainView.getTableView().getColumns()) {
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
        if (file != null) {
            try {
                isAccepted = mainModel.openDatabase(file);
                if (!isAccepted) {
                    showBadDatabaseAlert();
                }
            } catch (SQLException e) {
                showBadDatabaseAlert();
            }
        } else {
            //ob das so sauber ist?
            System.exit(0);
        }
    }

    private void showBadDatabaseAlert()
    {
        BadDatabaseAlertView alert = new BadDatabaseAlertView();
        alert.showAndWait();
        showOpenDatabaseFileWindow();
    }
}

