package com.cs501.finalproject.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User_Info {
    private ObjectId _id;
    private String username;
    private String password;
    private List<String> favorite;

    private String Description;

    public User_Info(){
        this.favorite=new ArrayList<>();
        this.setDescription("");
    }
    public void setId(ObjectId _id){
        this._id = _id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setDescription(String description){
        this.Description = description;
    }
    public ObjectId getId(){
        return this._id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
    public List<String> getFavoriteGames(){
        return this.favorite;
    }
    public void addGame(String game){
        this.favorite.add(game);
    }

    public String getDescription(){
        return this.Description;
    }
}
