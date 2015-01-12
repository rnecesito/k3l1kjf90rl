package com.example.sgm.japgolfapp.models;

public class CompetitorCompact {

    private String mId;
    private String mName;
    private String mScore;
    private String mHandicap;

    public CompetitorCompact(){}

    public CompetitorCompact(String id, String name, String score, String handicap){
        mId = id;
        mName = name;
        mScore = score;
        mHandicap = handicap;
    }
	
	public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getScore() {
        return mScore;
    }

    public void setScore(String score) { this.mScore = score; }

    public String getHandicap() {
        return mHandicap;
    }

    public void setHandicap(String handicap) {
        this.mHandicap = handicap;
    }
}
