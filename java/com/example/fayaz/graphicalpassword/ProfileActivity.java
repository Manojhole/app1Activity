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
import android.widget.ImageView;
import android.widget.TextView;
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

public class ProfileActivity extends AppCompatActivity {
    TextView tvMobile,tvPassword,tvCard;
    EditText tvName;
    String strMobile,result,strName;
    Button btnSave;
    ImageView Img1,Img2,Img3,Img4;

    SharedPrefHandler sharedPrefHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPrefHandler = new SharedPrefHandler(this);
        strMobile = sharedPrefHandler.getSharedPreferences("mno");

        btnSave=(Button) findViewById(R.id.ap_btnsubmit);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = tvName.getText().toString();
                if (strName.length() > 0)
                {
                    new UpdateName().execute();
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Enter Name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

tvCard = findViewById(R.id.ptvMsg);
        tvMobile = (TextView)findViewById(R.id.asp_tv_mobile);
        tvName = (EditText) findViewById(R.id.asp_tv_name);

        tvPassword = (TextView)findViewById(R.id.asp_tv_password);

        new SelectProfile().execute();

        Img1 = (ImageView)findViewById(R.id.img11);
        Img2 = (ImageView)findViewById(R.id.img22);
        Img3 = (ImageView)findViewById(R.id.img33);
        Img4 = (ImageView)findViewById(R.id.img44);
    }
    private class SelectProfile extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Retrieving your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    SelectProfile.this.cancel(true);
                }
            });
        }

        public void postData(String strRegno) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"profile_details.php");

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("f1", strRegno));

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
                        String  name = jsonObject.getString("name");
                        String mobile = jsonObject.getString("mobile");
                        String password  = jsonObject.getString("password");
                        String gpw  = jsonObject.getString("gpw");
                        String hint  = jsonObject.getString("hint");
                        String ptype  = jsonObject.getString("ptype");

                        setdata(name,mobile,password,gpw,hint,ptype);
                        Log.d("name -> ", "" + name);
                        // lst_tit.add(status);
                        //jsonObject.getString("name");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }
        }
        public void setdata( final  String name,final String mobile,final String password,final String gpw,final String hint,final String ptype)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvName.setText(name);
                    tvMobile.setText(mobile);
                    tvPassword.setText("***** Click here for password");
                    tvPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProfileActivity.this, "Your password is "+password, Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (gpw.length() == 4)
                    {
                        tvCard.setVisibility(View.VISIBLE);
                        setIamges(ptype,gpw);
                        tvCard.setText("Your password hint : "+hint);
                    }
                        else {
                        tvCard.setText("Password not set");
                        tvCard.setVisibility(View.VISIBLE);
                    }
                }
            });

        }


        private void setIamges(String ptype,String gpw) {
            switch (ptype) {
                case "Flowers":
                    for (int i=0;i<4;i++)
                    {
                        if (i == 0)
                        {Img1.setImageResource(mFlowers[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 1)
                        {Img2.setImageResource(mFlowers[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 2)
                        {Img3.setImageResource(mFlowers[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 3)
                        {Img4.setImageResource(mFlowers[Integer.parseInt(gpw.substring(i,i+1))]);}
                    }

                    break;
                case "Fruits":
                    for (int i=0;i<4;i++)
                    {
                        if (i == 0)
                        {Img1.setImageResource(mFruits[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 1)
                        {Img2.setImageResource(mFruits[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 2)
                        {Img3.setImageResource(mFruits[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 3)
                        {Img4.setImageResource(mFruits[Integer.parseInt(gpw.substring(i,i+1))]);}
                    }
                    break;
                case "Smileys":
                    for (int i=0;i<4;i++)
                    {
                        if (i == 0)
                        {Img1.setImageResource(mSmileys[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 1)
                        {Img2.setImageResource(mSmileys[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 2)
                        {Img3.setImageResource(mSmileys[Integer.parseInt(gpw.substring(i,i+1))]);}
                        else if (i == 3)
                        {Img4.setImageResource(mSmileys[Integer.parseInt(gpw.substring(i,i+1))]);}
                    }
                    break;
                default:

                    break;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            postData(strMobile);
            return null;
        }
    }
    public Integer[] mFlowers =
            {R.drawable.flower1,R.drawable.flower2,R.drawable.flower3,R.drawable.flower4,R.drawable.flower5,R.drawable.flower6,R.drawable.flower7,R.drawable.flower8,R.drawable.flower9};
    public Integer[] mFruits =
            {R.drawable.fruit1,R.drawable.fruit2,R.drawable.fruit3,R.drawable.fruit4,R.drawable.fruit5,R.drawable.fruit6,R.drawable.fruit7,R.drawable.fruit8,R.drawable.fruit9};
    public Integer[] mSmileys =
            {R.drawable.smily1,R.drawable.smily2,R.drawable.smily3,R.drawable.smily4,R.drawable.smily5,R.drawable.smily6,R.drawable.smily7,R.drawable.smily8,R.drawable.smily9};
    private class UpdateName extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Updating Name...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    UpdateName.this.cancel(true);
                }
            });
        }

        public void postData(String strMobile,String strName) {


            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"udate_prof.php");
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("f1", strMobile));
                nameValuePairs.add(new BasicNameValuePair("f2", strName));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("nameValuePairs", "" + nameValuePairs);
                HttpResponse response = httpclient.execute(httppost);
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
                        progressDialog.dismiss();
                        showToast(status);
                    } else {
                        progressDialog.dismiss();
                        showToast(status);
                    }
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Something went wrong.Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {
            postData(strMobile,strName);
            this.progressDialog.dismiss();
            return null;

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
