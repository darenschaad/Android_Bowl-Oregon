package com.epicodus.bowloregon.models;

import java.util.Date;

/**
 * Created by Guest on 5/6/16.
 */
public class Game {
    double score;
    Date date;
    String alleyName;
    String pushId;

    public Game() {}

    public Game(double score, Date date, String alleyName) {
        this.score = score;
        this.date = date;
        this.alleyName = alleyName;
    }

    public Date getDate() {
        return date;
    }

    public double getScore() {
        return score;
    }

    public String getAlleyName() {
        return alleyName;
    }

    public void setAlleyName(String alleyName) {
        this.alleyName = alleyName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
