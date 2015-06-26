package com.project1_2.johnkent.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by John on 6/26/2015.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    public TrackAdapter(Context context, int resource, ArrayList<Track> tracks){
        super(context,resource,tracks);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Track currentTrack=getItem(position);
        String name = currentTrack.name;
        String albumTitle = currentTrack.album.name;
        String url = null;
        List<Image> images = currentTrack.album.images;
        if (images != null && !images.isEmpty()) {
            url = images.get(0).url;
        }

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_detail, parent, false);
        }
        TextView trackTitle = (TextView) convertView.findViewById(R.id.trackTitle);
        TextView trackAlbum = (TextView) convertView.findViewById(R.id.trackAlbum);
        ImageView itemThumb =(ImageView) convertView.findViewById(R.id.imageViewTrack);
        trackTitle.setText(name);
        trackAlbum.setText(albumTitle);
        Picasso.with(getContext()).load(url).into(itemThumb);

        return convertView;
    }
}
