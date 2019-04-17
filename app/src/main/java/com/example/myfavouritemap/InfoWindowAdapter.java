package com.example.myfavouritemap;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public InfoWindowAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.infowindow, null);

        TextView title = view.findViewById(R.id.title);
        TextView address = view.findViewById(R.id.address);
        ImageView img = view.findViewById(R.id.potholeimage);


        title.setText(marker.getTitle());
        address.setText(marker.getSnippet());

        MapDataDetails infoWindowData = (MapDataDetails) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);


        return view;
    }
}
