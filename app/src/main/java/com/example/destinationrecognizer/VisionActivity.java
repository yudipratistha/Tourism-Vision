package com.example.destinationrecognizer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destinationrecognizer.API.APIClient;
import com.example.destinationrecognizer.API.APIService;
import com.example.destinationrecognizer.fragment.InformationFragment;
import com.example.destinationrecognizer.fragment.LabelFragment;
import com.example.destinationrecognizer.fragment.LandmarkFragment;
import com.example.destinationrecognizer.fragment.WebFragment;
import com.example.destinationrecognizer.model.LabelModel;
import com.example.destinationrecognizer.model.LandmarkModel;
import com.example.destinationrecognizer.model.VisionModel;
import com.example.destinationrecognizer.model.VisionModel1;
import com.example.destinationrecognizer.model.WebMatchingModel;
import com.example.destinationrecognizer.model.WebModel;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.LocationInfo;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;
import com.google.api.services.vision.v1.model.WebPage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisionActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyC4aXuQgjZw7Fzaooq6NfGLDMg8qINE4K4";
    private String[] visionAPI = new String[]{"LANDMARK_DETECTION", "LABEL_DETECTION", "WEB_DETECTION", "LOGO_DETECTION", "SAFE_SEARCH_DETECTION", "IMAGE_PROPERTIES"};
    private ImageView imv;
    private TextView responseText;
    private TextView textProgress;
    private ProgressBar imageUploadProgress;
    private LinearLayout navBar;
    private FrameLayout mainContainer;
    private String responses;
    private List<LandmarkModel> landmarks;
    private List<LabelModel> labels;
    private List<WebModel> webs;
    private List<WebMatchingModel> webMatchs;
    private VisionModel1 visionModel;
    private Bitmap rotatedBitmap;
    private Bitmap tempBitmap;
    private Canvas canvas ;
    private Paint p ;
    private LinearLayout linearVision;
    private float latitude;
    private float longitude;
    private boolean responseIsFound = false;
    protected APIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        service = APIClient.getService();
        visionModel = new VisionModel1();
        landmarks = new ArrayList<LandmarkModel>();
        labels = new ArrayList<LabelModel>();
        webs = new ArrayList<WebModel>();
        webMatchs = new ArrayList<WebMatchingModel>();
        imv = (ImageView)findViewById((R.id.imageVision));
        textProgress = (TextView) findViewById((R.id.textProgress));
        imageUploadProgress = (ProgressBar) findViewById((R.id.imageProgress));
        navBar = (LinearLayout) findViewById((R.id.bottom_layout));
        mainContainer = (FrameLayout) findViewById((R.id.main_container));
        linearVision = (LinearLayout) findViewById((R.id.linearVision));
        if(getIntent().hasExtra("path")) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(
//                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
//            Bitmap rotatedBitmap = rotateImage(bitmap, 90);
//            imv.setImageBitmap(rotatedBitmap);
            String path= getIntent().getStringExtra("path");
            Bundle bundle = getIntent().getExtras();
            latitude = bundle.getFloat("latitude");
            longitude = bundle.getFloat("longitude");
            File imgFile = new  File(path);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                rotatedBitmap = rotateImage(myBitmap, 90);
                tempBitmap = rotatedBitmap.copy(Bitmap.Config.ARGB_8888, true);
                canvas = new Canvas(tempBitmap);
                p = new Paint();
                imv.setImageBitmap(rotatedBitmap);
                Feature feature1 = new Feature();
                feature1.setType(visionAPI[0]);
                feature1.setMaxResults(10);
                Feature feature2 = new Feature();
                feature2.setType(visionAPI[1]);
                feature2.setMaxResults(10);
                Feature feature3 = new Feature();
                feature3.setType(visionAPI[2]);
                feature3.setMaxResults(10);
                callCloudVision(rotatedBitmap, feature1, feature2, feature3);
            };
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }

    private void callCloudVision(final Bitmap bitmap, final Feature feature1, final Feature feature2, final Feature feature3) {
        imageUploadProgress.setVisibility(View.VISIBLE);
        textProgress.setVisibility(View.VISIBLE);
        navBar.setVisibility(View.INVISIBLE);
        mainContainer.setVisibility(View.INVISIBLE);
        final List<Feature> featureList = new ArrayList<>();
        featureList.add(feature1);
        featureList.add(feature2);
        featureList.add(feature3);
        final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();
        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(featureList);
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d("error", "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d("error", "failed to make API request because of other IOException " + e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                if(!responseIsFound){
                    getVision();
                }
                else {
                    getVisionFound();
                }
            }
        }.execute();
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
    public VisionModel1 getVisionModel() {return this.visionModel;}

    //xyxyxyyxy
    //1028.0, 273.0  1425.0, 273.0  1425.0, 1290.0  1028.0, 1290.0
    //1028.0, 519.0  1556.0, 519.0  1556.0, 1267.0  1028.0, 1267.0
    //1155.0, 262.0  1422.0, 262.0  1422.0, 991.0  1155.0, 991.0

    public void setCanvas(String name, float x1,float x2,float x3,float x4,float y1,float y2,float y3,float y4){
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        p.setColor(Color.RED);
        p.setStrokeWidth(5f);
        p.setTextSize(rotatedBitmap.getHeight()/40); // Text Size
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
        canvas.drawLine(x1, y1, x2, y2, p);//up
        canvas.drawLine(x1, y1, x4, y4, p);//left
        canvas.drawLine(x4, y4, x3, y3, p);//down
        canvas.drawLine(x2, y2, x3, y3, p);//right
        p.setStrokeWidth(3f);
        canvas.drawText(name, x1+rotatedBitmap.getWidth()/120, y1+rotatedBitmap.getHeight()/40 ,p); //x=300,y=300
    }

    public void drawLandmark(){
//      imv.setImageBitmap(null);
        imv.draw(canvas);
        imv.setImageBitmap(tempBitmap);
    }

    public void getVision(){
        service.getVision((float)latitude, (float)longitude)
            .enqueue(new Callback<VisionModel>() {
                @Override
                public void onResponse(Call<VisionModel> call, Response<VisionModel> response) {
                    if (response.isSuccessful()) {
                        landmarks = response.body().getVisionModel1().getLandmark();
                        labels = response.body().getVisionModel1().getLabel();
                        webs = response.body().getVisionModel1().getWebEntities();
                        webMatchs = response.body().getVisionModel1().getPageMatchingImages();
                        visionModel.setVisionName(response.body().getVisionModel1().getVisionName());
                        visionModel.setAlamat(response.body().getVisionModel1().getAlamat());
                        visionModel.setDeskripsi(response.body().getVisionModel1().getDeskripsi());
                        visionModel.setType(response.body().getVisionModel1().getType());
                        visionModel.setPrice(response.body().getVisionModel1().getPrice());

                    } else {
                        Toast.makeText(VisionActivity.this, "Location Not Found", Toast.LENGTH_LONG).show();
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imv.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    imv.setLayoutParams(params);
                    showFragment();
                    imageUploadProgress.setVisibility(View.GONE);
                    textProgress.setVisibility(View.GONE);
                    navBar.setVisibility(View.VISIBLE);
                    mainContainer.setVisibility(View.VISIBLE);
                    imv.setImageBitmap(null);
                    for (LandmarkModel landmark : landmarks) {
                        setCanvas(landmark.getName(), landmark.getX1(), landmark.getX2(),
                                landmark.getX3(), landmark.getX4(), landmark.getY1(), landmark.getY2(), landmark.getY3(), landmark.getY4());
                    }
                    drawLandmark();
                }
                @Override
                public void onFailure(Call<VisionModel> call, Throwable t) {
                    Toast.makeText(VisionActivity.this, "Gagal 2" + t, Toast.LENGTH_LONG).show();
                }
            });
    }

    public void getVisionFound(){
        service.getVision((float)latitude, (float)longitude)
                .enqueue(new Callback<VisionModel>() {
                    @Override
                    public void onResponse(Call<VisionModel> call, Response<VisionModel> response) {
                        if (response.isSuccessful()) {
                            visionModel.setVisionName(response.body().getVisionModel1().getVisionName());
                            visionModel.setAlamat(response.body().getVisionModel1().getAlamat());
                            visionModel.setDeskripsi(response.body().getVisionModel1().getDeskripsi());
                            visionModel.setType(response.body().getVisionModel1().getType());
                            visionModel.setPrice(response.body().getVisionModel1().getPrice());

                        } else {
                            Toast.makeText(VisionActivity.this, "Location Not Found", Toast.LENGTH_LONG).show();
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imv.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                        imv.setLayoutParams(params);
                        showFragment();
                        imageUploadProgress.setVisibility(View.GONE);
                        textProgress.setVisibility(View.GONE);
                        navBar.setVisibility(View.VISIBLE);
                        mainContainer.setVisibility(View.VISIBLE);
                        imv.setImageBitmap(null);
                        for (LandmarkModel landmark : landmarks) {
                            setCanvas(landmark.getName(), landmark.getX1(), landmark.getX2(),
                                    landmark.getX3(), landmark.getX4(), landmark.getY1(), landmark.getY2(), landmark.getY3(), landmark.getY4());
                        }
                        drawLandmark();
                    }
                    @Override
                    public void onFailure(Call<VisionModel> call, Throwable t) {
                        Toast.makeText(VisionActivity.this, "Gagal 2" + t, Toast.LENGTH_LONG).show();
                    }
                });
    }

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
                            .replace(R.id.main_container, LandmarkFragment.newInstance(), LandmarkFragment.class.getSimpleName())
                            .commit();

                    break;
                case R.id.nav_task_done:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, LabelFragment.newInstance(), LabelFragment.class.getSimpleName())
                            .commit();

                    break;
                case R.id.nav_user:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, WebFragment.newInstance(), WebFragment.class.getSimpleName())
                            .commit();

                    break;
                case R.id.nav_information:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, InformationFragment.newInstance(), InformationFragment.class.getSimpleName())
                            .commit();

                    break;
            }
            return true;
            }

        });
        bottomNavigation.setSelectedItemId(R.id.nav_information);
    }

    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(bitmap.getByteCount()>4000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        else bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        AnnotateImageResponse imageResponses = response.getResponses().get(0);
        List<EntityAnnotation> entityAnnotations;
        List<EntityAnnotation> entityAnnotations2;

        String message = "";
        entityAnnotations = imageResponses.getLandmarkAnnotations();
        message = formatLandmarkAnnotation(entityAnnotations);
        message=message+"\n\n";
        entityAnnotations2 = imageResponses.getLabelAnnotations();
        message = message + formatLabelAnnotation(entityAnnotations2);
        message=message+"\n\n";
        WebDetection webAnnotations  = imageResponses.getWebDetection();
        message = message + formatWebAnnotation(webAnnotations);
        return message;
    }

    private String formatWebAnnotation(WebDetection entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            if(entityAnnotation.getWebEntities() !=null)
            for (WebEntity entity : entityAnnotation.getWebEntities()) {
                WebModel web = new WebModel();
                web.setName(entity.getDescription());
                web.setScore(entity.getScore());
                if(entity.getDescription()!=null && entity.getScore()!=null)webs.add(web);
            }
            else message = "NothingFound";
            if(entityAnnotation.getPagesWithMatchingImages() !=null)
            for (WebPage page : entityAnnotation.getPagesWithMatchingImages()) {
                WebMatchingModel webMatch = new WebMatchingModel();
                webMatch.setName(page.getUrl());
                if(page.getUrl()!=null)webMatchs.add(webMatch);
            }
        } else {
            message = "NothingFound";
        }
        return message;
    }

    private String formatLandmarkAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                LocationInfo info = entity.getLocations().listIterator().next();
                LandmarkModel landmark = new LandmarkModel();
                landmark.setName(entity.getDescription());
                landmark.setScore(entity.getScore());
                landmark.setLat(info.getLatLng().getLatitude());
                landmark.setLng(info.getLatLng().getLongitude());

                landmark.setX1(entity.getBoundingPoly().getVertices().listIterator(0).next().getX());
                landmark.setY1(entity.getBoundingPoly().getVertices().listIterator(0).next().getY());

                landmark.setX2(entity.getBoundingPoly().getVertices().listIterator(1).next().getX());
                landmark.setY2(entity.getBoundingPoly().getVertices().listIterator(1).next().getY());

                landmark.setX3(entity.getBoundingPoly().getVertices().listIterator(2).next().getX());
                landmark.setY3(entity.getBoundingPoly().getVertices().listIterator(2).next().getY());

                landmark.setX4(entity.getBoundingPoly().getVertices().listIterator(3).next().getX());
                landmark.setY4(entity.getBoundingPoly().getVertices().listIterator(3).next().getY());


                if(entity.getDescription()!=null && entity.getScore()!=null){
                    landmarks.add(landmark);
                    responseIsFound = true;
                }
            }
        } else {
            message = "NothingFound";
            responseIsFound = false;
        }
        return message;
    }

    private String formatLabelAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                LabelModel label = new LabelModel();
                label.setName(entity.getDescription());
                label.setScore(entity.getScore());
                if(entity.getDescription()!=null && entity.getScore()!=null)labels.add(label);
            }
        } else {
            message = "NothingFound";
        }
        return message;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
