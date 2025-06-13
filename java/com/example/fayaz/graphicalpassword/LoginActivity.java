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
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvForgot,tvNewUser;
    EditText etMobile,etPasword;
    String strMobile,strPassword;
    String result;

    SharedPrefHandler sharedPrefHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPrefHandler = new SharedPrefHandler(this);

        etMobile = (EditText) findViewById(R.id.al_et_mobile);
        etPasword = (EditText) findViewById(R.id.al_et_password);

        btnLogin = (Button) findViewById(R.id.al_btn_login);

        tvForgot = (TextView) findViewById(R.id.al_tv_fpassword);
        tvNewUser = (TextView) findViewById(R.id.al_tv_newuser);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMobile = etMobile.getText().toString();
                strPassword = etPasword.getText().toString();
                if (strMobile.length() > 0 && strPassword.length() > 0)
                {
                    //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    new Login().execute();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Enter Mobile No and Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPActivity.class));
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
            }
        });
    }
    private class Login extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        protected void onPreExecute() {
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Login in progress...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    Login.this.cancel(true);
                }
            });
        }

        public void postData(String strMobile, String strPassword) {
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings = new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"login.php");
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("f1", strMobile));
                nameValuePairs.add(new BasicNameValuePair("f2", strPassword));
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
                    if(status .equals("1")) {showToast(status);
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplication(), HomeActivity.class);
                        sharedPrefHandler.setSharedPreferences("mno",strMobile);
                        sharedPrefHandler.setSharedPreferences("spass",strPassword);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {

                        progressDialog.dismiss(); showToast(status);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            postData(strMobile, strPassword);
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
    private void showToast(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //stuff that updates ui
                if (res.equals("1")) {
                    Toast.makeText(getApplicationContext(), "  Login Successful ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
