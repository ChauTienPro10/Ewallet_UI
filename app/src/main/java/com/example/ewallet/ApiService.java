package com.example.ewallet;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.ewallet.Domain.HistoryDomain;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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
    @POST("/test/logout")
    Call<Void> logout();

    @POST("/test/register")
    Call<ResponseBody> register(@Body Member member);

    @POST("/transaction/getQRData")
    Call<ResponseBody> sendQrData(@Body String requestBody);

    @POST("/transaction/createQR")
    Call<ResponseBody> createQR(@Body JsonObject requestBody);
    @POST("/ETH/deposit_by_Eth")
    Call<ResponseBody> depositSer(@Body JsonObject object);
    @POST("/ETH/withdraw")
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
    @POST("/test/linkWallet")
    Call<ResponseBody> linktoETH(@Body JsonObject json);
    @POST("/transaction/AcceptCard")
    Call<ResponseBody>openCard(@Body JsonObject jsonObject);
    @POST("/profile/get_new_pass")
    Call<ResponseBody> getCode(@Body JsonObject js);
    @POST("/profile/authenCode_change_pass")
    Call<ResponseBody> authen_changepassword(@Body JsonObject js);

//    class ApiUtils {
//        public static ApiService getApiService(Context context) {
//            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new AuthInterceptor(sharedPreferences.getString("jwt", "")))
//                    .build();
//            Gson gson = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                    .setLenient()
//                    .create();
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://172.16.3.72:9005")
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .client(client)
//                    .build();
//
//            return retrofit.create(ApiService.class);
//        }
//    }

    public class ApiUtils {

        public static ApiService getApiService(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            OkHttpClient client = null;
            try {
                // Tải keystore từ thư mục assets
                InputStream keyStoreStream = context.getAssets().open("baeldung.p12");
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(keyStoreStream, "22012003".toCharArray());

                // Tạo KeyManager
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, "22012003".toCharArray());

                // Tạo TrustManager
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                // Tạo SSLContext
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

                HostnameVerifier hostnameVerifier = (hostname, session) -> {
                    // Chấp nhận bất kỳ hostname nào
                    return true;
                };

                // Cấu hình OkHttpClient
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                        .hostnameVerifier(hostnameVerifier)
                        .addInterceptor(new AuthInterceptor(sharedPreferences.getString("jwt", "")))
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://172.16.1.131:9005")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit.create(ApiService.class);
        }
    }
}
