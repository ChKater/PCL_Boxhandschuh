package de.luh.hci.pcl.boxhandschuh.main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


	@Override
	public void start(Stage primaryStage) {
		
		try {
//			ArduinoConnection.start();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Main.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root, 1280, 720);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			Stage stage = new Stage();
			Scene train = new Scene(new de.luh.hci.pcl.boxhandschuh.trainingapplication.view.Main(), 1100, 700);
			
			train.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			stage.setScene(train);
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		
	}
}
