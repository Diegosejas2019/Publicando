package com.code.publicando.publicando;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("idUser", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("idUser","");
        return usename;
    }
}
