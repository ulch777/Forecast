package com.example.yulia.forecast.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.example.yulia.forecast.communication.MyVolley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Program extends Application {
    private static final String TAG = "Program";
    private static Program instance;

    public static Program getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        instance = this;
        MyVolley.init(this);

//        getHashKey();
//        if(Looper.myLooper() == Looper.getMainLooper()) {
//            AppLogger.LogCut(TAG, "Main Thread");
//        }else AppLogger.LogCut(TAG, "NOT Main Thread");
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) instance.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
        } else {
            //noinspection deprecation
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED)
                        return true;
            }
        }
        // not connected to the internet
        return false;
    }

    public String getHashKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e(TAG, "hash key: " + something);
                return something;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "name not found: " + e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "no such an algorithm: " + e.toString());
        } catch (Exception e) {
            Log.e(TAG, "exception: " + e.toString());
        }
        return "";
    }
}
