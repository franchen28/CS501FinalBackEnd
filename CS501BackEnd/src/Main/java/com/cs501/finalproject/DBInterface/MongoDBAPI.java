package com.cs501.finalproject.DBInterface;

import com.cs501.finalproject.model.GamingAPI;
import com.cs501.finalproject.model.User_Info;
import com.mongodb.client.*;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.bson.Document;
import org.bson.types.ObjectId;
import retrofit2.Call;

import java.util.List;

public class MongoDBAPI implements MongoDBService {
    private final MongoDatabase database;
    private final MongoCollection<GamingAPI> gamingAPICollection;
    private final MongoCollection<User_Info> userCollection;

    public MongoDBAPI() {
//        MongoClients MongoClients = null;
        MongoClient mongoClient = MongoDBConnection.createMongoClient("mongodb://localhost:27017");
        this.database = mongoClient.getDatabase("Gaming");
        this.gamingAPICollection = database.getCollection("GamingAPI", GamingAPI.class);
        this.userCollection = database.getCollection("User_Info", User_Info.class);
    }

    @Override
    public Observable<String> getUserPassword(String userName) {
        System.out.println("getUserPassword method called with userName: " + userName);
        // Build a query to find the user document with the given name
        Document query = new Document("username", userName);
        System.out.println("Query: " + query.toJson()); // Add this line
        // Find the user document and retrieve its password field
        Observable<String> observable = Observable.fromCallable(() -> {
            try {
                User_Info user_info = userCollection.find(query)
                        .projection(new Document("password", 1))
                        .first();
                if (user_info == null) {
                    System.out.println("User not found in the database"); // Add this line
                    throw new Exception("User not found.");
                }
                System.out.println("User document: " + user_info.toString());
                System.out.println("Password: " + user_info.getPassword());
                return user_info.getPassword();
            } catch (Exception e) {
                e.printStackTrace(); // Print the stack trace
                throw e;
            }
        }).subscribeOn(Schedulers.io());

        // Create a Retrofit call object and return it
        return observable;
    }

    @Override
    public Observable<Document> getDocumentById(String collectionName, String id) {
        return null;
    }

    @Override
    public Observable<User_Info> createUser(String name, String password) {
        Document query = new Document("username", name);
        User_Info user_info = userCollection.find(query)
                .projection(new Document("password", 1))
                .first();
        if (user_info != null) {
            return Observable.error(new Exception("User with the given username already exists."));
        }
        User_Info user = new User_Info();
        user.setId(new ObjectId());
        user.setUsername(name);
        user.setPassword(password);
        userCollection.insertOne(user);
        return Observable.fromCallable(() -> {
            return user;
        });
    }
//    @Override
//    public Call<Integer> getUserPassword(String userName) {
//        // Build a query to find the user document with the given name
//        Document query = new Document("name", userName);
//
//        // Find the user document and retrieve its password field
//        String password = Objects.requireNonNull(userCollection.find(query)
//                        .projection(new Document("password", 1))
//                        .first())
//                .getPassword();
//
//        // Create a Retrofit call object and return it
//        return CallAdapter.adapt(() -> password);
//    }
    @Override
    public Call<List<Document>> getAllDocuments() {
        return null;
    }

    @Override
    public Call<Document> getDocumentById(String id) {
        return null;
    }

    @Override
    public Call<Document> createDocument(Document document) {
        return null;
    }

    @Override
    public Call<Document> updateDocument(String id, Document document) {
        return null;
    }

    @Override
    public Call<Void> deleteDocument(String id) {
        return null;
    }

//    @Override
//    public Call<MyResponse> myMethod(String param1, String param2) {
//        return null;
//    }
}
