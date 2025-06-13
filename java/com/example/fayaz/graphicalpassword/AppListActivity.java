package com.example.fayaz.graphicalpassword;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.os.AsyncTask;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


public class AppListActivity extends AppCompatActivity {
    ListView listViewNews;
    String result;
    SharedPrefHandler sharedPrefHandler;
    AppAdapter appAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        listViewNews = (ListView) findViewById(R.id.aln_lst_apps);
        sharedPrefHandler = new SharedPrefHandler(this);
        new GetAppsList().execute();
    }
    private class GetAppsList extends AsyncTask<String, AppItem, String> {
        private ProgressDialog progressDialog = new ProgressDialog(AppListActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Loading Apps...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    GetAppsList.this.cancel(true);
                }
            });
        }

        public void postData() {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"select_all_apps.php");

            appAdapter = new AppAdapter(getApplicationContext(), R.layout.my_view);
            try {

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    String result;
                    result = convertStreamToString(instream);


                    JSONArray arr = new JSONArray(result);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObject = arr.getJSONObject(i);
                        String appId = jsonObject.getString("id");
                        String appName = jsonObject.getString("appname");
                        String appLockStatus = jsonObject.getString("locked");
                        String appIcon = jsonObject.getString("icon");

                        Log.d("AppName -> ", "" + appName);

                        AppItem appItem = new AppItem(appId,appIcon,appName,appLockStatus);
                        publishProgress(appItem);
                    }
                } else {
                    response = null;
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No Data.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {
            postData();
            //start loading proggress dialog
            //pd= ProgressDialog.show(Chg_Password.this, "Loading...","");
            this.progressDialog.dismiss();
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listViewNews.setAdapter(appAdapter);
        }

        @Override
        protected void onProgressUpdate(AppItem... values) {
            appAdapter.add(values[0]);
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

        private void showToast(final String res) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //stuff that updates ui
                    if (res.equals("1")) {
                        Toast.makeText(getApplicationContext(), " Successfull", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), " Failed ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
