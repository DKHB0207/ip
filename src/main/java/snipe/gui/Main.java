package snipe.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import snipe.core.Snipe;

/**
 * A GUI for Snipe using FXML.
 */
public class Main extends Application {

    private Snipe snipe = new Snipe("src/main/txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            stage.setMinHeight(220);
            stage.setMinWidth(417);

            fxmlLoader.<MainWindow>getController().setSnipe(snipe);  // inject the Snipe instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
