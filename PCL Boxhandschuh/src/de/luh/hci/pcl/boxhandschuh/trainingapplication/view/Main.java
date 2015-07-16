package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.Award;
import de.luh.hci.pcl.boxhandschuh.model.Combination;
import de.luh.hci.pcl.boxhandschuh.model.Gesture;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Main extends GridPane{

	public static List<Award> awards;
	public static List<Combination> combinations;
	public static List<String> usernames;
	
	private static void init() {
		if (awards == null) {
			awards = new ArrayList<>();
			awards.add(new Award("100 Punkte", 0, 0, 0, 0, 100));

		}
		if(combinations == null){
			combinations = new ArrayList<>();
			combinations.add(new Combination("Test", new Gesture[]{Gesture.PUNCH, Gesture.UPPERCUT, Gesture.HOOK}));
		}
		if(usernames == null){
			usernames = new ArrayList<>();
			for (File file : new File("users").listFiles()) {
				if(file.isFile()){
					usernames.add(file.getName());
				}
			}
		}
	}
	
	public Main(){
		init();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Main.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		showMenu();
	}
	
	public void showMenu(){
		show(new Menu(this));
		usernames = new ArrayList<>();
        for (File file : new File("users").listFiles()) {
            if(file.isFile()){
                usernames.add(file.getName());
            }
        }
	}
	
	public void show(Node node){
		getChildren().clear();
		add(node, 0, 0);
	}

	
	public void showProfile(String username) {
		show(new Profile(this, username));
	}

	public void showTraining(String username, Combination combination) {
		show(new Training(combination, username, this));
		
	}

	

	public void combinationComplete(List<Award> gottenAwards, int points) {
		show(new CombinationComplete(this, gottenAwards, points));
		
	}

	public void showNewUser() {
		show(new NewUser(this));
		
	}
	
	
}
