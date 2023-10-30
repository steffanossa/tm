package tm.presenter;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import tm.model.InputDialogModel;
import tm.model.MainModel;
import tm.model.Student;
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
        students = mainModel.extractStudents();
        tableView = mainModel.fillTableView(tableView, students);
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
    }

    private boolean showConfirmationDialog(int selectedStudents) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        Image image = new Image(getClass().getResourceAsStream("/images/Confirmation.png"));
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        alert.setTitle("Löschung bestätigen");
        alert.setContentText(selectedStudents + " Einträge wirklich unwiderruflich löschen?");
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        
        Optional<ButtonType> result = alert.showAndWait();
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

    //TODO:am falschen platz
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehlerhafte Datenbank");
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        alert.setContentText(
            "Tabelle: 'Students'\n" +
            "Spalte: 'firstname', Typ: 'TEXT'\n" +
            "Spalte: 'surname', Typ: 'TEXT'\n" +
            "Spalte: 'matrikelnr', Typ: 'INTEGER', 'UNIQUE'\n" +
            "Spalte: 'fhkennung', Typ: 'TEXT', 'UNIQUE'"
            );
        Image image = new Image(getClass().getResourceAsStream("/images/Info.png"));
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.showAndWait();
        openDatabase();
    }
}

