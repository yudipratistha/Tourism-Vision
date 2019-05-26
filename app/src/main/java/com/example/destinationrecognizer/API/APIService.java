package com.example.destinationrecognizer.API;

import com.example.destinationrecognizer.model.VisionModel;
<<<<<<< HEAD
import com.example.destinationrecognizer.model.VisionModelList;
=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("get-location/{lat}/{lng}")
    Call<VisionModel> getVision(@Path("lat") float lat,
                                @Path("lng") float lng);
<<<<<<< HEAD

    @GET("get-all-data/{lat}/{lng}")
    Call<VisionModelList> getAllData(@Path("lat") float lat,
                                     @Path("lng") float lng);
=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
}
