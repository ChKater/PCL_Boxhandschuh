package de.luh.hci.pcl.boxhandschuh.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;


public class Protractor3DView extends GridPane{

	public Protractor3DView(){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Protractor3DView.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
