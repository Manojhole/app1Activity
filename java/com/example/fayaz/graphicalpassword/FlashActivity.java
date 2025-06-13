package com.example.fayaz.graphicalpassword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FlashActivity extends AppCompatActivity {
    SharedPrefHandler sharedPrefHandler;
    Button btnSave;
    EditText etHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        Thread my_thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception ex)
                {}
            }
        };

        my_thread.start();

        btnSave = (Button) findViewById(R.id.btnSubmit);
        sharedPrefHandler = new SharedPrefHandler(this);
        etHost = (EditText) findViewById(R.id.etHost);
        btnSave.setVisibility(View.GONE);
        etHost.setVisibility(View.GONE);
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
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(FlashActivity.this, "Enter Host", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

