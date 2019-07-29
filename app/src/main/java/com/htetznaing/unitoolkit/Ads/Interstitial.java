package com.htetznaing.unitoolkit.Ads;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.htetznaing.unitoolkit.Constants;

public class Interstitial {
    private InterstitialAd mInterstitialAd;
    private Activity context;
    private AdRequest adRequest;
    public Interstitial(Activity context) {
        this.context=context;
        init();
    }

    public void init(){
        if (context!=null) {
            adRequest = Constants.getAdBuilder();
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(Constants.getInterstitial());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    loadAds();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    loadAds();
                }
            });
            mInterstitialAd.loadAd(adRequest);
        }
    }

    private void loadAds() {
        if(!mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public boolean isLoaded() {
        if (mInterstitialAd.isLoaded()) {
            return true;
        } else {
            loadAds();
        }
        return false;
    }

    public void show() {
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else loadAds();
    }

}