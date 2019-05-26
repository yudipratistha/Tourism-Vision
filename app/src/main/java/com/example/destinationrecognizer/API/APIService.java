package com.example.destinationrecognizer.API;

import com.example.destinationrecognizer.model.VisionModel;

import com.example.destinationrecognizer.model.VisionModelList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("get-location/{lat}/{lng}")
    Call<VisionModel> getVision(@Path("lat") float lat,
                                @Path("lng") float lng);


    @GET("get-all-data/{lat}/{lng}")
    Call<VisionModelList> getAllData(@Path("lat") float lat,
                                     @Path("lng") float lng);

}
