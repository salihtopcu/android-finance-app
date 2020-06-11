package com.gelistirmen.finance;

import android.app.Application;
import android.graphics.Typeface;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gelistirmen.finance.model.membership.UserProfile;

public class MyApplication extends Application {
    public static MyApplication instance;
    public static String messageForActivity;
    public static UserProfile userProfile;

    public static boolean developmentMode = false;
    public static boolean mocking = true;

    public RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = (MyApplication)getApplicationContext();
        this.requestQueue = Volley.newRequestQueue(this);
    }

    public static class Typefaces {
        public static Typeface blenderProBold = Typeface.createFromAsset(MyApplication.instance.getAssets(), "blender_pro_bold.ttf");
        public static Typeface blenderProBook = Typeface.createFromAsset(MyApplication.instance.getAssets(), "blender_pro_book.ttf");
        public static Typeface blenderProHeavy = Typeface.createFromAsset(MyApplication.instance.getAssets(), "blender_pro_heavy.ttf");
        public static Typeface blenderProThin = Typeface.createFromAsset(MyApplication.instance.getAssets(), "blender_pro_thin.ttf");
    }
}
