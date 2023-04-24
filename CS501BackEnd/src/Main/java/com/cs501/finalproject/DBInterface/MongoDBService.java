package com.cs501.finalproject.DBInterface;

import com.cs501.finalproject.model.User_Info;
import io.reactivex.Observable;
import retrofit2.http.GET;
import org.bson.Document;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MongoDBService {
    @GET("/api/user/password")
    Observable<String> getUserPassword(@Query("username")String userName);

    Observable<Document> getDocumentById(String collectionName, String id);

    Observable<User_Info> createUser(String name, String password);

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
