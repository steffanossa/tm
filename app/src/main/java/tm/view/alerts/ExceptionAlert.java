package tm.view.alerts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Alert informing about the exception encountered
 */
public class ExceptionAlert extends Alert {

    public ExceptionAlert(String title, String message)
    {
        super(Alert.AlertType.ERROR);

        setTitle("Something bad happened...");
        setHeaderText(title);

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setMaxSize(340, 80);
        GridPane gridPane = new GridPane();
        gridPane.add(textArea, 0, 0);
        gridPane.setPadding(new Insets(10,10,0,10));
        gridPane.setAlignment(Pos.CENTER);
        

        Image image = new Image(getClass().getResourceAsStream("/images/Error.png"));
        ImageView imageView = new ImageView(image);
        setGraphic(imageView);

        getDialogPane().setContent(gridPane);
    }
}