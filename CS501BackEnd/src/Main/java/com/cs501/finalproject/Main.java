package com.cs501.finalproject;

import com.cs501.finalproject.DBInterface.MongoDBAPI;
import com.cs501.finalproject.DBInterface.MongoDBService;
import com.cs501.finalproject.model.User_Info;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {

        // Initialize MongoDBAPI with the MongoClient
        MongoDBAPI mongoDBAPI = new MongoDBAPI();

        // Initialize ServiceClient and set base URL
        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Add the call adapter factory here
                .build();
        MongoDBService myService = retrofit.create(MongoDBService.class);

        get("/hello", (req, res) -> "Hello World");

        get("/api/user/password", (req, res) -> {
            String userName = req.queryParams("username");
            Observable<String> passwordObservable = mongoDBAPI.getUserPassword(userName);
            passwordObservable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String password) {
                            System.out.println(password);
                            res.status(200);
                            res.type("application/json");
                            res.body(password);
                            System.out.println("Response body: " + res.body());
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(404);
                            res.body("User not found.");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

            return res;
        });

        post("/api/user/create", (req, res) -> {
            String name = req.queryParams("username");
            String password = req.queryParams("password");
            Observable<User_Info> userObservable = mongoDBAPI.createUser(name, password);

            // You can handle the user creation response and error here
            userObservable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<User_Info>() {
                        @Override
                        public void onNext(User_Info user) {
                            res.status(200);
                            res.type("application/json");
                            Moshi moshi = new Moshi.Builder().build();
                            JsonAdapter<User_Info> userAdapter = moshi.adapter(User_Info.class);
                            res.body(userAdapter.toJson(user));
                            System.out.println(res.body());
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(400);
                            res.body(e.getMessage());
                            System.out.println(res.body());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

            return res;
        });

        // Stop database connection when application is stopped
//        Runtime.getRuntime().addShutdownHook(new Thread(mongoDBAPI::close));
        // Continuously listen for incoming requests
//        while (true) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}