package tm.view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ConfirmDeletionAlertView extends Alert {
    
    public ConfirmDeletionAlertView (int selectedStudents)
    {
        super(Alert.AlertType.CONFIRMATION);

        setTitle("Confirm removal");
        setHeaderText(null);
        setContentText(
            "Are you sure you want to delete " + selectedStudents + " entity/entities?"
        );

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        Image image = new Image(getClass().getResourceAsStream("/images/Confirmation.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);
    }
}
