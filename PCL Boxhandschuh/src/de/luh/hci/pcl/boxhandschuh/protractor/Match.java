package de.luh.hci.pcl.boxhandschuh.protractor;

import org.apache.commons.math3.linear.RealMatrix;



public class Match {
	public double score;
	public RealMatrix r;
	public Template template;

	public Match(double aScore, RealMatrix r) {
		this.score = aScore;
		this.r = r;
	}
}