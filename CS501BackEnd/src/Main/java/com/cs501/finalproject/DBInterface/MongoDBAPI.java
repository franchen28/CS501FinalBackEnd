package com.cs501.finalproject.DBInterface;

import com.cs501.finalproject.model.GamingAPI;
import com.cs501.finalproject.model.User_Info;
import com.mongodb.client.*;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.bson.Document;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class MongoDBAPI implements MongoDBService {
    private final MongoDatabase database;
    private final MongoCollection<GamingAPI> gamingAPICollection;
    private final MongoCollection<User_Info> userCollection;
    private final MongoCollection<Document> userCollection1;
    public MongoDBAPI() {
//        MongoClients MongoClients = null;
        MongoClient mongoClient = MongoDBConnection.createMongoClient("mongodb+srv://mongo:dGorupYDungrHKbB@cluster0.ebclkcv.mongodb.net/test");
        this.database = mongoClient.getDatabase("Gaming");
        this.gamingAPICollection = database.getCollection("GamingAPI", GamingAPI.class);
        this.userCollection = database.getCollection("User_Info", User_Info.class);
        this.userCollection1 = database.getCollection("User_Info");
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
    public Observable<String> getUserDescription(String userName) {
        // Build a query to find the user document with the given name
        Document query = new Document("username", userName);
        // Find the user document and retrieve its password field
        Observable<String> observable = Observable.fromCallable(() -> {
            try {
                User_Info user_info = userCollection.find(query)
                        .projection(new Document("description", 1))
                        .first();
                if (user_info == null) {
                    System.out.println("User not found in the database"); // Add this line
                    throw new Exception("User not found.");
                }
                String res = user_info.getDescription();
                if(res == null){
                    res = "";
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace(); // Print the stack trace
                throw e;
            }
        }).subscribeOn(Schedulers.io());

        // Create a Retrofit call object and return it
        return observable;
    }



    @Override
    public Completable updateUserInfo(String oldUserName, String newUserName, String password, String description) {

        return Completable.create(emitter -> {
            try {
                Document query = new Document("username", oldUserName);
                Document query2 = new Document("username", newUserName);
                if(userCollection.countDocuments(query) == 0){
                    emitter.onError(new Exception("User not exist"));
                }
                if (userCollection.countDocuments(query2) == 0) {
                    userCollection.updateOne(
                            query,
                            new Document("$set", new Document("username", newUserName)
                                    .append("password", password)
                                    .append("description", description))
                    );
                    emitter.onComplete();
                }else {
                    emitter.onError(new Exception("Username already exists"));
                }

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Observable<List<String>> getGamesByPlatform(String platform) {
        return Observable.create(emitter -> {
            try {
                List<String> gameNames = new ArrayList<>();
                for (GamingAPI document : gamingAPICollection.find(new Document("gamePlatform", platform))) {
                    String gameName = document.getGameName();
                    gameNames.add(gameName);
                }
                emitter.onNext(gameNames);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
    @Override
    public Observable<String> getPlatformByGameName(String gameName) {
        return Observable.create(emitter -> {
            try {
                Document query = new Document("gameName", gameName);
                GamingAPI document = gamingAPICollection.find(query).first();

                if (document != null) {
                    String platform = document.getGamePlatform();
                    emitter.onNext(platform);
                } else {
                    emitter.onError(new Exception("Game not found."));
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
    @Override
    public Observable<String> getAPIByGameName(String gameName) {
        return Observable.create(emitter -> {
            try {
                Document query = new Document("gameName", gameName);
                GamingAPI document = gamingAPICollection.find(query).first();

                if (document != null) {
                    String API = document.getAPI();
                    emitter.onNext(API);
                } else {
                    emitter.onError(new Exception("Game not found."));
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
    @Override
    public Observable<List<String>> getFavoriteGamesByUsername(String username) {
        return Observable.create(emitter -> {
            try {
                Document query = new Document("username", username);
                Document userDocument = userCollection1.find(query).first();

                if (userDocument != null) {
                    List<String> favoriteGames;
                    if(userDocument.get("favorite")==null){
                        favoriteGames=new ArrayList<>();
                    }else{
                        favoriteGames = (ArrayList<String>)userDocument.get("favorite");
                    }
                    System.out.println(favoriteGames);
                    emitter.onNext(favoriteGames);
                } else {
                    emitter.onError(new Exception("User not found."));
                }

                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
    @Override
    public Observable<Document> getDocumentById(String collectionName, String id) {
        return null;
    }

    @Override
    public Completable createUser(String name, String password) {
        return Completable.create(emitter -> {
            try {
                Document query = new Document("username", name);
                System.out.println(name);
                if (userCollection.countDocuments(query) == 0) {
                    User_Info user = new User_Info();
                    user.setUsername(name);
                    user.setPassword(password);
                    userCollection.insertOne(user);
                    emitter.onComplete();
                } else {
                emitter.onError(new Exception("User already exists"));
            }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
    @Override
    public Completable addGame(String gameName, String gamePlatform, String api) {
        return Completable.create(emitter -> {
            try {
                Document query = new Document("gameName", gameName).append("gamePlatform", gamePlatform);
                if (gamingAPICollection.countDocuments(query) == 0) {
                    GamingAPI newgame = new GamingAPI();
                    newgame.setAPI(api);
                    newgame.setGameName(gameName);
                    newgame.setGamePlatform(gamePlatform);
                    gamingAPICollection.insertOne(newgame);
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
    @Override
    public Completable addFavoriteGame(String username, String gameName) {
        return Completable.create(emitter -> {
            try {
                Document query = new Document("username", username);
                Document userDocument = userCollection1.find(query).first();

                if (userDocument != null) {
                    // Retrieve the favorite games array from the document
                    System.out.println(userDocument.get("favorite"));
                    ArrayList<String> favoriteGames;

                    // Check if the favorite games array is present and not null
                    if (userDocument.get("favorite") != null) {
                        favoriteGames = (ArrayList<String>) userDocument.get("favorite");
                    } else {
                        // Create a new ArrayList if the favorite games array is not present or null
                        favoriteGames = new ArrayList<>();
                    }

                    // Check if the gameName already exists in the favorite games list
                    if (!favoriteGames.contains(gameName)) {
                        // Update the document by adding the gameName to the favorite games list
                        userCollection.updateOne(query, new Document("$push", new Document("favorite", gameName)));
                        emitter.onComplete();
                    } else {
                        // Throw an error if the gameName is already present in the list
                        emitter.onError(new Exception("Game name already exists in the favorites list."));
                    }
                } else {
                    emitter.onError(new Exception("User not found."));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Completable deleteFavoriteGame(String username, String gameName) {
        return Completable.create(emitter -> {
            try {
                Document query = new Document("username", username);
                Document userDocument = userCollection1.find(query).first();

                if (userDocument != null) {
                    // Retrieve the favorite games array from the document
                    ArrayList<String> favoriteGames = (ArrayList<String>) userDocument.get("favorite");

                    // Check if the gameName exists in the favorite games list
                    if (favoriteGames.contains(gameName)) {
                        // Update the document by removing the gameName from the favorite games list
                        userCollection.updateOne(query, new Document("$pull", new Document("favorite", gameName)));
                        emitter.onComplete();
                    } else {
                        // Throw an error if the gameName is not present in the list
                        emitter.onError(new Exception("Game name not found in the favorites list."));
                    }
                } else {
                    emitter.onError(new Exception("User not found."));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
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
