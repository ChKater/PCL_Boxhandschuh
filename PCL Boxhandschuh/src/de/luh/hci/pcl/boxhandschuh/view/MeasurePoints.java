package de.luh.hci.pcl.boxhandschuh.view;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Punch;

public class MeasurePoints extends GridPane{

	@FXML
	private TableView<MeasurePoint> data;
	
	public MeasurePoints(Punch punch){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"MeasurePoints.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data.setItems(FXCollections.observableArrayList(punch.getMeasurement().getMeasurement()));
	}
}
