package tm.view;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tm.model.dtos.StudentDTO;
import tm.view.alerts.ConfirmDeletionAlertView;
import tm.view.alerts.ErrorAlert;
import tm.view.alerts.ExceptionAlert;

/**
 * View for the main window
 */
public class MainView extends VBox {

    private Button addButton;
    private Button editButton;
    private Button removeButton;
    private TableView<StudentDTO> tableView;
    private ContextMenu contextMenu;
    private Label labelPreviewString;
    private ComboBox<String> comboBox;
    private Button clipboardButton;
    private Button saveToFileButton;
    private ImageView imageView;
    //
    private MenuItem helpMenuItem;
    private MenuItem aboutMenuItem;

    private Scene scene;


    public MainView()
    {
        scene = new Scene(this);
        helpMenuItem = new MenuItem("Help");
        aboutMenuItem = new MenuItem("About");
        Menu aboutMenu = new Menu("Help", null, helpMenuItem, aboutMenuItem);
        MenuBar menuBar = new MenuBar(aboutMenu);
        addButton = new Button("Add");
        removeButton = new Button("Remove");
        editButton = new Button("Edit");
        ToolBar toolBar = new ToolBar(addButton, editButton, removeButton);
        tableView = new TableView<>();
        contextMenu = new ContextMenu();
        labelPreviewString = new Label();
        labelPreviewString.setStyle("-fx-font-size: 16px;  -fx-translate-x: 6px;");
        imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/Grin256_transparent.png")));
        StackPane tablePane = new StackPane(tableView, imageView);
        //
        Button tempButton = new Button("ExceptionAlert");
        tempButton.setOnAction(event -> {
            ExceptionAlert alert = new ExceptionAlert(" Resource Not Available or Operator Intervention ", "java.lang.ArithmeticException: / by zero\r\n" + //
                    "\tat Program.foo(main.java:4)\r\n" + //
                    "\tat Program.main(main.java:12)");
            alert.show();
        });
        
        comboBox = new ComboBox<>();
        clipboardButton = new Button("Clipboard");
        saveToFileButton = new Button("Save as...");
        ToolBar buttonBar = new ToolBar(comboBox, clipboardButton, saveToFileButton, tempButton);
        
        getChildren().addAll(
            menuBar,
            toolBar,
            tablePane,
            labelPreviewString,
            buttonBar
        );
    }

    public void initialise(Stage stage) {
        stage.setScene(scene);
        
        stage.setTitle(String.format("Good morning, %s morning",
            LocalDate.now().getDayOfWeek().toString().substring(0, 1) +
            LocalDate.now().getDayOfWeek().toString().substring(1).toLowerCase()));
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setWidth(414);
        stage.setHeight(480);
        
        stage.show();
    }

    public MenuItem getHelpMenuItem() {
        return helpMenuItem;
    }

    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getClipboardButton() {
        return this.clipboardButton;
    }

    public Button getSaveToFileButton() {
        return this.saveToFileButton;
    }

    public Label getLabelPreviewString() {
        return this.labelPreviewString;
    }

    public ComboBox<String> getComboBox() {
        return this.comboBox;
    }

    public TableView<StudentDTO> getTableView() {
        return this.tableView;
    }

    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    public void hideImage() {
        imageView.setVisible(false);
    }

    public void showImage() {
        imageView.setVisible(true);
    }
}
