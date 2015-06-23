package de.luh.hci.pcl.boxhandschuh.protractor;

import java.util.ArrayList;
import java.util.List;

public class Template {

	private List<Point3D> trace;
	private String id;

	public Template(List<Point3D> trace, String id) {
		super();
		this.trace = trace;
		this.id = id;
	}

	public Template(String id) {
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

}
