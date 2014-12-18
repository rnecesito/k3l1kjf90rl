package com.example.sgm.japgolfapp.models;

import java.util.ArrayList;

public class BetSetting {

    private String mName;
    private String mHelp;
    private Boolean mChosen;

    public BetSetting(){}

    public BetSetting(String name, String help, Boolean choosen){
        mName = name;
        mHelp = help;
        mChosen = choosen;
    }
	
	public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getHelp() {
        return mHelp;
    }

    public void setHelp(String help) {
        this.mHelp = help;
    }

    public Boolean isChosen(){ return mChosen;}

    public void setIsChosen(Boolean chosen) { this.mChosen = chosen;}

}
