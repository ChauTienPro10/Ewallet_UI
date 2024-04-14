package com.example.ewallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import Entities.LoginRequest;
import Entities.LoginResponse;
import Entities.Member;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("test/hello")
    Call<ResponseBody> check();

    @POST("/test/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/test/register")
    Call<ResponseBody> register(@Body Member member);

    @POST("/transaction/getQRData")
    Call<ResponseBody> sendQrData(@Body String requestBody);

    @POST("/transaction/createQR")
    Call<ResponseBody> createQR(@Body JsonObject requestBody);

    class ApiUtils {
        public static ApiService getApiService(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(sharedPreferences.getString("jwt", "")))
                    .build();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.20.149:9001")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(ApiService.class);
        }
    }
}
