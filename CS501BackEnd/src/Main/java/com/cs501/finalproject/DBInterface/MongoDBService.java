package com.cs501.finalproject.DBInterface;

import com.cs501.finalproject.model.User_Info;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import org.bson.Document;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MongoDBService {
    @GET("/api/user/password")
    Observable<String> getUserPassword(@Query("username")String userName);
    @GET("/api/gamesapi/games")
    Observable<List<String>> getGamesByPlatform(@Query("platform") String platform);
    @GET("/api/gamesapi/platform")
    Observable<String> getPlatformByGameName(@Query("gamename") String gameName);
    @GET("/api/gamesapi/api")
    Observable<String> getAPIByGameName(@Query("gamename")String gameName);
    @GET("/api/user/password")
    Observable<List<String>> getFavoriteGamesByUsername(@Query("username")String username);

    Observable<Document> getDocumentById(String collectionName, String id);
    @POST("/api/user/create")
    Observable<User_Info> createUser(String name, String password);
    @POST("/api/user/addgame")
    Completable addFavoriteGame(String username, String gameName);
    @POST("/api/gamesapi/addgame")
    Completable addGame(String gameName, String gamePlatform, String api);
    @GET("myCollection")
    Call<List<Document>> getAllDocuments();

    @GET("myCollection/{id}")
    Call<Document> getDocumentById(@Path("id") String id);

    @POST("myCollection")
    Call<Document> createDocument(@Body Document document);

    @PUT("myCollection/{id}")
    Call<Document> updateDocument(@Path("id") String id, @Body Document document);

    @DELETE("myCollection/{id}")
    Call<Void> deleteDocument(@Path("id") String id);

//    @GET("my/api/endpoint")
//    Call<MyResponse> myMethod(@Query("param1") String param1, @Query("param2") String param2);

}
