package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class PlayHistory{

    private String mDate;
    private String mGolfCourseName;
    private ArrayList<PlayHistoryScore> mPlayHistoryScores;

	public PlayHistory(){}
    public PlayHistory(String date, String golfCourse, ArrayList<PlayHistoryScore> playHistoryScore){
        this.mDate = date;
        this.mGolfCourseName = golfCourse;
        this.mPlayHistoryScores = playHistoryScore;
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

    public ArrayList<PlayHistoryScore> getPlayHistoryScores() {
        return mPlayHistoryScores;
    }
    public void setPlayHistoryScores(ArrayList<PlayHistoryScore> playHistoryScores) {
        this.mPlayHistoryScores = playHistoryScores;
    }
}
