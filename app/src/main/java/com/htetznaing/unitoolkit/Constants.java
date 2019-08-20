package com.htetznaing.unitoolkit;

import android.util.Base64;

public class Constants {
    public static boolean CHANGE_HIDDEN=false;
    public static String CONVERT_TO_KEY = "convert";
    public static String CHANGE_HIDDEN_KEY = "change_hidden";

    public static void setChangeHidden(boolean changeHidden) {
        CHANGE_HIDDEN = changeHidden;
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
