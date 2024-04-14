package com.example.ewallet;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String jwt;

    public AuthInterceptor(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Thêm thông tin xác thực và chuỗi JWT vào header của yêu cầu
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + jwt)
                .build();

        return chain.proceed(modifiedRequest);
    }
}
