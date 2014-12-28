package com.example.sgm.japgolfapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CarlAnthony on 12/26/2014.
 */
public class CompetitorsModel {

    @SerializedName("id")
    private String id;

    @SerializedName("closed_competition_id")
    private String closedCompetitionId;

    @SerializedName("member_id")
    private String memberId;

    @SerializedName("joined")
    private String joined;

    @SerializedName("closed_competition_group_id")
    private String closedCompetitionGroupId;

    @SerializedName("member")
    private UserModel userModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClosedCompetitionId() {
        return closedCompetitionId;
    }

    public void setClosedCompetitionId(String closedCompetitionId) {
        this.closedCompetitionId = closedCompetitionId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getClosedCompetitionGroupId() {
        return closedCompetitionGroupId;
    }

    public void setClosedCompetitionGroupId(String closedCompetitionGroupId) {
        this.closedCompetitionGroupId = closedCompetitionGroupId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
