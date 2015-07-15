package de.luh.hci.pcl.boxhandschuh.model;

import javafx.scene.image.Image;

public enum Gesture {
	
	
	
    PUNCH("Punch", new Image("file:img/punch.jpg")),UPPERCUT("Uppercut", new Image("file:img/uppercut.jpg")),HOOK("Haken", new Image("file:img/hook.jpg"));
    
    private Image image;
    private String name;
    
    private Gesture(String name, Image image){
    	this.image = image;
    	this.name = name;
    }

	public Image getImage() {
		return image;
	}
	
	public String getName(){
		return name;
	}
	
	public static Gesture getByName(String name){
		for (Gesture gesture : values()) {
			if(name.equals(gesture.name)){
				return gesture;
			}
		}
		return null;
	}

}
