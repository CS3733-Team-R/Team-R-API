package edu.wpi.cs3733d18.teamR.controllers;




import edu.wpi.cs3733d18.teamR.database.KioskDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

abstract public class Controller {
    Stage primaryStage;

    public final void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public final void changeUI(Parent screen) {
        Scene scene = primaryStage.getScene();
        scene.setRoot(screen);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public final void goToController(String fxmlpath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(KioskDatabase.class.getResource(fxmlpath));
        Parent screen = loader.load();
        Controller temp = loader.getController();
        temp.primaryStage = primaryStage;
        changeUI(screen);
    }
}

