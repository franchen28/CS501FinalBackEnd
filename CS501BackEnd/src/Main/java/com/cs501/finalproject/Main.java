package com.cs501.finalproject;

import com.cs501.finalproject.DBInterface.MongoDBAPI;
import com.cs501.finalproject.DBInterface.MongoDBService;
import com.squareup.moshi.Moshi;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) {

        // Initialize MongoDBAPI with the MongoClient
        MongoDBAPI mongoDBAPI = new MongoDBAPI();

        // Initialize ServiceClient and set base URL
        Moshi moshi = new Moshi.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:4567/")
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