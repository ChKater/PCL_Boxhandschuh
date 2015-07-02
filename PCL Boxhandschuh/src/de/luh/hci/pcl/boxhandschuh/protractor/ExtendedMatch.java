package de.luh.hci.pcl.boxhandschuh.protractor;

import org.apache.commons.math3.linear.RealMatrix;

public class ExtendedMatch extends Match {
	
	private MatchType matchType;

	public ExtendedMatch(double aScore, RealMatrix r, MatchType matchType) {
		super(aScore, r);
		this.matchType = matchType;
		// TODO Auto-generated constructor stub
	}
	
	public ExtendedMatch(Match m, MatchType matchType) {
		super(m);
		this.matchType = matchType;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString(){
		return this.template.getId() + "; score: " + this.score + "; " + this.matchType;
	}

}
