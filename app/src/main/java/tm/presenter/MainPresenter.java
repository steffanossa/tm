package tm.presenter;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
        openDatabase();
        this.prepareTableView(mainView.getTableView());

        //TODO:move
        selectedStudents = mainView.getTableView().getSelectionModel().getSelectedItems();
        selectedStudents.addListener((ListChangeListener.Change<? extends Student> change) -> {
            boolean isEmpty = selectedStudents.isEmpty();
            mainView.getClipboardButton().setDisable(isEmpty);
            mainView.getRemoveButton().setDisable(isEmpty);
            mainView.getSaveToFileButton().setDisable(isEmpty);
        });
        //handlers
        addAddButtonHandler();
        addRemoveButtonHandler();
        addClipboardButtonHandler();
        addSaveToFileButtonHandler();

        mainView.getRemoveButton().setDisable(true);
        mainView.getClipboardButton().setDisable(true);
        mainView.getSaveToFileButton().setDisable(true);

        mainView.getComboBox().getItems().setAll(mainModel.getSeparatorKeySet());
        mainView.getComboBox().setValue("Komma");

        updatePreviewString();

        mainView.getComboBox().setOnAction(event -> {
            separator = mainModel.getSeparator(mainView.getComboBox().getValue());
            updatePreviewString();
        });

        configContextMenu(mainView.getTableView());
    }

    private void prepareTableView(TableView<Student> tableView) {
        // students = mainModel.extractStudents();

        tableView.getColumns().setAll(
            mainModel.createTableColumn("Vorname", "firstname", String.class),
            mainModel.createTableColumn("Nachname", "surname", String.class),
            mainModel.createTableColumn("Matrikel-Nr.", "matrikelnummer", Integer.class),
            mainModel.createTableColumn("FH-Kennung", "fhKennung", String.class)
        );

        this.updateTableView();

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        tableView.getColumns().addListener((Change<? extends TableColumn<?, ?>> change) -> {
            //TableColumn<?,?> oder eine Unterklasse davon
            updatePreviewString();
        });
    }

    private void updateTableView() {
        students = mainModel.extractStudents();
        mainView.getTableView().setItems(students);
    }

    private void updatePreviewString() {
        String separator = mainModel.getSeparator(mainView.getComboBox().getValue());
        String previewString = mainModel.createPreviewString(separator, mainView.getTableView());
        mainView.getPreviewString().setText(previewString);
        if (previewString == "Nichts anzuzeigen")
            mainView.showImage();
        else mainView.hideImage();
    }

    private boolean showConfirmationDialog(int selectedStudents) {
        ConfirmDeletionAlertView alert = new ConfirmDeletionAlertView(selectedStudents);
        Optional<ButtonType> result = alert.showAndWait();
        //lol
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void configContextMenu(TableView<Student> tableView) {
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

    private void addAddButtonHandler() {
        mainView.getAddButton().setOnAction(event -> {
            InputDialogPresenterInterface inputDialogPresenterInterface = (InputDialogPresenterInterface) new InputDialogPresenter(new InputDialogView(), new InputDialogModel(mainModel.getStudentDAO()));
            inputDialogPresenterInterface.showAndWait();
            updateTableView();
        });
    }

    private void addRemoveButtonHandler() {
        mainView.getRemoveButton().setOnAction(event -> {
            if (showConfirmationDialog(selectedStudents.size())){
            selectedStudents.forEach(student -> {
                mainModel.removeStudent(student);
            });
            updateTableView();
            }
        });
    }
    
    private void addClipboardButtonHandler() {
        mainView.getClipboardButton().setOnAction(event -> {
            mainModel.copyToClipboard(
                selectedStudents,
                mainView.getTableView(),
                MainModel.getColumnGetterMap(),
                separator);
        });
    }

    //TODO:relocate?
    private void addSaveToFileButtonHandler() {
        mainView.getSaveToFileButton().setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text (*.txt)", "*.txt"),
                //TODO:csv option if separator in (",", ";")
                // new FileChooser.ExtensionFilter("CSV-Datei (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("All", "*.*")
            );
            File file = fileChooser.showSaveDialog(mainView.getScene().getWindow());
            if (file != null)
                mainModel.saveTextToFile(
                    file,
                    selectedStudents,
                    mainView.getTableView(),
                    MainModel.getColumnGetterMap(),
                    separator
                );
        });
    }

    //TODO:relocate?
    private void openDatabase () {
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

    private void showBadDatabaseAlert() {
        BadDatabaseAlertView alert = new BadDatabaseAlertView();
        alert.showAndWait();
        openDatabase();
    }

}

