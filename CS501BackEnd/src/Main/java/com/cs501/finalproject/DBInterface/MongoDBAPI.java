package com.cs501.finalproject.DBInterface;

import com.cs501.finalproject.model.GamingAPI;
import com.cs501.finalproject.model.User_Info;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.bson.Document;
import org.bson.types.ObjectId;
import retrofit2.Call;

import java.util.List;

public class MongoDBAPI<MyResponse> implements MongoDBService<MyResponse> {
    private final MongoDatabase database;
    private final MongoCollection<GamingAPI> gamingAPICollection;
    private final MongoCollection<User_Info> userCollection;

    public MongoDBAPI(MongoClient mongoClient) {
        this.database = mongoClient.getDatabase("myDatabase");
        this.gamingAPICollection = database.getCollection("gamingAPIs", GamingAPI .class);
        this.userCollection = database.getCollection("user_info", User_Info .class);
    }

    @Override
    public Observable<String> getUserPassword(String userName) {
        // Build a query to find the user document with the given name
        Document query = new Document("name", userName);

        // Find the user document and retrieve its password field
        Observable<String> observable = Observable.fromCallable(() -> {
            User_Info user_info = userCollection.find(query)
                    .projection(new Document("password", 1))
                    .first();
            if (user_info == null) {
                throw new Exception("User not found.");
            }
            return user_info.getPassword();
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
        User_Info user = new User_Info();
        user.setId(new ObjectId());
        user.setName(name);
        user.setPassword(password);

        return Observable.fromCallable(() -> {
            userCollection.insertOne(user);
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

    @Override
    public Call<MyResponse> myMethod(String param1, String param2) {
        return null;
    }
}
