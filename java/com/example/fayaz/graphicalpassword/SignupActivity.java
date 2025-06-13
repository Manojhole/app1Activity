package com.example.fayaz.graphicalpassword;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignupActivity extends AppCompatActivity {
    ArrayList<String> sub_list = new ArrayList<String>();
    //    ListView lv_til;
    String result;
    ArrayAdapter<String> adp;
    //Spinner spinnerCity;
    String strName, strMobileNo,   strPassword;
    Button btnRegister;
    EditText etName,etMobile,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etMobile = (EditText)findViewById(R.id.ar_et_mobile);
        etName = (EditText)findViewById(R.id.ar_et_name);
        etPassword = (EditText)findViewById(R.id.ar_et_password);

        btnRegister=(Button)findViewById(R.id.ar_btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = etName.getText().toString();
                // strCity = spinnerCity.getSelectedItem().toString();
                strMobileNo = etMobile.getText().toString();
                strPassword = etPassword.getText().toString();
                if (strName.length() > 0 && strPassword.length() > 0 && strMobileNo.length() > 0 )//&& strCity.length() > 0)
                {
                    new RegisterUsers().execute();
                }
                else {
                    Toast.makeText(SignupActivity.this, "Enter all details to register.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class RegisterUsers extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Registration in progress...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    RegisterUsers.this.cancel(true);
                }
            });
        }

        public void postData( String strName,String strMobileNo,  String strPassword) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"register.php");

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("f1", strName));
                nameValuePairs.add(new BasicNameValuePair("f2", strMobileNo));
                nameValuePairs.add(new BasicNameValuePair("f3", strPassword));

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
                    Log.d("respo", "" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    Log.d("status", "" + status);
                    // String message = jsonObject.getString("message");


                    if (status.equals("1")) {


                        Intent intent = new Intent(getApplication(), LoginActivity.class);
                        startActivity(intent);
                        showToast(status);
                        finish();
                    } else {
                        showToast(status);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            postData(strName, strMobileNo , strPassword);
            return null;
        }
    }

    private void showToast(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                if (res.equals("1")) {
                    Toast.makeText(getApplicationContext(), "  Register Successful ", Toast.LENGTH_SHORT).show();

                }
                else if (res.equals("3")) {
                    Toast.makeText(getApplicationContext(), "  Failed : Only one user registration is allowed ", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(), "Register Failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
