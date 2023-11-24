package tm.view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Alert informing you that the file chosen is unacceptable
 */
public class BadDatabaseAlertView extends Alert {
    
    public BadDatabaseAlertView()
    {
        super(Alert.AlertType.INFORMATION);

        setTitle("Bad Database");
        setHeaderText(null);

        setContentText(
            "TABLE 'Students' (\n" +
            "'first_name', 'TEXT, NOT NULL'\n" +
            "'surname', Type: 'TEXT, NOT NULL'\n" +
            "'matriculation_number', 'INTEGER, NOT NUL,L UNIQUE'\n" +
            "'fh_identifier', Type: 'TEXT, NOT NULL, UNIQUE'\n" +
            "PRIMARY KEY('matriculation_number'))"
        );
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));


        Image image = new Image(getClass().getResourceAsStream("/images/Info.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);
    }
}
