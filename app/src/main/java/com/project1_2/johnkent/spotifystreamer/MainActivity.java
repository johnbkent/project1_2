package com.project1_2.johnkent.spotifystreamer;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


public class MainActivity extends ActionBarActivity {
    private ArtistAdapter artistAdapter;
    private TextView infoBar;
    private ArrayList<Artist> currentArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null || !savedInstanceState.containsKey("artists")) {
            Intent intent = getIntent();
            if (intent != null && intent.getAction() != null) {
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    String query = intent.getStringExtra(SearchManager.QUERY);
                    query.replaceAll(" ", "+");
                    new ArtistSearchTask().execute(query);
                }
            }
        } else {
            currentArtists= new ArrayList<>();
            infoBar = (TextView) findViewById(R.id.infoBar);
            ArrayList<MusicParcelable> list = savedInstanceState.getParcelableArrayList("artists");
            //currentArtists.clear();
            for (MusicParcelable mp : list){
                Artist artist = new Artist();
                artist.images = new ArrayList<>();
                artist.name=mp.name;
                artist.id=mp.id;
                Image image = new Image();
                image.url=mp.imgUrl;
                artist.images.add(image);
                currentArtists.add(artist);

            }
            if (currentArtists.isEmpty()){
                infoBar.setText(R.string.artist_error);
            } else {
                infoBar.setText(R.string.track_prompt);
                artistAdapter = new ArtistAdapter(getApplicationContext(), R.id.list_item, currentArtists);
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(artistAdapter);
            }
        }

    }

    @Override
    protected void onSaveInstanceState (Bundle outState){
        ArrayList<MusicParcelable> parcelList = new ArrayList<>();
        String imgUrl;
        if (currentArtists!=null) {
            for (Artist artist : currentArtists) {
                imgUrl = !artist.images.isEmpty() ? artist.images.get(0).url : null;
                MusicParcelable mp = new MusicParcelable(artist.name, artist.id, imgUrl);
                parcelList.add(mp);
            }
            outState.putParcelableArrayList("artists", parcelList);
        }
        super.onSaveInstanceState(outState);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),MainActivity.class)));
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class ArtistSearchTask extends AsyncTask<String, Void, ArtistsPager> {
        private boolean networkStatus;

        @Override
        protected ArtistsPager doInBackground(String... params){
            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            networkStatus = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (networkStatus) {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(params[0]);
                return results;
            } else {
                return null;
            }

        }
        @Override
        protected void onPostExecute(ArtistsPager results){
            infoBar = (TextView) findViewById(R.id.infoBar);
            if (artistAdapter==null && results!=null && !results.artists.items.isEmpty()) {
                currentArtists = new ArrayList<>(results.artists.items.subList(0,results.artists.items.size()-1));
                artistAdapter=new ArtistAdapter(getApplicationContext(),R.id.list_item,currentArtists);
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(artistAdapter);
                infoBar.setText(R.string.track_prompt);
            } else if (results!=null && !results.artists.items.isEmpty()) {
                currentArtists = new ArrayList<>(results.artists.items.subList(0,results.artists.items.size()-1));
                artistAdapter.clear();
                for (Artist artist : currentArtists){
                    artistAdapter.add(artist);

                }
                artistAdapter.notifyDataSetChanged();
            } else if (!networkStatus){
                infoBar.setText(R.string.network_error);

            } else {
                infoBar.setText(R.string.artist_error);
            }


        }

    }




}
