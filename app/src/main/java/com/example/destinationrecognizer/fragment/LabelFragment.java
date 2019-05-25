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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.VisionActivity;
import com.example.destinationrecognizer.model.LabelModel;
import com.example.destinationrecognizer.model.LandmarkModel;

import java.util.List;

public class LabelFragment extends Fragment {
    private List<LabelModel> labels;
    private List<LandmarkModel> landmarks;
    private View rootView;

    public LabelFragment() {}

    public static LabelFragment newInstance() {
        LabelFragment fragment = new LabelFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_label, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        labels = ((VisionActivity) getActivity()).getLabels();
        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(labels.size()==0||landmarks.size()==0)createLabelNullView();
        else for (LabelModel label : this.labels) {
            createLabelView(label);
        }
    }

    public void createLabelNullView(){
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.labelLayout);
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.label, null, false);
        rootLayout.addView(newLayout);

        TextView labelTextView = newLayout.findViewById(R.id.label);
        TextView percentageTextView = newLayout.findViewById(R.id.percentage);
        ProgressBar percentageBar = newLayout.findViewById(R.id.percentageBar);
        labelTextView.setText("Label Not Found");
        percentageTextView.setVisibility(View.GONE);
        percentageBar.setVisibility(View.GONE);
    }

    public void createLabelView(final LabelModel label){
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.labelLayout);
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.label, null, false);
        rootLayout.addView(newLayout);

        TextView labelTextView = newLayout.findViewById(R.id.label);
        TextView percentageTextView = newLayout.findViewById(R.id.percentage);
        ProgressBar percentageBar = newLayout.findViewById(R.id.percentageBar);
        labelTextView.setText(label.getName());
        percentageTextView.setText(String.valueOf(label.getPercentage())+"%");
        percentageBar.setProgress(label.getPercentage());

        labelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.google.com/search?q="+label.getName());
            }
        });
    }

    public void goToUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(browserIntent);
    }
}
