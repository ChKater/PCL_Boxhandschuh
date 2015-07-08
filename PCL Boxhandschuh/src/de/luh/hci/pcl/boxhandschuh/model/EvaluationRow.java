package de.luh.hci.pcl.boxhandschuh.model;

import java.util.Map;

public class EvaluationRow {

	private String method, sensors, classname;
	private Map<String, Integer> recognized;
	private double recognitionRate;
	public EvaluationRow(String method, String sensors, String classname,
			Map<String, Integer> recognized) {
		super();
		this.method = method;
		this.sensors = sensors;
		this.classname = classname;
		this.recognized = recognized;
		setRecognitionRate();
		
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSensors() {
		return sensors;
	}
	public void setSensors(String sensors) {
		this.sensors = sensors;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
		setRecognitionRate();
	}
	public Map<String, Integer> getRecognized() {
		return recognized;
	}
	public void setRecognized(Map<String, Integer> recognized) {
		this.recognized = recognized;
		setRecognitionRate();
	}
	public double getRecognitionRate() {
		return recognitionRate;
	}
	private void setRecognitionRate() {
		int total = 0;
		for (String key : recognized.keySet()) {
			total += recognized.get(key);
		}
		this.recognitionRate= (double) recognized.get(classname) / (double) total;
	}
	
	public int getRecognized(String classname){
		Integer rec = recognized.get(classname);
		if(rec != null){
			return rec;
		}
		return 0;
		
	}
	
	
	
	
	
	
	
	
}
