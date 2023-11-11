package tm.view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BadDatabaseAlertView extends Alert{
    
    public BadDatabaseAlertView()
    {
        super(Alert.AlertType.INFORMATION);

        setTitle("Fehlerhafte Datenbank");
        setHeaderText(null);

        setContentText(
            "Tabelle: 'Students'\n" +
            "Spalte: 'firstname', Typ: 'TEXT'\n" +
            "Spalte: 'surname', Typ: 'TEXT'\n" +
            "Spalte: 'matrikelnr', Typ: 'INTEGER', 'UNIQUE'\n" +
            "Spalte: 'fhkennung', Typ: 'TEXT', 'UNIQUE'"
        );
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));


        Image image = new Image(getClass().getResourceAsStream("/images/Info.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);
    }
}
