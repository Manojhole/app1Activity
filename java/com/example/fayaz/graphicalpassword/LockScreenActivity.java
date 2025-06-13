package com.example.fayaz.graphicalpassword;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LockScreenActivity extends AppCompatActivity {
    SharedPrefHandler sharedPrefHandler;
    Button btnSave;
    EditText etHost;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        Settings settings = new Settings(getApplicationContext());
        Toast.makeText(this, ""+settings.IP, Toast.LENGTH_SHORT).show();
        Thread my_thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                    //Intent i = new Intent(getApplicationContext(),App1Activity.class);
                    //startActivity(i);
                    new CheckLock().execute();
                    //finish();
                } catch (Exception ex)
                {}
            }
        };

        my_thread.start();



        btnSave = (Button) findViewById(R.id.abtnSubmit);
        sharedPrefHandler = new SharedPrefHandler(this);
        etHost = (EditText) findViewById(R.id.aetHost);
        if (sharedPrefHandler.getSharedPreferences("host") != "NF")
        {
            if(sharedPrefHandler.getSharedPreferences("host") != "")
            {
                etHost.setText(sharedPrefHandler.getSharedPreferences("host"));
            }

        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etHost.getText().length() > 0)
                {
                    sharedPrefHandler.setSharedPreferences("host",etHost.getText().toString());
                    //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    //finish();
                    new CheckLock().execute();
                }
                else
                {
                    Toast.makeText(LockScreenActivity.this, "Enter Host", Toast.LENGTH_SHORT).show();
                }
            }
        }); btnSave.setVisibility(View.GONE);
        etHost.setVisibility(View.GONE);
    }
    private class CheckLock extends AsyncTask<String, Void, String> {


        public void postData(String strAppID) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"check_lock.php");

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("f1", strAppID));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("nameValuePairs", "" + nameValuePairs);
                HttpResponse response;
                response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    String result;
                    result = convertStreamToString(instream);
                    Log.d("aaa","" + result);

                    JSONArray arr = new JSONArray(result);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObject = arr.getJSONObject(i);
                        String  locked = jsonObject.getString("locked");
                        String  gpws = jsonObject.getString("gpws");
                        String  hint = jsonObject.getString("hint");
                        String  ptype = jsonObject.getString("ptype");
                        setdata(locked,gpws,hint,ptype);
                        Log.d("name -> ", "" + locked);
                        // lst_tit.add(status);
                        //jsonObject.getString("name");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        public void setdata(final String locked,final String gpws,final String hint,final String ptype)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (locked.equals("1"))
                    {
                        Intent i = new Intent(getApplicationContext(),LockActivity.class);
                        i.putExtra("gpws",gpws);
                        i.putExtra("hint",hint);
                        i.putExtra("ptype",ptype);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),App3Activity.class));
                        finish();
                    }
                }
            });

        }
        @Override
        protected String doInBackground(String... params) {
            postData("3");
            return null;
        }
    }
    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception ex) {
            Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        result = sb.toString().trim();


        result = result.substring(1, result.length() - 1);

        if (!result.trim().equals("Error")) {
            String[] r = result.split("-");


        } else
            Toast.makeText(getApplication(), "", Toast.LENGTH_LONG).show();
        return sb.toString();

    }
}
