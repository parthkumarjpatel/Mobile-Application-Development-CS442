package com.assignment.parth.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewsSourceDownloader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity ;

    private final String URL_ALL = "https://newsapi.org/v2/sources?language=en&country=us&apiKey=267edc0f51d34eb997cef9b25c626fde";
    private final String URL_CATEGORY_HEAD = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private final String URL_CATEGORY_TAIL = "&apiKey=";
    private final String KEY = "267edc0f51d34eb997cef9b25c626fde";
    private static final String TAG = "NewsSourceDownloader";

    public NewsSourceDownloader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null) {
            mainActivity.noDataFound();
        } else {
            ArrayList<Sources> sources_data = parseJSON(s);
            mainActivity.sources_data_to_add(sources_data);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        Uri dataUri;
        if (params.length == 0) {
            dataUri = Uri.parse(URL_ALL);
        } else if (params[0].equals("all")) {
            dataUri = Uri.parse(URL_ALL);
        } else
            dataUri = Uri.parse(URL_CATEGORY_HEAD + params[0] + URL_CATEGORY_TAIL + KEY);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int HTTP_NOT_FOUND = conn.getResponseCode();
            if (HTTP_NOT_FOUND == 404) {
                return null;
            } else {
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return sb.toString();

            }
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Sources> parseJSON(String s) {
        try {
            Log.d(TAG, "parseJSON: parsing JSON");

            ArrayList<Sources> sourcesList = new ArrayList<>();

            String Name = null;
            String Id = null;
            String Category = null;
            String Url = null;

            JSONObject jsonObject = new JSONObject(s);
            JSONArray sources = jsonObject.getJSONArray("sources");
            Log.d(TAG, "Length " + sources.length());

            for (int i = 0; i < sources.length(); i++) {
                JSONObject source_object = (JSONObject) sources.get(i);
                Id = source_object.getString("id");
                Name = source_object.getString("name");
                Url = source_object.getString("url");
                Category = source_object.getString("category");
                sourcesList.add(new Sources(Id, Name, Url, Category));
            }

            return sourcesList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

