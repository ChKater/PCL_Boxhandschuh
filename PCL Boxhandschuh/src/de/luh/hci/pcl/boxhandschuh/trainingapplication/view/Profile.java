package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.controller.UserController;
import de.luh.hci.pcl.boxhandschuh.model.Award;
import de.luh.hci.pcl.boxhandschuh.model.User;

public class Profile extends GridPane{
	
	@FXML
	private Label username, punchCount, combinationCount, maxSpeed, maxForce, awards, points;

	private UserController usercontroller;
	
	private Main main;

	public Profile(Main main, String username){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Profile.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.main = main;
		this.usercontroller = new UserController();
		this.usercontroller.login(username);
		User user = this.usercontroller.getActiveUser();
		this.username.setText(user.getUsername());
		this.punchCount.setText("" + user.getPunchCount());
		this.combinationCount.setText("" + user.getCombinationCount());
		this.maxSpeed.setText("" + user.getMaxSpeed() * 3.6 + " km/h");
		this.maxForce.setText("" + user.getMaxForce());
		this.points.setText("" + user.getPoints());
		String newAwards = "";
		if(user.getAwards().size() > 0){
			for (Award award : user.getAwards()) {
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
