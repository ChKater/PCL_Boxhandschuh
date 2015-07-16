package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.IOException;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.Award;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CombinationComplete extends GridPane{

	@FXML
	private Label points, awards, awardsLbl;
	private Main main;
	
	
	


	public CombinationComplete(Main main, List<Award> gottenAwards, int points) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"CombinationComplete.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.main = main;
		this.points.setText(String.valueOf(points));
		if(gottenAwards.size() > 0){
			awards.setVisible(true);
			awardsLbl.setVisible(true);
			String newAwards = "";
			for (Award award : gottenAwards) {
				newAwards += award + ", ";
			}
			newAwards = newAwards.substring(0, newAwards.length() - 2);
			awards.setText(newAwards);
		}
	}
	
	@FXML
	public void cancel(){
		main.showMenu();
	}
}
