package com.cs501.finalproject.model;

import org.bson.types.ObjectId;

public class User_Info {
    private ObjectId id;
    private String name;
    private String password;

    public void setId(ObjectId id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public ObjectId getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }
}
