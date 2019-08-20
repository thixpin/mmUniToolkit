package com.htetznaing.unitoolkit.Utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.myanmartools.ZawgyiDetector;
import com.htetznaing.unitoolkit.Constants;

public class AIOmmTool {
    private static final ZawgyiDetector detector = new ZawgyiDetector();

    public static String zawgyi2Unicode(String input){
        return Rabbit.zg2uni(input);
    }

    public static String unicode2Zawgyi(String input){
        return Rabbit.uni2zg(input);
    }

    public static String getUnicode(String input,boolean force){
        if (force){
            return zawgyi2Unicode(input);
        }
        if (input!=null) {
            double score = detector.getZawgyiProbability(input);
            if (score < 0.999) {
                return zawgyi2Unicode(unicode2Zawgyi(input));
            } else
                return Rabbit.zg2uni(input);
        }
        return input;
    }

    public static String getZawgyi(String input){
        double score = detector.getZawgyiProbability(input);
        if (score < 0.999){
            return Rabbit.uni2zg(input);
        }else
        return input;
    }

    public static boolean isUnicode(Context context){
        TextView textView = new TextView(context, null);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setText("\u1000");
        textView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int length1 = textView.getMeasuredWidth();

        textView.setText("\u1000\u1039\u1000");
        textView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int length2 = textView.getMeasuredWidth();
        return length1 == length2;
    }
}
