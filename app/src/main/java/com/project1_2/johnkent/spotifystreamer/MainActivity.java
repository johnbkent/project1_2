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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class MainActivity extends ActionBarActivity {
    private MusicAdapter artistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null ) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                doMySearch(query);
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

    private void doMySearch(String query){

        //ArrayList<String> names = new ArrayList<>();
        //ArrayList<ImageView> thumbs = new ArrayList<>();
        //ImageView imageView;
        //for (int count=0; count<10; count++){
            //imageView = new ImageView(getApplicationContext());

            //thumbs.add(imageView);
        //}
        //artistAdapter=new MusicAdapter(getApplicationContext(),R.id.fragment_main,names,thumbs);
        //ListView fragmentView =(ListView) findViewById(R.id.listView);
        //fragmentView.setAdapter(artistAdapter);
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


        }

    }

    private class ImageLoadTask extends AsyncTask<String, Void, ArrayList<ImageView>> {

        @Override
        protected ArrayList<ImageView> doInBackground(String... params) {
            ArrayList<ImageView> imageViewList = new ArrayList<>();
            ImageView imageView = new ImageView(getApplicationContext());
            for (String url : params) {
                Picasso.with(getApplicationContext()).load(url).into(imageView);
                imageViewList.add(imageView);
            }
            return imageViewList;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageView> results){

        }
    }



}
