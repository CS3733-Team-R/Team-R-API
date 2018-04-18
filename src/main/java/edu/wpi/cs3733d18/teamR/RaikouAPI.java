package edu.wpi.cs3733d18.teamR;

import edu.wpi.cs3733d18.teamR.controllers.Controller;
import edu.wpi.cs3733d18.teamR.database.KioskDatabase;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class RaikouAPI

{
    public void thing(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath) throws IOException {
        //Platform.setImplicitExit(false);
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(KioskDatabase.class.getResource("/fxml/HubUI.fxml"));
        Parent menuScreen = loader.load();
        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        Scene scene = new Scene(menuScreen, windowWidth, windowLength);
       // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(xcoord);
        primaryStage.setY(ycoord);
        primaryStage.setTitle("Raikou Perscription Request");
        primaryStage.setScene(scene);

        if(cssPath != null) {
             primaryStage.getScene().getStylesheets().add(getClass().getResource("/css/all.css").toExternalForm());
        }
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        System.out.println("Jar is updated");
        primaryStage.setOnCloseRequest(e ->{ KioskDatabase.getInstance().close();});
        //primaryStage.setFullScreen(true);
        primaryStage.show();



        System.out.println("\nEverything was instantiated correctly!");

    }

    public void run(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath, String destNodeID, String originNode)
            throws ServiceException
    {
        String cssFile = cssPath;
        if ((xcoord < 0) || (ycoord < 0) || (windowWidth < 0) || (windowLength < 0)) {
            throw new ServiceException("There cannot be a negative value in the parameter of run().");
        }
        if (xcoord > windowWidth) {
            throw new ServiceException("Xcoord is out of the bounds of the window.");
        }
        if (ycoord > windowLength) {
            throw new ServiceException("Ycoord is out of the bounds of the window.");
        }
        if (cssPath == null) {
           // cssFile = "/Boundary2/APIStyle.css";
        }
        try {
            thing(xcoord, ycoord, windowWidth, windowLength, cssFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        //throw new RuntimeException();
        KioskDatabase.getInstance().close();
        //Platform.exit();
       // System.exit(0);
    }
}