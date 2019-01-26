package dev.android.com.newsfeed;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static dev.android.com.newsfeed.MainActivity.LOG_TAG;

public final class QueryUtils {

    private QueryUtils(){}

    private static List<News> extractFeatureFromJson(String newsJSON ){
        if(TextUtils.isEmpty(newsJSON)) return null;

        List<News> newsList = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");

            for(int i = 0;i<newsArray.length();i++){
                JSONObject currentNews = newsArray.getJSONObject(i);
                String webPublicationDate = currentNews.getString("webPublicationDate");
                String webUrl = currentNews.getString("webUrl");
                JSONObject fields = currentNews.getJSONObject("fields");

                String headline = fields.getString("headline");
                String thumbnail = "";
                if(fields.has("thumbnail")== true){
                     thumbnail= fields.getString("thumbnail");
                }


                String author ="";
                if(fields.has("byline" ))
                        author= fields.getString("byline");


                String sectionName ="";
               // if(fields.has("sectionName"))
                        sectionName = currentNews.getString("sectionName");

                newsList.add(new News(thumbnail,headline,sectionName,webUrl,webPublicationDate,author));

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"",e);
        }

        return newsList;

    }

    private static URL createUrl(String stringUrl){
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonresponse = "";

        if(url == null){
            return jsonresponse;
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(1000/*milliseconds*/);
            urlConnection.setConnectTimeout(15000/*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                Log.v("Message","InputStream"+inputStream);
                jsonresponse = readFromStream(inputStream);
                Log.v(LOG_TAG,"Response code: "+urlConnection.getResponseCode());
            }
            else {
                Log.e(LOG_TAG,"Error response code: "+ urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the News JSON results",e);
        }
        finally {
            if(urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonresponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> fetchNewsData(String requestUrl){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request",e);

        }

        List<News> newsList  = extractFeatureFromJson(jsonResponse);

        return newsList;
    }
}
