package com.example.destinationrecognizer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.destinationrecognizer.DetailActivity;
import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.model.LandmarkModel;
import com.example.destinationrecognizer.model.WebMatchingModel;
import com.example.destinationrecognizer.model.WebModel;

import java.util.List;

public class WebFragment2 extends Fragment{
    private List<WebModel> webs;
    private List<WebMatchingModel> webMatchs;
    private List<LandmarkModel> landmarks;
    private View rootView;
    private LinearLayout entitiesLayout;
    private LinearLayout matchingLayout;
    private LayoutInflater inflater;

    public WebFragment2() {}

    public static WebFragment2 newInstance() {
        WebFragment2 fragment = new WebFragment2();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_web, container, false);
        return rootView;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webs = ((DetailActivity) getActivity()).getWebs();
        webMatchs = ((DetailActivity) getActivity()).getWebMatchs();
        landmarks = ((DetailActivity) getActivity()).getLandmarks();
        createView();
        if(webs.size()==0||landmarks.size()==0)createWebNullView();
        else for (WebModel web : this.webs) {
                if(web.getName()!=null)createWebView(web);
            }
        if(webMatchs.size()==0||landmarks.size()==0) createWebMatchNullView();
        else
            for (WebMatchingModel webMatch : this.webMatchs) {
                createWebMatchView(webMatch);
            }
    }

    public void createView(){
         entitiesLayout = (LinearLayout) rootView.findViewById(R.id.webEntities);
         matchingLayout = (LinearLayout) rootView.findViewById(R.id.pageMatchingImages);
         inflater = LayoutInflater.from(getActivity().getApplicationContext());
    }

    public void createWebMatchView(final WebMatchingModel webmatch){
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.web_matching_image, null, false);
        matchingLayout.addView(newLayout);
        TextView webTextView = newLayout.findViewById(R.id.web_match);
        webTextView.setText(webmatch.getName());
        webTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(webmatch.getName());
            }
        });
    }
    public void createWebView(final WebModel web){
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.web, null, false);
        entitiesLayout.addView(newLayout);
        TextView webTextView = newLayout.findViewById(R.id.web);
        TextView scoreTextView = newLayout.findViewById(R.id.webScore);
        webTextView.setText(web.getName());
        scoreTextView.setText(String.valueOf(web.getScore()));
        webTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.google.com/search?q="+web.getName());
            }
        });
    }

    public void createWebNullView(){
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.web, null, false);
        entitiesLayout.addView(newLayout);
        TextView webTextView = newLayout.findViewById(R.id.web);
        webTextView.setText("Web Entities Not Found");
    }

    public void createWebMatchNullView(){
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.web_matching_image, null, false);
        matchingLayout.addView(newLayout);
        TextView webMatchTextView = newLayout.findViewById(R.id.web_match);
        webMatchTextView.setText("Page Not Found");

    }

    public void goToUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(browserIntent);
    }
}
