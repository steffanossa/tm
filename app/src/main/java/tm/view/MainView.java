package tm.view;

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
import tm.model.classes.Student;

public class MainView extends VBox {

    private ToolBar toolBar;
    private Button addButton;
    private Button editButton;
    private Button removeButton;
    private TableView<Student> tableView;
    private ContextMenu contextMenu;
    private Label labelPreviewString;
    private ToolBar buttonBar;
    private ComboBox<String> comboBox;
    private Button clipboardButton;
    private Button saveToFileButton;
    private StackPane tablePane;
    private ImageView imageView;
    //
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem openMenuItem;
    private MenuItem preferencesMenuItem;
    private Menu aboutMenu;
    private MenuItem helpMenuItem;
    private MenuItem aboutMenuItem;


    public MainView()
    {
        this.openMenuItem = new MenuItem("Open Database...");
        this.preferencesMenuItem = new MenuItem("Preferences");
        this.fileMenu = new Menu("File", null, openMenuItem, preferencesMenuItem);
        this.helpMenuItem = new MenuItem("Help");
        this.aboutMenuItem = new MenuItem("About");
        this.aboutMenu = new Menu("About", null, helpMenuItem, aboutMenuItem);
        this.menuBar = new MenuBar(fileMenu, aboutMenu);
        this.addButton = new Button("Add");
        this.removeButton = new Button("Remove");
        this.editButton = new Button("Edit");
        this.toolBar = new ToolBar(addButton, editButton, removeButton);
        this.tableView = new TableView<>();
        this.contextMenu = new ContextMenu();
        this.labelPreviewString = new Label();
        this.labelPreviewString.setStyle("-fx-font-size: 16px;  -fx-translate-x: 6px;");
        this.imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/Grin256_transparent.png")));
        this.tablePane = new StackPane(tableView, imageView);
        //
        
        this.comboBox = new ComboBox<>();
        this.clipboardButton = new Button("Clipboard");
        this.saveToFileButton = new Button("Save as...");
        this.buttonBar = new ToolBar(comboBox, clipboardButton, saveToFileButton);
        
        getChildren().addAll(
            menuBar,
            toolBar,
            tablePane,
            labelPreviewString,
            buttonBar
        );
    }

    public MenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    public MenuItem getPreferencesMenuItem() {
        return preferencesMenuItem;
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
