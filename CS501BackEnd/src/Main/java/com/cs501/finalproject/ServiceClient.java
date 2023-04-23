package com.cs501.finalproject;

import com.cs501.finalproject.DBInterface.MongoDBAPI;
import com.squareup.moshi.Moshi;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ServiceClient<MyRequest> {
    private MongoDBAPI myService;

    public void MyServiceClient() {
        Moshi moshi = new Moshi.Builder().build();
        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/") // Base URL of your REST API
                .addConverterFactory(MoshiConverterFactory.create()) // JSON converter
                .build();

        // Create an instance of your service interface
        myService = retrofit.create(MongoDBAPI.class);
    }

    public ServiceClient(MongoDBAPI myService) {
        this.myService = myService;
    }

}
