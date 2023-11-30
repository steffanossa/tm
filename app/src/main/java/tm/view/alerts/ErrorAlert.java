package tm.view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Alert informing about why the data put in is bad
 */
public class ErrorAlert extends Alert {

    public ErrorAlert(String title, String message)
    {
        super(Alert.AlertType.ERROR);

        setTitle(title);
        setHeaderText(null);
        setContentText(message);

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        Image image = new Image(getClass().getResourceAsStream("/images/Error.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);
    }
}