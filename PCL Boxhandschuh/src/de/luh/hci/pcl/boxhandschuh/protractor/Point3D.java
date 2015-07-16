package de.luh.hci.pcl.boxhandschuh.protractor;

public class Point3D {

	public double x,y,z;

	public Point3D(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D copy(){
		return new Point3D(x, y, z);
	}
	
	@Override
	public String toString() {
	    return "(" + x +", " + y + ", " +z + ")";
	}
}
