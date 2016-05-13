package com.epicodus.bowloregon.models;

/**
 * Created by Guest on 5/6/16.
 */
public class Game {
    private double score;
    private String date;
    private String alleyName;
    private String pushId;

    public Game() {}

    public Game(double score, String date, String alleyName) {
        this.score = score;
        this.date = date;
        this.alleyName = alleyName;
    }

    public String getDate() {
        return date;
    }

    public double getScore() {
        return score;
    }
    public String getAlleyName() {
        return alleyName;
    }

    public void setAlleyName(String pushId) {
        this.alleyName = alleyName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
