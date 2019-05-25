package com.example.destinationrecognizer.API;


import android.os.AsyncTask;
import android.util.Log;

import com.example.destinationrecognizer.model.PlaceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by navneet on 23/7/16.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, Wrapper> {
    String url;
    private PlaceModel place;
    private List<PlaceModel> places;

    public AsyncResponse delegate = null;

    public GetNearbyPlacesData(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Wrapper doInBackground(Object... params) {
        Wrapper w = new Wrapper();
        w.currentLat = (double) params[0];
        w.currentlng = (double) params[1];
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            url = (String) params[2];
            DownloadUrl downloadUrl = new DownloadUrl();
            w.googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return w;
    }

    @Override
    protected void onPostExecute(Wrapper w) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(w.googlePlacesData);
        ShowNearbyPlaces(nearbyPlacesList, w.currentLat, w.currentlng);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList, double lat1, double lng1) {
        places = new ArrayList<PlaceModel>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            boolean isType = false;
            Log.d("onPostExecute", "Entered into showing locations");
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat2 = Double.parseDouble(googlePlace.get("lat"));
            double lng2 = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            int typesLength = Integer.parseInt(googlePlace.get("types_length"));

            double latitude1 = Math.toRadians(lat1);
            double latitude2 = Math.toRadians(lat2);
            double longDiff = Math.toRadians(lng2 - lng1);
            double y = Math.sin(longDiff) * Math.cos(latitude2);
            double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);
            float azimuth = (float) (Math.toDegrees(Math.atan2(y, x)));
            for (int j = 0; j < typesLength ; ++j) {
                String typeNum = "type"+j;
//                if(googlePlace.get(typeNum).equals("political") ||
//                        googlePlace.get(typeNum).equals("locality") || googlePlace.get(typeNum).equals("route")){
//                    isType = true;
//                }
            }
            if(!isType){
                place = new PlaceModel();
                place.setPlaceName(placeName);
                place.setVicinity(vicinity);
                place.setAzimuth(azimuth);
                place.setDistance((float)lat1, (float)lng1, (float)lat2, (float)lng2);
                places.add(place);
               // break;
            }

        }
        //Log.e("places2: ", String.valueOf(places.size()));
        delegate.processfinished(places);

    }


}
