package com.htetznaing.unitoolkit;

import android.util.Base64;

import com.google.android.gms.ads.AdRequest;

public class Constants {
    public static String CHANGING="unicode";
    public static boolean CHANGE_HIDDEN=false;
    public static String CONVERT_TO_KEY = "convert";
    public static String CHANGE_HIDDEN_KEY = "change_hidden";

    public static void setCHANGING(String CHANGING) {
        Constants.CHANGING = CHANGING;
    }

    public static void setChangeHidden(boolean changeHidden) {
        CHANGE_HIDDEN = changeHidden;
    }


    public static AdRequest getAdBuilder(){
        return new AdRequest.Builder().addTestDevice("97519ED2263009D76D7691B456383B6C").build();
    }

    static {
        System.loadLibrary("native-lib");
    }

    public static String getBanner(){
        return decodeString(getAdmobBanner());
    }

    public static String getInterstitial(){
        return decodeString(getAdmobInterstitial());
    }

    private static String decodeString(String encodedString) {
        byte[] decodeValue = Base64.decode(encodedString, Base64.DEFAULT);
        return new String(decodeValue);
    }

    private static native String getAdmobBanner();

    private static native String getAdmobInterstitial();
}
