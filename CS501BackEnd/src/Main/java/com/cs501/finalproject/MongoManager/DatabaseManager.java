package com.cs501.finalproject.MongoManager;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
public class DatabaseManager  {
    private static final String DB_CONNECTION_STRING = "mongodb+srv://mongo:dGorupYDungrHKbB@cluster0.ebclkcv.mongodb.net/test";
    private MongoClient mongoClient;

    public void connect() throws MongoException {
        this.mongoClient = (MongoClient) MongoClients.create(DB_CONNECTION_STRING);
    }

    public MongoClient getClient() {
        return mongoClient;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
