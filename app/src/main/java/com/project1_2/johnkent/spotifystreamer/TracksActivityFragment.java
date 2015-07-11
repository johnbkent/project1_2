package com.project1_2.johnkent.spotifystreamer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TracksActivityFragment extends Fragment {
    private TrackAdapter trackAdapter;
    private ArrayList<Track> trackArrayList;
    private String artistName;

    public TracksActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TracksActivity parent = (TracksActivity) getActivity();
        ActionBar ab = parent.getSupportActionBar();
        if (savedInstanceState == null || !savedInstanceState.containsKey("tracks")) {
            Bundle args = getArguments();
            artistName = args.getString("name");
            ab.setSubtitle(artistName);
            ab.setDisplayHomeAsUpEnabled(true);
            new TrackTask().execute(args.getString("id"));
        } else {
            trackArrayList = new ArrayList<>();
            ArrayList<MusicParcelable> list = savedInstanceState.getParcelableArrayList("tracks");
            artistName = list.get(0).artistName;
            ab.setSubtitle(artistName);
            String resultsText = getString(R.string.track_results);
            resultsText+= " " + artistName;
            for (MusicParcelable mp : list){
                Track track = new Track();
                track.album = new AlbumSimple();
                track.album.images = new ArrayList<>();
                track.album.images.add(new Image());
                track.album.images.get(0).url=mp.imgUrl;
                track.preview_url=mp.previewUrl;
                track.album.name=mp.albumName;
                track.name=mp.trackName;
                trackArrayList.add(track);
            }
            TextView infoBar = (TextView) getActivity().findViewById(R.id.infoBar);
            if (trackArrayList.isEmpty()){
                infoBar.setText(R.string.track_error);
            } else {
                infoBar.setText(resultsText);
                trackAdapter = new TrackAdapter(getActivity().getApplicationContext(), R.id.list_item,trackArrayList);
                ListView listView = (ListView)getActivity().findViewById(R.id.listView);
                listView.setAdapter(trackAdapter);
            }

        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState){
        ArrayList<MusicParcelable> parcelList = new ArrayList<>();
        String imgUrl;
        if (trackArrayList!=null) {
            for (Track track : trackArrayList) {
                imgUrl = !track.album.images.isEmpty() ? track.album.images.get(0).url : null;
                MusicParcelable mp = new MusicParcelable(track.name, artistName, track.album.name,track.preview_url,imgUrl);
                parcelList.add(mp);
            }
            outState.putParcelableArrayList("tracks", parcelList);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_tracks, container, false);
        return view;
    }

    private class TrackTask extends AsyncTask<String, Void, Tracks> {
        private boolean networkStatus;

        @Override
        protected Tracks doInBackground(String... params){
            ConnectivityManager cm =
                    (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            networkStatus = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (networkStatus) {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                Map<String, Object> options = new HashMap<>();
                options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());
                Tracks tracksObject = spotify.getArtistTopTrack(params[0], options);
                return tracksObject;
            }
            else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(Tracks results){
            String resultsText = getString(R.string.track_results);
            resultsText+= " " + artistName;
            TextView infoBar =(TextView) getActivity().findViewById(R.id.infoBar);
            ListView listView = (ListView)getActivity().findViewById(R.id.listView);
            if (results!=null && trackAdapter==null&& !results.tracks.isEmpty()) {
                trackArrayList = new ArrayList<>(results.tracks);
                trackAdapter=new TrackAdapter(getActivity().getApplicationContext(),R.id.list_item,trackArrayList);
                listView.setAdapter(trackAdapter);
                infoBar.setText(resultsText);

            } else if (results!=null && !results.tracks.isEmpty()) {
                trackAdapter.clear();
                trackArrayList=new ArrayList<>(results.tracks);
                for (Track track : trackArrayList){
                    trackAdapter.add(track);
                }

                trackAdapter.notifyDataSetChanged();
                infoBar.setText(resultsText);
            } else if (!networkStatus) {
                infoBar.setText(R.string.network_error);

            } else {
                infoBar.setText(R.string.track_error);
            }

        }

    }
}
