package com.example.destinationrecognizer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.VisionActivity;

public class InformationFragment extends Fragment {
    private View rootView;
    private TextView namaPariwisata;
    private TextView alamat;
    private TextView deskripsi;
    private TextView luasArea;
    private TextView fasilitas;

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

        namaPariwisata = (TextView)rootView.findViewById(R.id.nama_pariwisata);
        alamat = (TextView)rootView.findViewById(R.id.alamat_pariwisata);
        deskripsi = (TextView)rootView.findViewById(R.id.deskripsi_pariwisata);
        luasArea = (TextView)rootView.findViewById(R.id.luas_area_pariwisata);
        fasilitas = (TextView)rootView.findViewById(R.id.fasilitas_pariwisata);

        namaPariwisata.setText(((VisionActivity) getActivity()).getVisionModel().getVisionName());
        alamat.setText(((VisionActivity) getActivity()).getVisionModel().getAlamat());
        deskripsi.setText(((VisionActivity) getActivity()).getVisionModel().getDeskripsi());
        luasArea.setText(((VisionActivity) getActivity()).getVisionModel().getLuasArea());
        fasilitas.setText(((VisionActivity) getActivity()).getVisionModel().getFasilitas());


    }
}
