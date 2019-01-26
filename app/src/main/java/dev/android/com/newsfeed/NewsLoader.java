package dev.android.com.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static dev.android.com.newsfeed.MainActivity.LOG_TAG;

public class NewsLoader extends AsyncTaskLoader<List<News>>{

    private String mUrl;
    public NewsLoader(Context context, String url){
        super(context);
        mUrl  = url;
    }
    @Override
    public List<News> loadInBackground() {
        if(mUrl == null) return null;
            Log.v(LOG_TAG,"loadinBackground()");
            List<News> newsList = QueryUtils.fetchNewsData(mUrl);
            return newsList;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"onStartLoading()");
        forceLoad();
    }
}
