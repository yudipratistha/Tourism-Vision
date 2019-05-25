package com.example.destinationrecognizer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.destinationrecognizer.Destination;
import com.example.destinationrecognizer.DetailActivity;
import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.model.VisionModel1;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Destination> destinationList;


    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public TextView title, region;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            region = (TextView) view.findViewById(R.id.region);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position  =   getAdapterPosition();
            Destination destination = destinationList.get(position);

            Intent detail_activity = new Intent(view.getContext(), DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Destination", destination);
            detail_activity.putExtras(bundle);

            if(destination.getImageBase64()!=null) {
                byte[] decodedString = Base64.decode(destination.getImageBase64(), Base64.DEFAULT);
                detail_activity.putExtra("byteArray", decodedString);
                detail_activity.putExtra("isImage", "true");
            }
            view.getContext().startActivity(detail_activity);
        }
    }


    public DestinationsAdapter(Context mContext, List<Destination> destinationList) {
        this.mContext = mContext;
        this.destinationList = destinationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destination_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.title.setText(destination.getVisionName());
        holder.region.setText(destination.getType());

        if(destination.getImageBase64()!=null) {
            byte[] decodedString = Base64.decode(destination.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //holder.thumbnail.setImageBitmap(decodedByte);
            Glide.with(mContext).load(bitmapToByte(decodedByte)).asBitmap().into(holder.thumbnail);
        }

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

    }

    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.destination_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }
}