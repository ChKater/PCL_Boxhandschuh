package de.luh.hci.pcl.boxhandschuh.protractor;

import java.util.ArrayList;
import java.util.List;

public class Template {

	private List<Point3D> accelerometerTrace;
	private List<Point3D> gyroskopTrace;
	private List<Point3D> trajectoryTrace;

	private String id;

	public Template(String id) {
		super();
		this.gyroskopTrace = new ArrayList<>();
		this.accelerometerTrace = new ArrayList<>();
		this.trajectoryTrace = new ArrayList<>();
		this.id = id;
	}
	
	

	public Template(List<Point3D> accelerometerTrace,
			List<Point3D> gyroskopTrace, List<Point3D> trajectoryTrace,
			String id) {
		super();
		this.accelerometerTrace = accelerometerTrace;
		this.gyroskopTrace = gyroskopTrace;
		this.trajectoryTrace = trajectoryTrace;
		this.id = id;
	}



	public List<Point3D> getAccelerometerTrace() {
		return accelerometerTrace;
	}

	public void setAccelerometerTrace(List<Point3D> accelerometerTrace) {
		this.accelerometerTrace = accelerometerTrace;
	}

	public List<Point3D> getGyroskopTrace() {
		return gyroskopTrace;
	}

	public void setGyroskopTrace(List<Point3D> gyroskopTrace) {
		this.gyroskopTrace = gyroskopTrace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "[" + id + "]:";
	}

	public List<Point3D> getTrajectoryTrace() {
		return trajectoryTrace;
	}

	public void setTrajectoryTrace(List<Point3D> trajectoryTrace) {
		this.trajectoryTrace = trajectoryTrace;
	}

}
