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

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by John on 6/22/2015.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    //Use this constructor for the artist search
    public ArtistAdapter(Context context, int resource, ArrayList<Artist> artists){
        super(context, resource, artists);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Artist currentArtist=getItem(position);
        String name = currentArtist.name;
        String url = null;
        List<Image> images = currentArtist.images;
        if (images != null && !images.isEmpty()) {
            url = images.get(0).url;
        }

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_detail, parent, false);
        }
        TextView artistName = (TextView) convertView.findViewById(R.id.list_text);
        ImageView itemThumb =(ImageView) convertView.findViewById(R.id.list_thumb);
        artistName.setText(name);
        Picasso.with(getContext()).load(url).into(itemThumb);

        return convertView;
    }


}
