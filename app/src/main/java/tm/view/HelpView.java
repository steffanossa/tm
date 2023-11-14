package tm.view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpView extends Alert {
    
    public HelpView() {
        super(AlertType.INFORMATION);
        setTitle("Help");
        GridPane gridPane = new GridPane();

        Label databaseLabel = new Label("Database");
        databaseLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Label databaseInfo = new Label(
            """
            This app must be used with SQLite databases that
            include a table created with the following statement:
            """
        );
        TextArea createStatement = new TextArea(
            """
            CREATE TABLE "Students" (
                "first_name"	TEXT NOT NULL,
                "surname"	TEXT NOT NULL,
                "matriculation_number"	INTEGER NOT NULL UNIQUE,
                "fh_identifier"	TEXT NOT NULL UNIQUE,
                PRIMARY KEY("matriculation_number")
            )"""
        );
        createStatement.setEditable(false);
        createStatement.setWrapText(false);
        createStatement.setMaxSize(300, 114);
        createStatement.setStyle("-fx-font-size: 10px;");

        Label inputLabel = new Label("Input");
        inputLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label inputInfo = new Label(
            """
            'Firstname' and 'Surname' take any names expressed
            using the extended latin alphabet including
            '-', '\'' and whitespaces.

            'Matriculation Nr.' takes a series of 1 to 9 digits.
            Uniqueness is mandatory.

            'FH Identifier' takes a combination of two standard latin
            letters and six digits.
            Uniqueness is mandatory."""
        );
        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        Separator separator3 = new Separator(Orientation.HORIZONTAL);

        Label tableLabel = new Label("Table\n");
        tableLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Label tableInfo = new Label(
            """
            Reorder the columns by dragging their headers.
            
            Columns can be de-/activated by right-clicking anywhere
            within the table and setting the checks accordinlgy."""

        );
        VBox container = new VBox(databaseLabel, separator1, databaseInfo, createStatement, inputLabel, separator2, inputInfo, tableLabel, separator3, tableInfo);
        ImageView logoImageView = new ImageView(this.getClass().getResource("/images/Question256x512.png").toString());
        logoImageView.setFitWidth(128);
        logoImageView.setPreserveRatio(true);

        
        gridPane.addColumn(0, logoImageView);
        gridPane.addColumn(1, container);
        


        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        
        getDialogPane().setHeaderText(null);
        setGraphic(null);
        
        getDialogPane().contentProperty().set(gridPane);
        
    }
}
