package de.luh.hci.pcl.boxhandschuh.model;

public interface GestureListener {
    public void onGesture(Gesture gesture,double points, double maxSpeed, double maxForce);
}
