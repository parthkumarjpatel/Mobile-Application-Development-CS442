package com.example.inspirationrewards;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;


public class LoginAPI extends AsyncTask<String, Void, String>  {
    private static final String TAG = "LoginAPI";
    private static final String baseUrl =
            "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String loginEndPoint ="/login";
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private EditProfile editProfile;
   YourProfile yourProfile;
    private String find;

    public LoginAPI(YourProfile yourProfile) {
        this.yourProfile = yourProfile;
    }

    public LoginAPI(EditProfile editProfile) {
        this.editProfile = editProfile;
    }

    public LoginAPI(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected void onPostExecute(String connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just use the returned string size - empty is fail
        if(find=="1")
        {
            if (connectionResult.contains("error")) // If there is "error" in the results...
            {
                mainActivity.sendResults1( connectionResult);
                mainActivity.makeToast();

            } else {
                Log.d(TAG,"result--------------"+connectionResult);
                mainActivity.sendResults( connectionResult);
                //mainActivity.loggedIn();
            }
        }
        else{
            if (connectionResult.contains("error")) // If there is "error" in the results...
                editProfile.result("FAILED");
            else
                editProfile.result(connectionResult);
        }


        }




    @Override
    protected String doInBackground(String... strings) {
        String stuId = strings[0];
        String uName = strings[1];
        String password = strings[2];
        find = strings[3];


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", stuId);
            jsonObject.put("username", uName);
            jsonObject.put("password",password );

            Log.d(TAG, "doInBackground: "+jsonObject.toString());
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

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                Log.d("succ", result.toString());
                JSONObject jsonObject1=new JSONObject(String.valueOf(result));
                String us=jsonObject1.getString("username");
                String ln=jsonObject1.getString("lastName");
                String fn=jsonObject1.getString("firstName");
                String ps =jsonObject1.getString("password");
                int result1=jsonObject1.getInt("pointsToAward");
               // Boolean adm1=jsonObject1.getBoolean("admin");
                String dept = jsonObject1.getString("department");
                String pos=jsonObject1.getString("position");
                String sty= jsonObject1.getString("story");
                String lct=jsonObject1.getString("location");
                String im=jsonObject1.getString("imageBytes");
                String rr=jsonObject1.getString("rewards" );
                //mainActivity.setData(ln,fn,us,lct,result1,dept,pos,sty,im,rr);
                Log.d(TAG, "doAPICall: Found in LoginAPI"+result.toString());
                return result.toString();

            } else {
                // Not HTTP_OK - some error occurred - use connection's getErrorStream
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }
                Log.d(TAG, "doAPICall: Not FOUND LoginAPI" + result.toString());
                // Return the results (to onPostExecute)
                return result.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Some exception occurred! Log it.
            //Log.d(TAG, "doAuth: " + e.getClass().getName() + ": " + e.getMessage());

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

