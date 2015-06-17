package de.luh.hci.pcl.boxhandschuh.protrector;

import java.util.ArrayList;
import java.util.List;

public class Template {

	private List<Point3D> trace;
	private int id;

	public Template(List<Point3D> trace, int id) {
		super();
		this.trace = trace;
		this.id = id;
	}

	public Template(int id) {
		super();
		this.trace = new ArrayList<>();
		this.id = id;
	}
	
	public List<Point3D> getTrace() {
		return trace;
	}

	public void setTrace(List<Point3D> trace) {
		this.trace = trace;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
