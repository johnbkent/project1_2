package com.project1_2.johnkent.spotifystreamer;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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


public class MainActivity extends ActionBarActivity {
    private ArtistAdapter artistAdapter;
    private TextView infoBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null) {
            Intent intent = getIntent();
            if (intent != null && intent.getAction() != null) {
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    String query = intent.getStringExtra(SearchManager.QUERY);
                    query.replaceAll(" ", "+");
                    new ArtistSearchTask().execute(query);
                }
            }
        }
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

        @Override
        protected ArtistsPager doInBackground(String... params){
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results;

        }
        @Override

        protected void onPostExecute(ArtistsPager results){
            infoBar = (TextView) findViewById(R.id.infoBar);
            if (artistAdapter==null&& !results.artists.items.isEmpty()) {
                artistAdapter=new ArtistAdapter(getApplicationContext(),R.id.list_item,new ArrayList<>(results.artists.items.subList(0,results.artists.items.size()-1)));
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(artistAdapter);
                infoBar.setText(R.string.track_prompt);
            } else if (!results.artists.items.isEmpty()) {
                artistAdapter.clear();
                for (Artist artist : results.artists.items){
                    artistAdapter.add(artist);
                }
                artistAdapter.notifyDataSetChanged();
            } else {
                infoBar.setText(R.string.artist_error);

            }


        }

    }




}
