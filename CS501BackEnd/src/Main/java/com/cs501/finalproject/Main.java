package com.cs501.finalproject;

import com.cs501.finalproject.DBInterface.MongoDBAPI;
import com.cs501.finalproject.DBInterface.MongoDBService;
import com.cs501.finalproject.model.User_Info;
import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.List;

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
            //while (passwordObservable.subscribeOn(Schedulers.io()).blockingFirst() == null) {
                //Thread.sleep(50);
            //}
            while (res.body() == null) {
                Thread.sleep(50);
            }
            System.out.println("aaaa"+res.body());
            return res.body();
        });

        get("/api/gamesapi/games", (req, res) -> {
            String platform = req.queryParams("platform");
            Observable<List<String>> gamesObservable = mongoDBAPI.getGamesByPlatform(platform);
            gamesObservable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<List<String>>() {
                        @Override
                        public void onNext(List<String> games) {
                            res.status(200);
                            res.type("application/json");
                            res.body(new Gson().toJson(games));
                            System.out.println("Response body: " + res.body());
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(404);
                            res.body("Platform not found or an error occurred.");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

            // Wait for the observable to complete and return the response
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res.body();
        });
        get("/api/gamesapi/platform", (req, res) -> {
            String gameName = req.queryParams("gamename");
            Observable<String> platformObservable = mongoDBAPI.getPlatformByGameName(gameName);
            platformObservable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String platform) {
                            System.out.println(platform);
                            res.status(200);
                            res.type("application/json");
                            res.body(platform);
                            System.out.println("Response body: " + res.body());
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(404);
                            res.body("Game not found.");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
// Wait for the observable to complete and return the response
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res.body();
        });
        get("/api/gamesapi/api", (req, res) -> {
            String gameName = req.queryParams("gamename");
            Observable<String> apiObservable = mongoDBAPI.getAPIByGameName(gameName);
            apiObservable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String platform) {
                            System.out.println(platform);
                            res.status(200);
                            res.type("application/json");
                            res.body(platform);
                            System.out.println("Response body: " + res.body());
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(404);
                            res.body("Game not found.");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
// Wait for the observable to complete and return the response
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res.body();
        });
        get("/api/user/favoriteGames", (req, res) -> {
            String username = req.queryParams("username");
            Observable<List<String>> favoriteGamesObservable = mongoDBAPI.getFavoriteGamesByUsername(username);
            favoriteGamesObservable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableObserver<List<String>>() {
                        @Override
                        public void onNext(List<String> favoriteGames) {
                            res.status(200);
                            res.type("application/json");
                            res.body(new Gson().toJson(favoriteGames));
                            System.out.println("Response body: " + res.body());
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(404);
                            res.body("User not found or an error occurred.");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

            // Wait for the observable to complete and return the response
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res.body();
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
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res;
        });
        post("/api/user/addgame", (req, res) -> {
            String username = req.queryParams("username");
            String gameName = req.queryParams("gamename");
            Completable addFavoriteGameCompletable = mongoDBAPI.addFavoriteGame(username, gameName);
            addFavoriteGameCompletable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            res.status(200);
                            res.body("Favorite game added successfully.");
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(400);
                            res.body(e.getMessage());
                        }
                    });

            // Wait for the completable to complete and return the response
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res.body();
        });
        post("/api/gamesapi/addgame", (req, res) -> {
            String gameName = req.queryParams("gamename");
            String gamePlatform = req.queryParams("platform");
            String api = req.queryParams("api");
            Completable addGameCompletable = mongoDBAPI.addGame(gameName, gamePlatform, api);
            addGameCompletable.subscribeOn(Schedulers.io())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            res.status(200);
                            res.body("Game added successfully.");
                        }

                        @Override
                        public void onError(Throwable e) {
                            res.status(500);
                            res.body("An error occurred while adding the game.");
                        }
                    });

            // Wait for the completable to complete and return the response
            while (res.body() == null) {
                Thread.sleep(50);
            }
            return res.body();
        });

    }
}