package com.example.destinationrecognizer.API;

import com.example.destinationrecognizer.model.PlaceModel;

import java.util.List;

public interface AsyncResponse {
    void processfinished(List<PlaceModel> places);
}