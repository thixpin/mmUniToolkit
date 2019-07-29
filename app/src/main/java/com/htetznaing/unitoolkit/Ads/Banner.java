package com.htetznaing.unitoolkit.Ads;

import android.content.Context;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.htetznaing.unitoolkit.Constants;

public class Banner {
    private static AdView mAdView;
    private static LinearLayout adLayout;
    private static AdRequest adRequest;
    private static Context context;
    private static AdSize adSize;

    public Banner(Context context, final LinearLayout adsLayout,AdSize adSize){
        this.adLayout=adsLayout;
        this.context=context;
        this.adSize=adSize;
        init();
    }


    public static void init(){
        if (context!=null && adSize!=null) {
            adRequest = Constants.getAdBuilder();
            mAdView = new AdView(context);
            mAdView.setAdUnitId(Constants.getBanner());
            mAdView.setAdSize(adSize);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    mAdView.loadAd(adRequest);
                    adLayout.removeAllViews();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adLayout.removeAllViews();
                    adLayout.addView(mAdView);
                }
            });
            mAdView.loadAd(adRequest);
        }
    }
}
