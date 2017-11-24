package com.example.supertec.php;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.acl.LastOwnerException;




/**
 * Created by Supertec on 11/23/2017.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    String username,password;

    AlertDialog alertDialog;

    Context context;


    BackgroundWorker(Context ctxt){
        context = ctxt;
    }

    public ProgressDialog pdLoading;

    @Override
    protected String doInBackground(String... params) {


        String login_url = "http://7c767094.ngrok.io/login";
        HttpURLConnection con = null;

        try {
            username = params[1];
            password = params[2];
            URL url = new URL(login_url);


            con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStream outputStream = con.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username",username );
                jsonObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bufferedWriter.write(String.valueOf(jsonObject));
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            con.connect();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "Conection Error"+e1.toString();
        }

        try {

            /* Responese */

            int response_code = con.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Pass data to onPostExecute method
                return(result.toString());

            }else{
                return "unsuccessfull";
            }
        } catch (IOException e) {
            e.printStackTrace();

            return "Response Error";
        } finally {
            con.disconnect();
        }

    }

    @Override
    protected void onPreExecute(){
        //super.onPreExecute();
        pdLoading = new ProgressDialog(this.context);
        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();


    }

    protected void onPostExecute(String result){
        pdLoading.dismiss();

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }

}
