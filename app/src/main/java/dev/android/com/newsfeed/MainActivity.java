package dev.android.com.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<News>>{

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String GAURDIAN_REQUEST_URL ="https://content.guardianapis.com/search?show-fields=thumbnail%2Cheadline%2Cbyline&api-key=60ba8163-2233-4249-86e1-51d452fe76a9";
    //"https://content.guardianapis.com/search?api-key=60ba8163-2233-4249-86e1-51d452fe76a9";
    //"https://content.guardianapis.com/search?show-fields=thumbnail%2Cheadline%2Cbyline&order-by=oldest&api-key=60ba8163-2233-4249-86e1-51d452fe76a9";//
    //"https://content.guardianapis.com/search?show-fields=thumbnail%2Cheadline%2Cbyline&api-key=60ba8163-2233-4249-86e1-51d452fe76a9";

    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter newsArrayAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        ListView listView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID,null,this);
        }
        else{
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        newsArrayAdapter = new NewsAdapter(this,new ArrayList<News>());

        listView.setAdapter(newsArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = newsArrayAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,newsUri);
                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"onCreateLoader()");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.
                getString(
                        getString(R.string.settings_order_by_key),
                        getString(R.string.settings_order_by_default));

        String useDate = sharedPreferences.
                getString(
                    getString(R.string.settings_use_date_key),
                    getString(R.string.settings_use_date_default));



        Uri uri = Uri.parse(GAURDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = uri.buildUpon();
        //uriBuilder.appendQueryParameter("show-fields","thumbnail,headline,byline");
        uriBuilder.appendQueryParameter("order-by",orderBy);
        uriBuilder.appendQueryParameter("page-size","30");
        uriBuilder.appendQueryParameter("use-date",useDate);
        //uriBuilder.appendQueryParameter("api-key","60ba8163-2233-4249-86e1-51d452fe76a9");

        return new NewsLoader(this,uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquake);

        newsArrayAdapter.clear();

        if(data != null && !data.isEmpty()){
            Log.v(LOG_TAG,"onLoadFinished()");
            newsArrayAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.v(LOG_TAG,"onLoaderReset()");
        newsArrayAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
