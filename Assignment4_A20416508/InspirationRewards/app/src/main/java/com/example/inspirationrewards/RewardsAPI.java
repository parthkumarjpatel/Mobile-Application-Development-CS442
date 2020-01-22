package com.example.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RewardsAPI extends AsyncTask<String, Void, String> {
    private AwardActivity awardActivity;
    private static final String TAG = "AddRewards";
    private static final String baseUrl = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String loginEndPoint ="/rewards";

    public RewardsAPI( AwardActivity  mainActivity) {
        this.awardActivity = mainActivity;
    }
    @Override
    protected String doInBackground(String... strings) {
        String tid = strings[0];
        String tusername = strings[1];
        String tname = strings[2];
        String tdate = strings[3];
        String tpoints = strings[4];
        String tcomments = strings[5];
        String s_id = strings[6];
        String s_username = strings[7];
        String s_password = strings[8];
        try{
            JSONObject main = new JSONObject();
            JSONObject target = new JSONObject();
            target.put("studentId",tid);
            target.put("username", tusername);
            target.put("name", tname);
            target.put("date", tdate);
            target.put("notes",tcomments);
            target.put("value",Integer.parseInt(tpoints));

            main.put("target",target);

            JSONObject source = new JSONObject();
            source.put("studentId",s_id);
            source.put("username", s_username);
            source.put("password", s_password);

            main.put("source", source);
            Log.d(TAG,"JSON Object-----------"+main.toString());
            return doAPICall(main);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String a) {
        Log.d(TAG,"in Post execute"+a);
        if(a.contains("error"))
        {
            awardActivity.sendResults("FAILED",a);
        }
        else{
            Log.d(TAG,""+ a);
            awardActivity.sendResults("SUCCESS",a);

        }
    }
    private String doAPICall(JSONObject jsonObject) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = baseUrl + loginEndPoint;  // Build the full URL

            Uri uri = Uri.parse(urlString);    // Convert String url to URI
            URL url = new URL(uri.toString()); // Convert URI to URL

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");  // POST - others might use PUT, DELETE, GET

            // Set the Content-Type and Accept properties to use JSON data
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            // Write the JSON (as String) to the open connection
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();

            // If successful (HTTP_OK)
            if (responseCode == HTTP_OK) {

                // Read the results - use connection's getInputStream
                Log.d(TAG, "yyyyyyyyyyyyyyyy");
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                Log.d(TAG, "API RUNNING"+result.toString());
                return result.toString();

            } else {
                // Not HTTP_OK - some error occurred - use connection's getErrorStream
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                return result.toString();
            }

        } catch (Exception e) {
            // Some exception occurred! Log it.
            Log.d(TAG, "doAuth: " + e.getClass().getName() + ": " + e.getMessage());

        } finally { // Close everything!
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }
        return "Some error has occurred"; // Return an error message if Exception occurred
    }


}
