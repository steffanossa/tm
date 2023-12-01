package tm.view;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tm.model.enums.PattyImages;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * About window for this application
 */
public class AboutView extends Alert {
    
    public AboutView() {
        super(AlertType.INFORMATION);
        setTitle("About");
        GridPane gridPane = new GridPane();
        Label headerLabel = new Label(
            "Transfermodul Programmieraufgabe\n" +
            "by steffanossa"
        );
        headerLabel.setStyle("-fx-font-size: 14px;");
        Label infoLabel = new Label(
            "JavaFX\t\t21.0.1\n" +
            "SQLite-JDBC\t3.34.0"
        );
        ImageView logoImageView = new ImageView(this.getClass().getResource(PattyImages.LOGO.getPath()).toString());
        
        // weil transparente pixel ignoriert werden
        Pane coverPane = new Pane();
        coverPane.setMouseTransparent(true);
        coverPane.setPrefSize(logoImageView.getFitWidth(), logoImageView.getFitHeight());
        StackPane stackPane = new StackPane(coverPane, logoImageView);
        stackPane.setOnMouseEntered(event -> coverPane.getScene().setCursor(javafx.scene.Cursor.HAND));
        stackPane.setOnMouseExited(event -> coverPane.getScene().setCursor(javafx.scene.Cursor.DEFAULT));
        stackPane.setOnMouseClicked(event ->
        {
            try { Desktop.getDesktop().browse(new URI("https://www.github.com/steffanossa/tm")); }
            catch (IOException e) {}
            catch (URISyntaxException e) {}
        });
        
        Tooltip tooltip = new Tooltip("Visit project on GitHub");
        Tooltip.install(stackPane, tooltip);


        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream(PattyImages.LOGO.getPath())));

        gridPane.addColumn(0, headerLabel);
        gridPane.addColumn(1, stackPane);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        getDialogPane().headerProperty().set(gridPane);
        
        getDialogPane().contentProperty().set(infoLabel);
        
    }
}
