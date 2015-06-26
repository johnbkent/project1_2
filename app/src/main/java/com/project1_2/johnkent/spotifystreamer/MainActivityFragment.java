package com.project1_2.johnkent.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


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
                    new TrackTask().execute(artist.id);
                }
            }
        });
        return rootView;

        }


    private class TrackTask extends AsyncTask<String, Void, Tracks>{

        @Override
        protected Tracks doInBackground(String... params){
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());
            Tracks tracksObject = spotify.getArtistTopTrack(params[0],options);
            return tracksObject;

        }

        @Override
        protected void onPostExecute(Tracks results){
            TextView infoBar =(TextView) getActivity().findViewById(R.id.infoBar);
            ListView listView = (ListView)getActivity().findViewById(R.id.listView);
            if (trackAdapter==null&& !results.tracks.isEmpty()) {
                trackAdapter=new TrackAdapter(getActivity().getApplicationContext(),R.id.list_item,new ArrayList<>(results.tracks.subList(0,results.tracks.size()-1)));
                listView.setAdapter(trackAdapter);
                infoBar.setText(R.string.track_results);
            } else if (!results.tracks.isEmpty()) {
                listView.setAdapter(trackAdapter);
                trackAdapter.clear();
                for (Track track : results.tracks){
                    trackAdapter.add(track);
                }
                infoBar.setText(R.string.track_results);
                trackAdapter.notifyDataSetChanged();
            } else {
                infoBar.setText(R.string.track_error);

            }

        }

    }




}
