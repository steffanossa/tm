package tm.view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
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


    public MainView()
    {    
        this.toolBar = new ToolBar();
        this.addButton = new Button("Add");
        this.removeButton = new Button("Remove");
        this.editButton = new Button("Edit");
        this.tableView = new TableView<>();
        this.contextMenu = new ContextMenu();
        this.labelPreviewString = new Label();
        this.labelPreviewString.setStyle("-fx-font-size: 16px;  -fx-translate-x: 6px;");
        this.buttonBar = new ToolBar();
        this.tablePane = new StackPane();
        this.imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/Grin256_transparent.png")));

        this.comboBox = new ComboBox<>();
        this.clipboardButton = new Button("Clipboard");
        this.saveToFileButton = new Button("Save as...");
        
        //
        this.toolBar.getItems().addAll(
            addButton,
            editButton,
            removeButton
        );
        this.buttonBar.getItems().addAll(
            comboBox,
            clipboardButton,
            saveToFileButton
        );
        
        getChildren().addAll(
            // menuBar,
            toolBar,
            tablePane,
            labelPreviewString,
            buttonBar
        );
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
