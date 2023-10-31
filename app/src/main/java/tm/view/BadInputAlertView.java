package tm.view;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class BadInputAlertView extends Alert{

    public BadInputAlertView(String message) {
        super(Alert.AlertType.ERROR);

        setTitle("Fehlerhafte Eingabe");
        setHeaderText(null);
        setContentText(message);

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        Image image = new Image(getClass().getResourceAsStream("/images/Error.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);
    }
}