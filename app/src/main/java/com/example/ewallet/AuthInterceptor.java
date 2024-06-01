package com.example.ewallet;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    // JWT token to be added in the Authorization header
    private String jwt;
    // Constructor to initialize the interceptor with JWT token
    public AuthInterceptor(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Get the original request from the chain
        Request originalRequest = chain.request();
        // Create a modified request with Authorization header containing JWT token
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + jwt)
                .build();
        // Proceed with the modified request
        return chain.proceed(modifiedRequest);
    }
}
