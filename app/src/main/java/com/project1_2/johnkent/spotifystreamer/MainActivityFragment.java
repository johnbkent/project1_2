package com.project1_2.johnkent.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import kaaes.spotify.webapi.android.models.Artist;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private TrackAdapter trackAdapter;


    public MainActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (adapterView.getAdapter().getClass()==ArtistAdapter.class) {
                    Artist artist = (Artist) adapterView.getAdapter().getItem(position);
                    Intent intent = new Intent(getActivity(),TracksActivity.class);
                    intent.putExtra("id",artist.id);
                    intent.putExtra("name",artist.name);
                    startActivity(intent);
//                    new TrackTask().execute(artist.id);
                }
            }
        });
        return rootView;

        }







}
