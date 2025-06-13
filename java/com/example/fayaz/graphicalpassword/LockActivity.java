package com.example.fayaz.graphicalpassword;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class LockActivity extends AppCompatActivity {

    ImageView Img1,Img2,Img3,Img4,ImgBack;

    GridView GridPassword;
    LockImageAdapter imageAdapter;

    String Selected;

    boolean galleryFlag = false;
    ArrayList<String> pathList = new ArrayList<String>();
    String gpws,hint,ptype;
    TextView tvHint;
    Button btnCheck;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        GridPassword = (GridView) findViewById(R.id.gridPassword);
        Selected="";
        Img1 = (ImageView)findViewById(R.id.img1);
        Img2 = (ImageView)findViewById(R.id.img2);
        Img3 = (ImageView)findViewById(R.id.img3);
        Img4 = (ImageView)findViewById(R.id.img4);
        Img1.setTag("1");
        Img2.setTag("1");
        Img3.setTag("1");
        Img4.setTag("1");
        ImgBack = (ImageView)findViewById(R.id.imgDel);

        tvHint = findViewById(R.id.asp_tv_hint);

        btnCheck = findViewById(R.id.btnSetUnlock);

        Bundle LastIntent = getIntent().getExtras();
        if (LastIntent != null)
        {
            gpws = LastIntent.getString("gpws");
            hint = LastIntent.getString("hint");
            ptype = LastIntent.getString("ptype");
        }
        tvHint.setText(hint);
        imageAdapter  = new LockImageAdapter(this,ptype);
        GridPassword.setAdapter(imageAdapter);

        GridPassword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                   //if (AppLockerPreference.getInstance(LockActivity.this).isVibrate()) {
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    SetImages(imageAdapter.getSelectedItem(position), position);
            }
        });

        ImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImages(1);
            }
        });
       // Log.d("test", "GalleryFlagL: " + galleryFlag);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Selected.equals(gpws))
                {
                    Intent intent = new Intent(getApplication(), App3Activity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(LockActivity.this, "Your have entered wrong password.", Toast.LENGTH_SHORT).show();
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


}
