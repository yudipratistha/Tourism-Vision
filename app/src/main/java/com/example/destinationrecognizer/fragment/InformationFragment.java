package com.example.destinationrecognizer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

<<<<<<< HEAD
import com.example.destinationrecognizer.DetailActivity;
import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.VisionActivity;
import com.example.destinationrecognizer.model.LandmarkModel;

import java.util.List;

public class InformationFragment extends Fragment {
    private View rootView;
    private TextView infoNotFound;
=======
import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.VisionActivity;

public class InformationFragment extends Fragment {
    private View rootView;
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
    private TextView namaPariwisata;
    private TextView alamat;
    private TextView deskripsi;
    private TextView luasArea;
    private TextView fasilitas;
<<<<<<< HEAD
    private TextView alamat_label;
    private TextView deskripsi_label;
    private TextView luas_label;
    private TextView fasilitas_label;
    private List<LandmarkModel> landmarks;

=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344

    public InformationFragment() {}

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        return rootView;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

<<<<<<< HEAD
        infoNotFound = (TextView)rootView.findViewById(R.id.infoNotFound);
=======
>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
        namaPariwisata = (TextView)rootView.findViewById(R.id.nama_pariwisata);
        alamat = (TextView)rootView.findViewById(R.id.alamat_pariwisata);
        deskripsi = (TextView)rootView.findViewById(R.id.deskripsi_pariwisata);
        luasArea = (TextView)rootView.findViewById(R.id.luas_area_pariwisata);
        fasilitas = (TextView)rootView.findViewById(R.id.fasilitas_pariwisata);
<<<<<<< HEAD
        alamat_label = (TextView)rootView.findViewById(R.id.alamat_label);
        deskripsi_label = (TextView)rootView.findViewById(R.id.deskripsi_label);
        luas_label = (TextView)rootView.findViewById(R.id.luas_label);
        fasilitas_label = (TextView)rootView.findViewById(R.id.fasilitas_label);

        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(landmarks.size()!=0 && ((VisionActivity) getActivity()).getVisionModel().getVisionName()!=null){
            infoNotFound.setVisibility(View.GONE);
            namaPariwisata.setText(((VisionActivity) getActivity()).getVisionModel().getVisionName());
            alamat.setText(((VisionActivity) getActivity()).getVisionModel().getAlamat());
            deskripsi.setText(((VisionActivity) getActivity()).getVisionModel().getDeskripsi());
            luasArea.setText(((VisionActivity) getActivity()).getVisionModel().getType());
            if(((VisionActivity) getActivity()).getVisionModel().getPrice()!=null)
                fasilitas.setText("USD $"+((VisionActivity) getActivity()).getVisionModel().getPrice());
            else fasilitas.setText("free");        }
        else{
            infoNotFound.setText("Information Not Found");
            namaPariwisata.setVisibility(View.GONE);
            alamat.setVisibility(View.GONE);
            deskripsi.setVisibility(View.GONE);
            luasArea.setVisibility(View.GONE);
            fasilitas.setVisibility(View.GONE);
            alamat_label.setVisibility(View.GONE);
            deskripsi_label.setVisibility(View.GONE);
            luas_label.setVisibility(View.GONE);
            fasilitas_label.setVisibility(View.GONE);
        }
=======

        namaPariwisata.setText(((VisionActivity) getActivity()).getVisionModel().getVisionName());
        alamat.setText(((VisionActivity) getActivity()).getVisionModel().getAlamat());
        deskripsi.setText(((VisionActivity) getActivity()).getVisionModel().getDeskripsi());
        luasArea.setText(((VisionActivity) getActivity()).getVisionModel().getLuasArea());
        fasilitas.setText(((VisionActivity) getActivity()).getVisionModel().getFasilitas());


>>>>>>> 730fa9ab3fe5d6d8c58fccd2fb1adcf83ea7d344
    }
}
