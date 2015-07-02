package de.luh.hci.pcl.boxhandschuh.protractor;


public class Score {
	private double score;
	private MatchType matchType;
	
	public Score(double score, MatchType matchType){
		this.score = score;
		this.matchType = matchType;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public MatchType getMatchType() {
		return matchType;
	}
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}
	

}
