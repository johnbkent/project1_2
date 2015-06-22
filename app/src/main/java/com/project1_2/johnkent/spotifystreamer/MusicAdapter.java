package com.project1_2.johnkent.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by John on 6/22/2015.
 */
public class MusicAdapter extends ArrayAdapter<String> {
    private ArrayList<ImageView> thumbs;
    private ArrayList<String> trackDetails;
    private String adapterType;

    //Use this constructor for the artist search
    public MusicAdapter(Context context, int resource, ArrayList<String> names, ArrayList<ImageView> drawables){
        super(context, resource, names);
        thumbs=drawables;
        adapterType="artist";
    }
    //Use this constructor for the top 10 tracks adapter
    public MusicAdapter(Context context, int resource, ArrayList<String> names, ArrayList<String> details, ArrayList<ImageView> drawables){
        super(context, resource, names);
        thumbs=drawables;
        trackDetails=details;
        adapterType="tracks";


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView image = thumbs.get(position);
        String text = getItem(position);
        if (convertView == null && adapterType.equals("artist")){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_detail, parent, false);
        } else if (convertView == null && adapterType.equals("tracks")){
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.tracks_detail, parent, false);
            //TextView itemDetails=convertView.findViewById(R.id.list_details);
            //itemDetails.setText(details.get(position);
        }
        TextView itemName = (TextView) convertView.findViewById(R.id.list_text);
        ImageView itemThumb =(ImageView) convertView.findViewById(R.id.list_thumb);
        itemName.setText(text);
        itemThumb.setImageDrawable(image.getDrawable());
        return convertView;
    }


}
