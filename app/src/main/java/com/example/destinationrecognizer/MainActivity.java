package com.example.destinationrecognizer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
<<<<<<< HEAD
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.destinationrecognizer.API.APIClient;
import com.example.destinationrecognizer.API.APIService;
import com.example.destinationrecognizer.adapter.DestinationsAdapter;
import com.example.destinationrecognizer.model.LandmarkModel;
import com.example.destinationrecognizer.model.VisionModel;
import com.example.destinationrecognizer.model.VisionModel1;
import com.example.destinationrecognizer.model.VisionModelList;
=======
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.destinationrecognizer.adapter.DestinationsAdapter;
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DestinationsAdapter adapter;
    private List<Destination> destinationList;
    private FloatingActionButton fab;

<<<<<<< HEAD
    protected APIService service;
    private VisionModel1 visionModel;

=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        fab = findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

<<<<<<< HEAD
        service = APIClient.getService();
        visionModel = new VisionModel1();
=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
        destinationList = new ArrayList<>();
        adapter = new DestinationsAdapter(this, destinationList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.mountain).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
<<<<<<< HEAD
        service.getAllData((float) -8.637694, (float)115.22279)
            .enqueue(new Callback<VisionModelList>() {
                @Override
                public void onResponse(Call<VisionModelList> call, Response<VisionModelList> response) {
                    if (response.isSuccessful()) {
                        for (VisionModel1 vision : response.body().getVisionModelList()) {
                            Destination destination = new Destination();
                            destination.setVisionName(vision.getVisionName());
                            destination.setAlamat(vision.getAlamat());
                            destination.setDeskripsi(vision.getDeskripsi());
                            destination.setImageBase64(vision.getImageBase64());
                            destination.setPrice(vision.getPrice());
                            destination.setType(vision.getType());
                            destination.setLabel(vision.getLabel());
                            destination.setLandmark(vision.getLandmark());
                            destination.setPageMatchingImages(vision.getPageMatchingImages());
                            destination.setWebEntities(vision.getWebEntities());
                            destinationList.add(destination);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Location Not Found", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<VisionModelList> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Gagal 2" + t, Toast.LENGTH_LONG).show();
                }
            });
=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
        int[] covers = new int[]{
                R.drawable.bedugul,
                R.drawable.tanahlot,
                R.drawable.bajrasandhi,
                R.drawable.liberty,
        };
<<<<<<< HEAD
=======

        Destination a = new Destination("Bedugul", "Bali", covers[0]);
        destinationList.add(a);
        a = new Destination("Tanah Lot", "Bali", covers[1]);
        destinationList.add(a);
        a = new Destination("Bajrasandhi", "Bali", covers[2]);
        destinationList.add(a);
        a = new Destination("Patung Liberty", "USA", covers[3]);
        destinationList.add(a);

        adapter.notifyDataSetChanged();
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}