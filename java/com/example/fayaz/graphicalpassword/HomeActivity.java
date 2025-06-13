package com.example.fayaz.graphicalpassword;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {

    LinearLayout lineMyDetails,lineExit,lineSetPass,lineApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lineApps = (LinearLayout) findViewById(R.id.ah_app);
        lineMyDetails = (LinearLayout) findViewById(R.id.ah_myd);
        lineSetPass = (LinearLayout) findViewById(R.id.ah_mySett);
        lineExit = (LinearLayout) findViewById(R.id.ah_close);

        lineApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AppListActivity.class));
            }
        });

        lineMyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });

        lineSetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SetGraphicalPasswordActivity.class));
            }
        });

        lineExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}