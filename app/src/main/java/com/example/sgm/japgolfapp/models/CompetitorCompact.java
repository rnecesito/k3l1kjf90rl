package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class CompetitorCompact {

    private String mId;
    private String mName;
    private String mScore;

    public CompetitorCompact(){}

    public CompetitorCompact(String id, String name, String score){
        mId = id;
        mName = name;
        mScore = score;
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

}
