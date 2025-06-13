package com.example.fayaz.graphicalpassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class AppAdapter extends ArrayAdapter {
    List list=new ArrayList();
    Bitmap bitmap;
    Settings settings;
    Context cActivity;
    String result;
    public AppAdapter(Context context, int resource) {
        super(context, resource);
        settings=new Settings(context);
        cActivity = context;
    }

    public void add(AppItem object) {
        list.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        AppHolder appHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.my_view,parent,false);
            appHolder = new AppHolder();
            appHolder.tvAppName = (TextView) row.findViewById(R.id.App_name);
            appHolder.imgIcon = (ImageView) row.findViewById(R.id.App_icon);
            appHolder.aSwitch = (Switch) row.findViewById(R.id.App_switch);
            row.setTag(appHolder);
        }
        else
        {
            appHolder = (AppHolder) row.getTag();
        }
        final AppItem appItem = (AppItem) getItem(position);
        appHolder.tvAppName.setText(appItem.getAppname());
        new getImageFromString(appHolder.imgIcon).execute(appItem.getIcon());
        if (appItem.getLocked().equals("1"))
        {
            appHolder.aSwitch.setChecked(true);
        }
        else
        {
            appHolder.aSwitch.setChecked(false);
        }
        appHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String[] strPara= new String[2];
                    strPara[0] = appItem.getId();
                    strPara[1] = ((isChecked)?"1":"0");
                    new UpdateLockStatus().execute(strPara);
            }
        });
        return row;
    }

static class AppHolder
{
    TextView tvAppName;
    ImageView imgIcon;
    Switch aSwitch;
}
public class getImageFromString extends AsyncTask<String,Void,Integer> {

    ImageView imageView;

    public getImageFromString(ImageView imgView) {
        this.imageView = imgView;
    }

    @Override
    protected Integer doInBackground(String... strIcon) {
        Integer resourceId = R.drawable.flower1;
        if (strIcon[0].contains("flower8"))
        {
            return R.drawable.flower8;
        }
        else if (strIcon[0].contains("flower3"))
        {
            return R.drawable.flower3;
        }
        else if (strIcon[0].contains("flower1"))
        {
            return R.drawable.flower1;
        }
        else
            return resourceId;
    }

    @Override
    protected void onPostExecute(Integer resourseId) {
        super.onPostExecute(resourseId);
        imageView.setImageResource(resourseId);
    }
}
    private class UpdateLockStatus extends AsyncTask<String[], Void, String> {
       // private ProgressDialog progressDialog = new ProgressDialog(cActivity);

        public void postData(String strId,String strLockStatus) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(settings.IP+"lock_unlock_app.php");
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("f1", strId));
                nameValuePairs.add(new BasicNameValuePair("f2", strLockStatus));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

                }
            } catch (Exception e) {
               // Log.d("status", "" + status);
            }
        }

        @Override
        protected String doInBackground(String[]... params) {

            postData(params[0][0],params[0][1]);
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

        }

        result = sb.toString().trim();


        result = result.substring(1, result.length() - 1);

        if (!result.trim().equals("Error")) {
            String[] r = result.split("-");


        }
        return sb.toString();

    }
}
