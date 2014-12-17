package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class HoleRecord {

    private String mName;
    private ArrayList<Competitor> mCompetitors;

    public HoleRecord(){}

    public HoleRecord(String name, ArrayList<Competitor> competitors){
        mName = name;
        mCompetitors = competitors;
    }
	
	public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }


    public ArrayList<Competitor> getCompetitors(){
        return mCompetitors;
    }

    public void setCompetitors(ArrayList<Competitor> scores){
        this.mCompetitors = scores;
    }
}
