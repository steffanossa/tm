package tm.view.alerts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tm.model.enums.PattyImages;

public class GenericAlert extends Alert{
    
    public GenericAlert (
        AlertType alertType,
        String title,
        String message,
        PattyImages pattyImage)
        {
            super(alertType);
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream(PattyImages.LOGO.getPath())));

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(pattyImage.getPath())));
            setGraphic(imageView);
            if (alertType.equals(Alert.AlertType.ERROR)) {
                createErrorContent(title, message);
            } else {
                setTitle(title);
                setHeaderText(null);
                setContentText(message);
            }
    }

    private void createErrorContent(String title, String message) {
        setTitle("Something bad happened...");
        setHeaderText(title);
        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setMaxSize(340, 80);
        GridPane gridPane = new GridPane();
        gridPane.add(textArea, 0, 0);
        gridPane.setPadding(new Insets(10,10,0,10));
        gridPane.setAlignment(Pos.CENTER);
        getDialogPane().setContent(gridPane);
    }
}
