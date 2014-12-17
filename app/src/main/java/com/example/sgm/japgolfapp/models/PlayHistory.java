package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class PlayHistory{

    private String mDate;
    private String mGolfCourseName;
    private ArrayList<Competitor> mCompetitors;

	public PlayHistory(){}
    public PlayHistory(String date, String golfCourse, ArrayList<Competitor> competitor){
        this.mDate = date;
        this.mGolfCourseName = golfCourse;
        this.mCompetitors = competitor;
    }
	public String getDate() {
        return mDate;
    }
    public void setDate(String date) {
        this.mDate = date;
    }

    public String getGolfCourseName() {
        return mGolfCourseName;
    }
    public void setGolfCourseName(String golfCourseName) {
        this.mGolfCourseName = golfCourseName;
    }

    public ArrayList<Competitor> getPlayHistoryScores() {
        return mCompetitors;
    }
    public void setPlayHistoryScores(ArrayList<Competitor> competitors) {
        this.mCompetitors = competitors;
    }
}
