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
import tm.model.classes.Student;

/**
 * View for the main window
 */
public class MainView extends VBox {

    private Button addButton;
    private Button editButton;
    private Button removeButton;
    private TableView<Student> tableView;
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
        scene = new Scene(this);
        
        comboBox = new ComboBox<>();
        clipboardButton = new Button("Clipboard");
        saveToFileButton = new Button("Save as...");
        ToolBar buttonBar = new ToolBar(comboBox, clipboardButton, saveToFileButton);
        
        getChildren().addAll(
            menuBar,
            toolBar,
            tablePane,
            labelPreviewString,
            buttonBar
        );
    }

    public void initialise(Stage primaryStage) {
        primaryStage.setScene(scene);
        
        primaryStage.setTitle(String.format("Good morning, %s morning",
            LocalDate.now().getDayOfWeek().toString().substring(0, 1) +
            LocalDate.now().getDayOfWeek().toString().substring(1).toLowerCase()));
        
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setWidth(414);
        primaryStage.setHeight(480);
        
        primaryStage.show();
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

    public Label getPreviewString() {
        return this.labelPreviewString;
    }

    public ComboBox<String> getComboBox() {
        return this.comboBox;
    }

    public TableView<Student> getTableView() {
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
