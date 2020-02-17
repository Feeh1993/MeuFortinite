package com.example.meufortinite.MODEL;

import com.example.meufortinite.MODEL.Stats;

import java.util.ArrayList;

public class ItemResponse {

    private String accountId;
    private int platformId;
    private String platformName;
    private String platformNameLong;
    private String epicUserHandle;
    private ArrayList<Stats> lifeTimeStats;

    public ItemResponse(){}

    public ArrayList<Stats> getLifeTimeStats() {
        return lifeTimeStats;
    }

    public void setLifeTimeStats(ArrayList<Stats> lifeTimeStats) {
        this.lifeTimeStats = lifeTimeStats;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformNameLong() {
        return platformNameLong;
    }

    public void setPlatformNameLong(String platformNameLong) {
        this.platformNameLong = platformNameLong;
    }

    public String getEpicUserHandle() {
        return epicUserHandle;
    }

    public void setEpicUserHandle(String epicUserHandle) {
        this.epicUserHandle = epicUserHandle;
    }
}
