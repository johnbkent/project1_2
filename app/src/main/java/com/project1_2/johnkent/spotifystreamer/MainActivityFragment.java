package com.project1_2.johnkent.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.TracksPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArtistAdapter trackAdapter;

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
                Artist artist=(Artist) adapterView.getAdapter().getItem(position);
                new TrackTask().execute(artist.id);
            }
        });
        return rootView;

    }

    private class TrackTask extends AsyncTask<String, Void, TracksPager>{

        @Override
        protected TracksPager doInBackground(String... params){

            return new TracksPager();

        }

    }




}
