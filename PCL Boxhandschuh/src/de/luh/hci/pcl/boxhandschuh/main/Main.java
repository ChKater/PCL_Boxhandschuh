package de.luh.hci.pcl.boxhandschuh.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import de.luh.hci.pcl.boxhandschuh.arduino.ArduinoConnection;
import de.luh.hci.pcl.boxhandschuh.controller.TrainingCombinations;
import de.luh.hci.pcl.boxhandschuh.model.Combination;
import de.luh.hci.pcl.boxhandschuh.model.Gesture;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            ArduinoConnection.start();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

//        Combination one = new Combination("Three fast ones", new Gesture[] { Gesture.PUNCH,Gesture.HOOK,Gesture.UPPERCUT});
//        TrainingCombinations trainer = new TrainingCombinations();
//        trainer.saveCombination(one);

        launch(args);

    }
}
