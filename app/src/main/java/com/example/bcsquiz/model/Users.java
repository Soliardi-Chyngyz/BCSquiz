package com.example.bcsquiz.model;

import com.google.firebase.firestore.Exclude;

import org.intellij.lang.annotations.PrintFormat;

import java.io.Serializable;

public class Users implements Serializable {


    private long id;
    private String user;
    private int points;

    public Users(String user, int points) {
        this.user = user;
        this.points = points;
    }

    public Users() {

    }

    @Exclude
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
