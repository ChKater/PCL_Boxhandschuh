package de.luh.hci.pcl.boxhandschuh.main;

import de.luh.hci.pcl.boxhandschuh.arduino.FakeArduinoConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

public class Main extends Application {
	private static final double MAX_DATA_POINTS = 150;

	@Override
	public void start(Stage primaryStage) {
		
		try {
			FakeArduinoConnection.start();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Main.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root, 800, 600);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}
}
