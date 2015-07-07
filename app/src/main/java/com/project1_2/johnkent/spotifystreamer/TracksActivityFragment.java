package com.project1_2.johnkent.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TracksActivityFragment extends Fragment {
    private TrackAdapter trackAdapter;
//    private MusicParcelable parcelable;

    public TracksActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        getActivity().getActionBar().setSubtitle(args.getString("name"));
        new TrackTask().execute(args.getString("id"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracks, container, false);
    }

    private class TrackTask extends AsyncTask<String, Void, Tracks> {

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
