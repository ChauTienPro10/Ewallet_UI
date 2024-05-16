package com.example.ewallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import com.example.ewallet.Domain.HistoryDomain;

import Entities.Card;
import Entities.EtherWallet;
import Entities.LoginRequest;
import Entities.LoginResponse;
import Entities.Member;
import okhttp3.OkHttpClient;
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
    @POST("/transaction/deposit")
    Call<ResponseBody> depositSer(@Body JsonObject object);
    @POST("/transaction/withdrawal")
    Call<ResponseBody> withdrawSer(@Body JsonObject object);
    @POST("/test/authen_pincode")
    Call<Boolean> authenPin(@Body JsonObject obj);
    @GET("/check/getInfor")
    Call<Member> getMember();

    @GET("/transaction/check_balance")
    Call<String> getBalance();

    @POST("/profile/change_email")
    Call<ResponseBody> changeEmail(@Body JsonObject jsonObj);
    @GET("/test/getPincode")
    Call<ResponseBody> getPin();

    @POST("/profile/change_phone")
    Call<ResponseBody> changePhone(@Body JsonObject jsonObj);
    @POST("/profile/change_password")
    Call<ResponseBody> changePass(@Body JsonObject jsonObj);
    @GET("/check/history")
    Call<ArrayList<HistoryDomain>> getHistory();
    @POST("/check/findHistory")
    Call<ArrayList<HistoryDomain>> findHistory(@Body JsonObject jsonObj);

    @GET("/profile/getCard")
    Call<Card> getCard();
    @POST("check/find_member")
    Call<Member> searhMember(@Body JsonObject jsonOb);

    @POST("/transaction/transfer")
    Call<ResponseBody> transfer(@Body JsonObject obj);

    @GET("/ETH/getEth")
    Call<EtherWallet> getInfEth();
    @POST("/ETH/getEthBalance")
    Call<ResponseBody> getbalanceETH(@Body JsonObject obj);
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
                    .baseUrl("http://172.16.3.157:9005")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(ApiService.class);
        }
    }
}
