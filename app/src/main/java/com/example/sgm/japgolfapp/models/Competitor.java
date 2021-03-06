package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class Competitor {

    private String mName;
    private String mGross;
    private String mNet;
    private String mRank;
    private ArrayList<Integer> mScores;

    public Competitor(){}

    public Competitor(String name, String gross, String net, String rank, ArrayList<Integer> scores){
        this.mName = name;
        this.mGross = gross;
        this.mNet = net;
        this.mRank = rank;
        this.mScores = scores;
    }
	
	public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getGross() {
        return mGross;
    }

    public void setGross(String gross) {
        this.mGross = gross;
    }

    public String getNet() {
        return mNet;
    }

    public void setNet(String net) {
        this.mNet = net;
    }

    public String getRank() {
        return mRank;
    }

    public void setRank(String rank) {
        this.mRank = rank;
    }

    public ArrayList<Integer> getScores(){
        return mScores;
    }

    public void setScores(ArrayList<Integer> scores){
        this.mScores = scores;
    }
}
