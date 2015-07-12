package de.luh.hci.pcl.boxhandschuh.model;

public class Combination {
    
    private String name;
    private Gesture[] gestures;
    public Combination(String name, Gesture[] gestures) {
        super();
        this.name = name;
        this.gestures = gestures;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Gesture[] getGestures() {
        return gestures;
    }
    public void setGestures(Gesture[] gestures) {
        this.gestures = gestures;
    }
    
}
