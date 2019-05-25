package com.example.destinationrecognizer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.destinationrecognizer.DetailActivity;
import com.example.destinationrecognizer.R;

public class InformationFragment2 extends Fragment {
    private View rootView;
    private TextView namaPariwisata;
    private TextView infoNotFound;
    private TextView alamat;
    private TextView deskripsi;
    private TextView luasArea;
    private TextView fasilitas;

    public InformationFragment2() {}

    public static InformationFragment2 newInstance() {
        InformationFragment2 fragment = new InformationFragment2();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        return rootView;    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        infoNotFound = (TextView)rootView.findViewById(R.id.infoNotFound);
        namaPariwisata = (TextView)rootView.findViewById(R.id.nama_pariwisata);
        alamat = (TextView)rootView.findViewById(R.id.alamat_pariwisata);
        deskripsi = (TextView)rootView.findViewById(R.id.deskripsi_pariwisata);
        luasArea = (TextView)rootView.findViewById(R.id.luas_area_pariwisata);
        fasilitas = (TextView)rootView.findViewById(R.id.fasilitas_pariwisata);

        infoNotFound.setVisibility(View.GONE);
        namaPariwisata.setText(((DetailActivity) getActivity()).getDestination().getVisionName());
        alamat.setText(((DetailActivity) getActivity()).getDestination().getAlamat());
        deskripsi.setText(((DetailActivity) getActivity()).getDestination().getDeskripsi());
        luasArea.setText(((DetailActivity) getActivity()).getDestination().getType());
        if(((DetailActivity) getActivity()).getDestination().getPrice()!=null)
        fasilitas.setText("USD $"+((DetailActivity) getActivity()).getDestination().getPrice());
        else fasilitas.setText("free");



    }
}
