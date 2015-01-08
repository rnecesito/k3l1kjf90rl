package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class HoleRecordCompact {

    private String mId;
    private String mName;
    private ArrayList<CompetitorCompact> mCompetitors;

    public HoleRecordCompact(){}

    public HoleRecordCompact(String id, String name, ArrayList<CompetitorCompact> competitors){
        mId = id;
        mName = name;
        mCompetitors = competitors;
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

    public void setCompetitors(ArrayList<CompetitorCompact> competitors){ this.mCompetitors = competitors; }


}
