package com.example.destinationrecognizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.destinationrecognizer.fragment.InformationFragment2;
import com.example.destinationrecognizer.fragment.LabelFragment2;
import com.example.destinationrecognizer.fragment.LandmarkFragment2;
import com.example.destinationrecognizer.fragment.WebFragment2;
import com.example.destinationrecognizer.model.LabelModel;
import com.example.destinationrecognizer.model.LandmarkModel;
import com.example.destinationrecognizer.model.VisionModel1;
import com.example.destinationrecognizer.model.WebMatchingModel;
import com.example.destinationrecognizer.model.WebModel;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private Destination destination;
    private List<LandmarkModel> landmarks;
    private List<LabelModel> labels;
    private List<WebModel> webs;
    private List<WebMatchingModel> webMatchs;
    private ImageView imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        imv = (ImageView)findViewById((R.id.imageVision));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        destination = (Destination) bundle.getSerializable("Destination");
        landmarks = destination.getLandmark();
        labels = destination.getLabel();
        webs = destination.getWebEntities();
        webMatchs = destination.getPageMatchingImages();
        showFragment();

        if(getIntent().hasExtra("byteArray")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
            getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            imv.setImageBitmap(bitmap);
        }else{
            Drawable res = ContextCompat.getDrawable(this, R.drawable.mountain);
            imv.setImageDrawable(res);
        }
    }

    public List<LabelModel> getLabels(){
        return this.labels;
    }
    public List<LandmarkModel> getLandmarks(){
        return this.landmarks;
    }
    public List<WebModel> getWebs(){
        return this.webs;
    }
    public List<WebMatchingModel> getWebMatchs(){
        return this.webMatchs;
    }
    public Destination getDestination() {return this.destination;}


    private void showFragment(){
        BottomNavigationView bottomNavigation;
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_task:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, LandmarkFragment2.newInstance(), LandmarkFragment2.class.getSimpleName())
                            .commit();

                    break;
                case R.id.nav_task_done:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, LabelFragment2.newInstance(), LabelFragment2.class.getSimpleName())
                            .commit();

                    break;
                case R.id.nav_user:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, WebFragment2.newInstance(), WebFragment2.class.getSimpleName())
                            .commit();

                    break;
                case R.id.nav_information:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, InformationFragment2.newInstance(), InformationFragment2.class.getSimpleName())
                            .commit();

                    break;
            }
            return true;
            }

        });
        bottomNavigation.setSelectedItemId(R.id.nav_information);
    }
}
