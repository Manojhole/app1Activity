package com.example.fayaz.graphicalpassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class SetGraphicalPasswordActivity extends AppCompatActivity {

    ImageView Img1,Img2,Img3,Img4,ImgBack;
    Spinner spnTypes;
    GridView GridPassword;
    ImageAdapter imageAdapter;
    Integer start = 0;
    String Selected,result,strMobile,strPassword,strHint,strPType;
    Button btnSave;
    EditText etHint;
    SharedPrefHandler sharedPrefHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_graphical_password);
        sharedPrefHandler = new SharedPrefHandler(this);
        strMobile = sharedPrefHandler.getSharedPreferences("mno");
        btnSave = findViewById(R.id.btnSetPass);
        etHint = findViewById(R.id.asp_et_hint);

        spnTypes = (Spinner) findViewById(R.id.sp_cat);
        GridPassword = (GridView) findViewById(R.id.gridPassword);

        Img1 = (ImageView)findViewById(R.id.img1);
        Img2 = (ImageView)findViewById(R.id.img2);
        Img3 = (ImageView)findViewById(R.id.img3);
        Img4 = (ImageView)findViewById(R.id.img4);
        Img1.setTag("1");
        Img2.setTag("1");
        Img3.setTag("1");
        Img4.setTag("1");
        ImgBack = (ImageView)findViewById(R.id.imgDel);

        imageAdapter  = new ImageAdapter(this,spnTypes.getSelectedItem().toString());
        GridPassword.setAdapter(imageAdapter);

        spnTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClearImages(4);
                imageAdapter  = new ImageAdapter(getApplicationContext(),spnTypes.getSelectedItem().toString());
                GridPassword.setAdapter(imageAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GridPassword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                   // if (AppLockerPreference.getInstance(SetGraphicalPasswordActivity.this).isVibrate()) {
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);
                   // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Selected.contains(""+position))
                {
                    Toast.makeText(SetGraphicalPasswordActivity.this, "Same selection not allowed.", Toast.LENGTH_SHORT).show();
                }
                else {
                    SetImages(imageAdapter.getSelectedItem(position), position);
                }
            }
        });

        ImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImages(1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Img4.getTag() == "2") {
                    if (etHint.getText().length() > 0)
                    {
                        strPassword = Selected;
                        strHint = etHint.getText().toString();
                        strPType = spnTypes.getSelectedItem().toString();
                        new UpdatePassword().execute();
                    }
                    else
                        Toast.makeText(SetGraphicalPasswordActivity.this, "Enter Hint for your password.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SetGraphicalPasswordActivity.this, "Set Password first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void SetImages(Integer ResourceId,int position)
    {

        if (Img1.getTag().equals("1")) {
            Img1.setImageResource(ResourceId);
            Img1.setTag("2");
            Selected += ""+position;
        } else if (Img2.getTag().equals("1")) {
            Img2.setImageResource(ResourceId);
            Img2.setTag("2");
            Selected += ""+position;
        } else if (Img3.getTag().equals("1")) {
            Img3.setImageResource(ResourceId);
            Img3.setTag("2");
            Selected += ""+position;
        } else if (Img4.getTag().equals("1")) {
            Img4.setImageResource(ResourceId);
            Img4.setTag("2");
            Selected += ""+position;
        }
    }
    public void ClearImages(int count)
    {
        switch (count)
        {
            case 4:
                Img1.setImageResource(R.drawable.ic_help_outline_black_24dp);
                Img2.setImageResource(R.drawable.ic_help_outline_black_24dp);
                Img3.setImageResource(R.drawable.ic_help_outline_black_24dp);
                Img4.setImageResource(R.drawable.ic_help_outline_black_24dp);
                Img1.setTag("1");
                Img2.setTag("1");
                Img3.setTag("1");
                Img4.setTag("1");
                Selected = "";
                break;
            case 1:
                if (Img4.getTag().equals("2")) {
                    Img4.setImageResource(R.drawable.ic_help_outline_black_24dp);
                    Img4.setTag("1");
                    Selected = Selected.substring(1,3);
                } else if (Img3.getTag().equals("2")) {
                    Img3.setImageResource(R.drawable.ic_help_outline_black_24dp);
                    Img3.setTag("1");
                    Selected = Selected.substring(1,2);
                } else if (Img2.getTag().equals("2")) {
                    Img2.setImageResource(R.drawable.ic_help_outline_black_24dp);
                    Img2.setTag("1");
                    Selected = Selected.substring(1,1);
                } else if (Img1.getTag().equals("2")) {
                    Img1.setImageResource(R.drawable.ic_help_outline_black_24dp);
                    Img1.setTag("1");
                    Selected = "";
                }
        }
    }
    private class UpdatePassword extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(SetGraphicalPasswordActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Updating Graphical Password...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    UpdatePassword.this.cancel(true);
                }
            });
        }

        public void postData(String strMobile,String strGPassword,String strHint,String strPType) {


            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            Settings settings=new Settings(getApplicationContext());
            HttpPost httppost = new HttpPost(settings.IP+"update_grp_password.php");
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("f1", strMobile));
                nameValuePairs.add(new BasicNameValuePair("f2", strGPassword));
                nameValuePairs.add(new BasicNameValuePair("f3", strHint));
                nameValuePairs.add(new BasicNameValuePair("f4", strPType));

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
                final String strM=e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Something went wrong.Please try again."+strM, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {
            postData(strMobile,strPassword,strHint,strPType);
            this.progressDialog.dismiss();
            return null;

        }

        private void showToast(final String res) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //stuff that updates ui
                    if (res.equals("1")) {
                        Toast.makeText(getApplicationContext(), " Successfully Updated.", Toast.LENGTH_SHORT).show();

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
