package tm.view;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AboutView extends Dialog<ButtonType> {
    private DialogPane dialogPane;
    private ImageView logoImageView;
    private Label authorLabel;
    private ButtonType okButton;

    public AboutView() {
        this.dialogPane = new DialogPane();
        this.logoImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        this.authorLabel = new Label("hallo");
        this.okButton = new ButtonType("Cool", ButtonBar.ButtonData.OK_DONE);

        this.dialogPane.getChildren().addAll(
            logoImageView,
            authorLabel
        );
        this.dialogPane.getButtonTypes().add(okButton);

        dialogPane.setMinHeight(400);
        setDialogPane(dialogPane);
        
        

        setTitle("About");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
    }
}
