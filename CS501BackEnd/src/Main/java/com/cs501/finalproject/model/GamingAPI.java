package com.cs501.finalproject.model;

import org.bson.types.ObjectId;

public class GamingAPI {
    private ObjectId id;
    private String GamePlatform;
    private String GameName;
    private String API;

    public void setId(ObjectId id){
        this.id = id;
    }

    public void setGamePlatform(String platform){
        this.GamePlatform = platform;
    }

    public void setGameName(String GameName){
        this.GameName = GameName;
    }

    public void setAPI(String API){
        this.API = API;
    }

    public ObjectId getId(){
        return this.id;
    }

    public String getGamePlatform(){
        return this.GamePlatform;
    }

    public String getGameName(){
        return this.GameName;
    }
    public String getAPI(){
        return this.API;
    }

}
