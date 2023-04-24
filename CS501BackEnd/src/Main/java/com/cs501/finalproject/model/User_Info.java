package com.cs501.finalproject.model;

import org.bson.types.ObjectId;

public class User_Info {
    private ObjectId _id;
    private String username;
    private String password;

    public void setId(ObjectId _id){
        this._id = _id;
    }

    public void setName(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
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
}
