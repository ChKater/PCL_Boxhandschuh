package de.luh.hci.pcl.boxhandschuh.protractor;

import org.apache.commons.math3.linear.RealMatrix;



public class Match implements Comparable<Match>{
	public double score;
	public RealMatrix r;
	public Template template;

	public Match(double aScore, RealMatrix r) {
		this.score = aScore;
		this.r = r;
	}

	public Match(Match m) {
		this.score = m.score;
		this.r = m.r;
		this.template = m.template;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Match other) {
		return Double.compare(score, other.score) * -1;
	}
	
	@Override
	public String toString() {
		return template.getId() + ", score: " + Math.rint(score);
	}
}