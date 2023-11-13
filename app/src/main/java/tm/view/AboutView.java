package tm.view;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class AboutView extends VBox {
    private GridPane gridPane;
    private ImageView logoImageView;
    private Label textArea;
    private Hyperlink hyperlink;
    private Button okButton;
    private VBox vbox;

    public AboutView() {
        this.gridPane = new GridPane();
        this.logoImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/Logo256.png")));
        this.logoImageView.setFitHeight(128);
        this.logoImageView.setPreserveRatio(true);
        logoImageView.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/steffanossa/tm"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }
        });
        logoImageView.setOnMouseEntered(event -> {
            logoImageView.setCursor(javafx.scene.Cursor.HAND);
        });
        logoImageView.setOnMouseExited(event -> {
            logoImageView.setCursor(javafx.scene.Cursor.DEFAULT);
        });

        

        Tooltip tooltip = new Tooltip("Visit github");
        Tooltip.install(logoImageView, tooltip);
        this.textArea = new Label("Wir sind alle Kinder Gottes\nJeder wirft sein Stöckchen so weit,\nwie er kann und hofft,\nsein Hund schafft es.");
        
        this.okButton = new Button("Cool");

        this.gridPane.addColumn(0, logoImageView);
        this.gridPane.addColumn(1, textArea);
        
        getChildren().setAll(gridPane);
        
        
    }

    public ImageView getImageView() {
        return this.logoImageView;
    }
}
