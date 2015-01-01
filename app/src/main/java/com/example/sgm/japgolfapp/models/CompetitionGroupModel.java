package com.example.sgm.japgolfapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CarlAnthony on 12/26/2014.
 */
public class CompetitionGroupModel {

    @SerializedName("id")
    private String id;

    @SerializedName("closed_competition_competitor_id")
    private String closeCompetitionCompetitorId;

    @SerializedName("name")
    private String name;

    @SerializedName("closed_competition_id")
    private String closedCompetitionId;

    @SerializedName("competitors")
    private List<CompetitorsModel> competitors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCloseCompetitionCompetitorId() {
        return closeCompetitionCompetitorId;
    }

    public void setCloseCompetitionCompetitorId(String closeCompetitionCompetitorId) {
        this.closeCompetitionCompetitorId = closeCompetitionCompetitorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompetitorsModel> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<CompetitorsModel> competitors) {
        this.competitors = competitors;
    }

    public String getClosedCompetitionId() {
        return closedCompetitionId;
    }

    public void setClosedCompetitionId(String closedCompetitionId) {
        this.closedCompetitionId = closedCompetitionId;
    }
}


