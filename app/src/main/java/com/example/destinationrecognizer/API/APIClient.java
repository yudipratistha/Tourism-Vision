package com.example.destinationrecognizer.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static APIService getService(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://api-antipikun.000webhostapp.com/api/")
                .baseUrl("http://google-vision-location.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(APIService.class);
    }
}
