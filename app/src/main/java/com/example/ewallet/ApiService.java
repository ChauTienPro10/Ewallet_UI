package com.example.ewallet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiService=new Retrofit.Builder()
            .baseUrl("http://10.0.244.251:9001")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);

    @GET("test/hello")
    Call<String> check();
}
