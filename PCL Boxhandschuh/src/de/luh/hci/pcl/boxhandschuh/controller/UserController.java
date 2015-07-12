package de.luh.hci.pcl.boxhandschuh.controller;

import de.luh.hci.pcl.boxhandschuh.model.User;

public class UserController {

    private static String basePath = "data/users/";

    private User activeUser;

    private JSONFileWriter fileWriter;

    private Boolean speedAward = false;

    private Boolean forceAward = false;

    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void login(String username) {
        fileWriter = new JSONFileWriter(basePath + username);

        try {
            activeUser = (User) fileWriter.read(User.class);

            if (activeUser == null) {
                throw new Exception("no user saved!");
            }

        } catch (Exception e) {
            // TODO: handle exception
            User newUSer = new User(username);
            activeUser = newUSer;
            saveUser();
        }

    }

    public void saveUser() {
        if (activeUser != null) {
            fileWriter = new JSONFileWriter(basePath + activeUser.getUsername());
            fileWriter.write(activeUser);
        }
    }

    public void logout() {
        saveUser();
        activeUser = null;
        forceAward = false;
        speedAward = false;
    }

    public void checkSpeed(double speed) {
        if (activeUser != null) {
            if (activeUser.getMaxSpeed() < speed) {
                activeUser.setMaxSpeed(speed);
                speedAward = true;
            }
        }
    }

    public void checkForce(double force) {
        if (activeUser != null) {
            if (activeUser.getMaxForce() < force) {
                activeUser.setMaxForce(force);
                forceAward = true;
            }
        }
    }

    public Boolean getSpeedAward() {
        return speedAward;
    }

    public Boolean getForceAward() {
        return forceAward;
    }

}
