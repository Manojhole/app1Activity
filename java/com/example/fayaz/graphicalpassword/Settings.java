package com.example.fayaz.graphicalpassword;

import android.content.Context;

public class Settings {


    SharedPrefHandler sharedPrefHandler;
    public String IP = "https://sgcprojects123.000webhostapp.com/graphicalpassword/api/";
    public Settings(Context cntx)
    {

        sharedPrefHandler = new SharedPrefHandler(cntx);
        if (sharedPrefHandler.getSharedPreferences("host") != "NF")
        {
            if(sharedPrefHandler.getSharedPreferences("host") != "")
            {
                //IP = "http://"+sharedPrefHandler.getSharedPreferences("host")+"/graphicalpassword/apis/";
                IP = "https://sgcprojects123.000webhostapp.com/graphicalpassword/api/";
            }
        }
        else
        {
            IP = "https://sgcprojects123.000webhostapp.com/graphicalpassword/api/";
        }
    }
}
