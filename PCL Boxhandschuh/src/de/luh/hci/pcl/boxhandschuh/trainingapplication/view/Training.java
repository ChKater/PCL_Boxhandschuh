package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.controller.UserController;
import de.luh.hci.pcl.boxhandschuh.model.Award;
import de.luh.hci.pcl.boxhandschuh.model.Combination;
import de.luh.hci.pcl.boxhandschuh.model.Gesture;
import de.luh.hci.pcl.boxhandschuh.model.GestureListener;
import de.luh.hci.pcl.boxhandschuh.model.User;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToGestureFactory;

public class Training extends GridPane implements GestureListener {

	@FXML
	private Label username, points, nextPunch, nextPunchLbl;

	@FXML
	private ImageView nextPunchImage;
	
	@FXML
	private Button pause;

	private Combination combination;
	private int currentGesture;

	private UserController usercontroller;
	private User user;

	private IntegerProperty pointsInRun = new SimpleIntegerProperty(0);
	private MeasurementToGestureFactory gestureFactory;

	private Main main;

	private List<Award> gottenAwards = new ArrayList<>();
	
	private boolean running = true;

	public Training(Combination combination, String username, Main main) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Training.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.combination = combination;
		this.main = main;
		this.usercontroller = new UserController();
		this.usercontroller.login(username);
		this.user = this.usercontroller.getActiveUser();
		updatePunchUI();

		this.username.setText("Spieler: " + username);
		this.points.textProperty().bind(Bindings.createStringBinding(() -> {
			return pointsInRun.get() + " Punkte";
		}, pointsInRun));
		gestureFactory = new MeasurementToGestureFactory(user);
		gestureFactory.addListener(this);
		user.getAwards().addListener((ListChangeListener.Change<? extends Award> c) -> {
			if(c.wasAdded()){
				gottenAwards.addAll(c.getAddedSubList());
			}
		});
	}

	@Override
	public void onGesture(Gesture gesture, double score, double maxSpeed,
			double maxForce) {
		Gesture current = combination.getGestures()[currentGesture];
		int currentPoints = 0;
		if (current == gesture) {
			currentPoints = 100 + (200000 - (int) score) / 100;
		} else {
			currentPoints = (200000 - (int) score) / 100;
		}
		usercontroller.addPunch(currentPoints, maxSpeed, maxForce);
		increasePoints(currentPoints);
		currentGesture++;

		if (currentGesture >= combination.getGestures().length) {
			usercontroller.combinationComplete();
			combinationComplete();
		} else {
			updatePunchUI();
		}

	}

	private void increasePoints(int points) {
		pointsInRun.set(pointsInRun.get() + points);
	}

	private void updatePunchUI() {
		Gesture current = combination.getGestures()[currentGesture];
		nextPunch.setText(current.getName());
		nextPunchImage.setImage(current.getImage());
		nextPunchImage.setFitHeight(400);
		nextPunchImage.setFitWidth(300);

	}

	private void combinationComplete() {
		gestureFactory.removeListener(this);
		gestureFactory.disconnect();
		main.combinationComplete(gottenAwards, pointsInRun.get());
	}
	
	@FXML
	public void pause(){
		running = !running;
		if(running){
			disable(false);
			pause.setText("Pauseieren");
			gestureFactory.addListener(this);

		} else {
			disable(true);
			pause.setText("Weiter");
			gestureFactory.removeListener(this);
		}
		
	}
	
	private void disable(boolean disabled) {
		username.setDisable(disabled);
		points.setDisable(disabled);
		nextPunch.setDisable(disabled);
		nextPunchImage.setDisable(disabled);
		nextPunchLbl.setDisable(disabled);
	}

	@FXML
	public void cancel(){
		main.showMenu();
	}
}
