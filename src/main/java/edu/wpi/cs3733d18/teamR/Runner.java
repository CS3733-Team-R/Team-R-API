package edu.wpi.cs3733d18.teamR;

import edu.wpi.cs3733d18.teamR.RaikouAPI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Runner
        extends Application
{
    public static void main(String[] args)
    {
        launch(new String[0]);
    }

    public void start(Stage primaryStage)
            throws Exception
    {
        RaikouAPI raikouAPI = new RaikouAPI();
        raikouAPI.run(100, 30, 900, 600, null, null, null);
    }
}
