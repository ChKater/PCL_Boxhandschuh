package de.luh.hci.pcl.boxhandschuh.controller;

import java.util.ArrayList;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.Award;
import de.luh.hci.pcl.boxhandschuh.model.User;
import de.luh.hci.pcl.boxhandschuh.model.UserPojo;
import de.luh.hci.pcl.boxhandschuh.trainingapplication.view.Main;

public class UserController {

	private static String basePath = "users/";

	

	public User getActiveUser() {
		return activeUser;
	}

	private User activeUser;

	private JSONFileWriter fileWriter;

	public UserController() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void login(String username) {
		fileWriter = new JSONFileWriter(basePath + username);

		UserPojo pojo = (UserPojo) fileWriter.read(UserPojo.class);
		
		if (pojo == null) {
			pojo = new UserPojo(username);
			activeUser = new User(pojo);
			saveUser();
		} else {
			activeUser = new User(pojo);
		}

	}

	public void saveUser() {
		if (activeUser != null) {
			fileWriter = new JSONFileWriter(basePath + activeUser.getUsername());
			fileWriter.write(activeUser.getPojo());
		}
	}

	public void logout() {
		saveUser();
		activeUser = null;
	}

	public void checkSpeed(double speed) {
		if (activeUser != null) {
			if (activeUser.getMaxSpeed() < speed) {
				activeUser.setMaxSpeed(speed);
			}
		}
	}

	public void checkForce(double force) {
		if (activeUser != null) {
			if (activeUser.getMaxForce() < force) {
				activeUser.setMaxForce(force);
			}
		}
	}

	public void addPunch(int points, double maxSpeed, double maxForce) {
		checkSpeed(maxSpeed);
		checkForce(maxForce);
		activeUser.setPunchCount(activeUser.getPunchCount() + 1);
		activeUser.setPoints(activeUser.getPoints() + points);
		checkAward();
	}

	public void combinationComplete() {
		activeUser.setCombinationCount(activeUser.getCombinationCount() + 1);
		checkAward();
		saveUser();
	}

	public void checkAward() {
		for (Award award : Main.awards) {
			if (award.conditionsFullfilled(activeUser)) {
				activeUser.addAward(award);
			}
		}
	}
}
