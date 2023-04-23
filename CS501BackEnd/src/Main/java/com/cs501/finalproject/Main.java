package com.cs501.finalproject;

import com.cs501.finalproject.DBInterface.MongoDBAPI;
import com.cs501.finalproject.MongoManager.DatabaseManager;
import com.squareup.moshi.Moshi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Main {
    public static void main(String[] args) {
        // Initialize DatabaseManager and connect to MongoDB
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connect();

        // Initialize ServiceClient and set base URL
        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Add the call adapter factory here
                .build();
        MongoDBAPI myService = retrofit.create(MongoDBAPI.class);

        // Continuously listen for incoming requests
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}