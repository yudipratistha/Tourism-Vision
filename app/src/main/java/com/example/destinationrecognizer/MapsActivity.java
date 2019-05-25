package com.example.destinationrecognizer;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destinationrecognizer.API.AsyncResponse;
import com.example.destinationrecognizer.API.GetNearbyPlacesData;
import com.example.destinationrecognizer.model.PlaceModel;
import com.example.destinationrecognizer.view.AutoFitTextureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class MapsActivity extends AppCompatActivity implements LocationListener,SensorEventListener,OnMapReadyCallback {
    //location
    private double lat;
    private double lng;
    private int PROXIMITY_RADIUS = 50;
    LocationManager locationManager;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    //sensor
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mRRotate = new float[9];
    private float[] mOrientation = new float[3];
    private float x;
    private float y;
    private float z;
    private float prev_x = 123456789;
    private float prev_y = 123456789;
    private float prev_z = 123456789;
    private TextView textViewInfo;
    private TextView textDistance;
    private PlaceModel place;
    private List<PlaceModel> places;

    private ImageView imageMarker;
    private AutoFitTextureView textureView;
    private RelativeLayout markerLayout;
    private RelativeLayout markerLayout2;
    private RelativeLayout[] layout;
    private RelativeLayout relative;
    private RelativeLayout mapLayout;
    private Button btnTakePicture;

    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;

    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private float dpHeight;
    private float dpWidth;
    private int DSI_height;
    private int DSI_width;
    private boolean isView = false;
    private boolean viewIsAvailable = false;
    private boolean isMap = false;
    private int[] viewId;
    private GoogleMap mMap;
    private  Marker marker;
    private boolean visibility = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        CheckPermission();
        place = new PlaceModel();
        places = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnTakePicture = (Button)findViewById(R.id.takePicture);
        markerLayout2 = (RelativeLayout)findViewById((R.id.markerLayout));
        mapLayout = (RelativeLayout)findViewById((R.id.mapLayout));
        textureView = (AutoFitTextureView)findViewById(R.id.textureView);
        textureView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(visibility){
                    btnTakePicture.setVisibility(View.VISIBLE);
                    markerLayout2.setVisibility(View.INVISIBLE);
                    mapLayout.setVisibility(View.INVISIBLE);
                    visibility=false;
                }
                else{
                    btnTakePicture.setVisibility(View.INVISIBLE);
                    markerLayout2.setVisibility(View.VISIBLE);
                    mapLayout.setVisibility(View.VISIBLE);
                    visibility=true;
                }
            }
        });
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        //From Java 1.4 , you can use keyword 'assert' to check expression true or false
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DSI_height = displayMetrics.heightPixels;
        DSI_width = displayMetrics.widthPixels;
        dpHeight = displayMetrics.heightPixels;
        dpWidth = displayMetrics.widthPixels;
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.	SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.	SENSOR_DELAY_UI);
        getLocation();
        startBackgroundThread();
        if(textureView.isAvailable())
            openCamera();
        else
            textureView.setSurfaceTextureListener(textureListener);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        stopBackgroundThread();
        mSensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        clearMarker();
        viewIsAvailable = false;
        String url = getUrl(location.getLatitude(), location.getLongitude());
        Object[] DataTransfer = new Object[3];
        DataTransfer[0] = location.getLatitude();
        DataTransfer[1] = location.getLongitude();
        lat =  location.getLatitude();
        lng =  location.getLongitude();
        DataTransfer[2] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(
                new AsyncResponse(){
                    @Override
                    public void processfinished(List<PlaceModel> places_param) {
                        places = new ArrayList<>(places_param);
                        isView = true;
                        isMap = true;
                    }
                }
        );
        getNearbyPlacesData.execute(DataTransfer);
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    private String getUrl(double latitude, double longitude) {
        String types = "park";
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + types);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDujwaNWBwZlhDA0T6g9f2WnO7Oi-nhJxU");
        //Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometer) {
            mLastAccelerometer = lowPass(event.values.clone(), mLastAccelerometer);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            mLastMagnetometer = lowPass(event.values.clone(), mLastMagnetometer);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.remapCoordinateSystem(mR, SensorManager.AXIS_X, SensorManager.AXIS_Z, mRRotate);
            SensorManager.getOrientation(mRRotate, mOrientation);
            z = mOrientation[2];
            x = (float)(Math.toDegrees(mOrientation[0]));
            y = (float)(Math.toDegrees(mOrientation[1]));
            float x2=x;
            if(x2<0) x2+=360;
            LatLng latlng;
            if(isView) {
                int i = 0;
                viewId = new int[this.places.size()];
                layout = new RelativeLayout[this.places.size()];
                for (PlaceModel place : this.places) {
                    createView(place.getDistanceString(),place.getPlaceName(), (place.getAzimuth()-x)*20, (15-y)*30, i);
                    i++;
                }
                latlng = new LatLng(lat, lng);
                CameraPosition newPos = new CameraPosition(latlng, 19, 45, x2);
                marker.setPosition(latlng);
                marker.setRotation((float)x2);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPos));
                isView = false;
                viewIsAvailable = true;
            }
            if(isMap){
                latlng = new LatLng(lat, lng);
                marker.setRotation((float)x2);
                CameraPosition newPos = new CameraPosition(latlng, 19, 45, x2);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPos));
            }
            float move_x = x - prev_x;
            float move_y = y - prev_y;
            float move_z = z - prev_z;
            if(prev_x == 123456789) move_x = 0;
            if(prev_y == 123456789) move_y = 0;
            if(prev_z == 123456789) move_z = 0;
            if(viewIsAvailable) {
                int j =0;
                for (j=0; j<viewId.length; j++) {
                    markerLayout = layout[j].findViewById(viewId[j]);
                    move(markerLayout, move_x*20, move_y*30, (float)Math.toDegrees(z), 550, 550);
                }
            }
            prev_x = x;
            prev_y = y;
            prev_z = z;
        }
    }

    public void createView(String distance, String place, float leftMargin, float topMargin, int i){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        layout[i] = (RelativeLayout) inflater.inflate(R.layout.marker, null, false);
        relative = (RelativeLayout)findViewById(R.id.markerLayout);
        relative.addView(layout[i]);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout[i].getLayoutParams();
        params.setMargins(Math.round(leftMargin), Math.round(topMargin), 0,0);

        imageMarker = layout[i].findViewById(R.id.marker);
        textViewInfo = layout[i].findViewById(R.id.info);
        textDistance = layout[i].findViewById(R.id.distance);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.marker_home);
        Matrix matrix = new Matrix();
        float scale = ((float) 400) / bitmap.getWidth();
        matrix.setScale(scale, scale);
        imageMarker.setImageMatrix(matrix);
        textViewInfo.setHorizontallyScrolling(true);
        textDistance.setHorizontallyScrolling(true);
        textViewInfo.setText(place);
        textDistance.setText(distance);
        viewId[i] = layout[i].generateViewId();
        layout[i].setId(viewId[i]);
    }

    public void clearMarker(){
        relative = (RelativeLayout)findViewById(R.id.markerLayout);
        Log.e("a", String.valueOf(relative.getChildCount()));
        relative.removeAllViews();
    }

    public static void move(final RelativeLayout view, float x, float y, float z, float height, float width){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        int topMargin = params.topMargin- Math.round(y);
        int bottomMargin = params.bottomMargin;
        int leftMargin = params.leftMargin - Math.round(x);
        int rightMargin = params.rightMargin;

        params.setMargins(leftMargin,topMargin, rightMargin,bottomMargin);
//        view.setRotation(z*-1);
//        view.setPivotX(width);
//        view.setPivotY(height);
//        view.setRotation(z*-1);
        view.setLayoutParams(params);
    }

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + 0.22f * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        marker = mMap.addMarker(new MarkerOptions().position(sydney).title("You are here!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_marker_sm2))
                .flat(true));
        // Add a marker in Sydney and move the camera

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice=null;
        }
    };

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private void createCameraPreview() {
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MapsActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(cameraDevice == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            //Check realtime permission if run higher API 23
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return;
            }
            //textureView.setAspectRatio((int)dpWidth, (int)dpHeight);
            manager.openCamera(cameraId,stateCallback,null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread= null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void takePicture() {
        if(cameraDevice == null)
            return;
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null)
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

            //Capture image with custom size
            int width = 640;
            int height = 480;
            if(jpegSizes != null && jpegSizes.length > 0)
            {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Check orientation base on device
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));
            final String path = Environment.getExternalStorageDirectory()+"/"+ UUID.randomUUID().toString()+".jpg";
            file = new File(path);
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                        Intent intent = new Intent(getApplicationContext(), VisionActivity.class);
                        intent.putExtra("path", path);
                        intent.putExtra("latitude", (float)lat);
                        intent.putExtra("longitude", (float)lng);
                        startActivity(intent);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                        {
                            if(image != null)
                                image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream outputStream = null;
                    try{
                        outputStream = new FileOutputStream(file);
                        outputStream.write(bytes);
                    }finally {
                        if(outputStream != null)
                            outputStream.close();
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener,mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MapsActivity.this, "Saved "+file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.capture(captureBuilder.build(),captureListener,mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            },mBackgroundHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

}