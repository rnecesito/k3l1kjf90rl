package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class HoleRecord {

    private String mId;
    private String mName;
    private ArrayList<CompetitorCompact> mCompetitors;
    private ArrayList<BetSetting> mBetSettings;

    public HoleRecord(){}

    public HoleRecord(String name, ArrayList<CompetitorCompact> competitors, ArrayList<BetSetting> betSettings){
        mName = name;
        mCompetitors = competitors;
        mBetSettings = betSettings;
    }

    public HoleRecord( String id, String name, ArrayList<CompetitorCompact> competitors, ArrayList<BetSetting> betSettings){
        mId = id;
        mName = name;
        mCompetitors = competitors;
        mBetSettings = betSettings;
    }


    public String getId(){ return mId; }

    public void setId(String id) { this.mId = id; }
	
	public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }


    public ArrayList<CompetitorCompact> getCompetitors(){
        return mCompetitors;
    }

    public void setCompetitors(ArrayList<CompetitorCompact> scores){
        this.mCompetitors = scores;
    }


    public ArrayList<BetSetting> getBetSettings(){ return mBetSettings; }

    public void setBetSettings(ArrayList<BetSetting> betSettings){ this.mBetSettings = betSettings; }

}
