package com.example.sgm.japgolfapp.models;

public class BetSetting {

    private String mId;
    private String mAmmount;
    private String mName;
    private String mHelp;
    private Boolean mChosen;

    public BetSetting(){}

    public BetSetting(String id, String ammount, String name, String help, Boolean choosen){
        this.mId = id;
        this.mAmmount = ammount;
        this.mName = name;
        this.mHelp = help;
        this.mChosen = choosen;
    }

    public BetSetting(String name, String help, Boolean choosen){
        this.mName = name;
        this.mHelp = help;
        this.mChosen = choosen;
    }

    public void setId(String id){
        mId = id;
    }

    public String getId(){
        return mId;
    }

    public void setAmmount(String ammount){
        mAmmount = ammount;
    }

    public String getAmmount(){
        return mAmmount;
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
