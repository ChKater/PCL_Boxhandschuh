package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.IOException;

import de.luh.hci.pcl.boxhandschuh.model.Combination;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

public class Menu extends GridPane{

	@FXML
	private ChoiceBox<String> user;
	
	@FXML
	private ChoiceBox<Combination> combination;
	
	private Main main;
	
	public Menu(Main main){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Menu.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.main = main;
		this.combination.getItems().addAll(Main.combinations);
		this.combination.getSelectionModel().select(0);
		this.user.getItems().addAll(Main.usernames);
		this.user.getSelectionModel().select(0);

	}
	
	@FXML
	public void train(){
		String username = user.getSelectionModel().getSelectedItem();
		main.showTraining(username, combination.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void profile(){
		main.showProfile(user.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void addUser(){
		main.showNewUser();
	}
}
