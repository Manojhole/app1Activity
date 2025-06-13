package com.example.fayaz.graphicalpassword;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class ForgotPActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText etMobile;
    String strMobile,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_p);

        btnSubmit = (Button) findViewById(R.id.afp_et_btnsubmit);

        etMobile = (EditText) findViewById(R.id.afp_et_mobile);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMobile = etMobile.getText().toString();
                if (strMobile.length() > 0)
                {
                    //Toast.makeText(ForgotPActivity.this, "Your password is sent to your mobile number.", Toast.LENGTH_SHORT).show();
                    new MyAsyncTask().execute();
                }
                else
                {
                    Toast.makeText(ForgotPActivity.this, "Enter Mobile Number here.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(ForgotPActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Checking your Mobile no for password...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask.this.cancel(true);
                }
            });
        }

        public void postData(String strUsn) {


            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"forgot_password.php");
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("f1", strUsn));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("nameValuePairs", "" + nameValuePairs);
                HttpResponse response = httpclient.execute(httppost);
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
                        String  name = jsonObject.getString("name");
                        String mobile  = jsonObject.getString("phone");
                        String password  = jsonObject.getString("password");

                        sendPasswordSms(name,mobile,password);
                        Log.d("name -> ", "" + name);

                    }
                }


            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Mobile no exist. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }




        public void sendPasswordSms( final  String name,final String mobile,final String password)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//
                    String textMessage="Hi "+name+"! Your password is "+password+" to login in app.";
                    Toast.makeText(ForgotPActivity.this, textMessage, Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        protected String doInBackground(String... params) {
            postData(strMobile);
            //start loading proggress dialog
            //pd= ProgressDialog.show(Chg_Password.this, "Loading...","");
            this.progressDialog.dismiss();
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dissmiss
            //pd.dismiss();
            //  adp.notifyDataSetChanged();
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
