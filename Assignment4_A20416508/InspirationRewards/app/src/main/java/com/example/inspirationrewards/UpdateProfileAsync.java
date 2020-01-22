package com.example.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class UpdateProfileAsync extends AsyncTask<String, Void, String> {
    private static final String TAG = "UpdateProfileAsync";
    private static final String baseUrl =
            "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String profiles ="/profiles";
    private EditProfile editProfile;
    private String username, password, studentid;
    public UpdateProfileAsync(EditProfile editProfile) {
        this.editProfile = editProfile;
    }
    @Override
    protected void onPostExecute(String connectionResult){
        // Normally we would parse the results and make use of the data
        // For this example, we just use the returned string size - empty is fail
        if (connectionResult.contains("error")) // If there is "error" in the results...
        {
            editProfile.sendResults("FAILED", null, null, null);
            Log.d("hello", "onPostExecute: Updated failed!!!!!!");
        }

        else {
            editProfile.sendResults("SUCCESS", studentid, username, password);
            Log.d("hello", "onPostExecute: Updated Done!!!!!!");
        }
    }
    @Override
    protected String doInBackground(String... strings) {
        String sID = strings[0];
        studentid = sID;
        String user = strings[1];
        username = user;
        String pass = strings[2];
        password = pass;
        String first = strings[3];
        String second = strings[4];
        int pointstoAward = Integer.parseInt(strings[5]);
        String department = strings[6];
        String edit = strings[7];
        String pos = strings[8];
        String admin = strings[9];
        String loc = strings[10];
        String imageBytes = strings[11];
       // String rewardRecords = strings[12];
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("studentId", sID);
            jsonObject.put("username", user);
            jsonObject.put("password", pass);
            jsonObject.put("firstName", first);
            jsonObject.put("lastName", second);
            jsonObject.put("pointsToAward",pointstoAward);
            jsonObject.put("department", department);
            jsonObject.put("story", edit);
            jsonObject.put("position", pos);

            jsonObject.put("admin", admin);
            jsonObject.put("location", loc);
            jsonObject.put("imageBytes", imageBytes);
            JSONArray rewardRecords=new JSONArray();
            jsonObject.put("rewards", rewardRecords);
            Log.d("from JSON",jsonObject.toString());


            return doAPICall(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String doAPICall(JSONObject jsonObject) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;


        try {

            String urlString = baseUrl + profiles;  // Build the full URL

            Uri uri = Uri.parse(urlString);    // Convert String url to URI
            URL url = new URL(uri.toString()); // Convert URI to URL

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");  // POST - others might use PUT, DELETE, GET

            // Set the Content-Type and Accept properties to use JSON data
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            // Write the JSON (as String) to the open connection
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();
            Log.d(TAG, "doAPICall:Out stream writer working ");

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();

            // If successful (HTTP_OK)
            if (responseCode == HTTP_OK) {

                // Read the results - use connection's getInputStream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }
                Log.d("Updating Profile", "doAPICall: Found" + result.toString());
                // Return the results (to onPostExecute)
                return result.toString();

            } else {
                // Not HTTP_OK - some error occurred - use connection's getErrorStream
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }
                Log.d("Updating Profile", "doAPICall: Not found" + result.toString());
                // Return the results (to onPostExecute)
                return result.toString();

            }

        } catch (Exception e) {
            // Some exception occurred! Log it.
            Log.d("Updating Profile", "doAuth: " + e.getClass().getName() + ": " + e.getMessage());

        } finally { // Close everything!
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Updating Profile", "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }
        return "Some error has occurred"; // Return an error message if Exception occurred
    }



}
