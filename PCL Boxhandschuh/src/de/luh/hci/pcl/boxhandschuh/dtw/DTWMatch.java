package de.luh.hci.pcl.boxhandschuh.dtw;

public class DTWMatch {

	private double score;
	private DTWTemplate template;

	public DTWMatch(double score, DTWTemplate template) {
		super();
		this.score = score;
		this.template = template;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public DTWTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DTWTemplate template) {
		this.template = template;
	}

}
