package tm.view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ConfirmDeletionAlertView extends Alert {
    
    public ConfirmDeletionAlertView (int selectedStudents)
    {
        super(Alert.AlertType.CONFIRMATION);

        setTitle("Löschen bestätigen");
        setHeaderText(null);
        setContentText(
            selectedStudents + " Beobachtung/en wirklich unwiderruflich entfernen?"
        );

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        Image image = new Image(getClass().getResourceAsStream("/images/Confirmation.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);
    }
}
