package com.zhiyuan3g.managementsystemdemo.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2015/12/6.
 */
public class DepthPageTransforme implements ViewPager.PageTransformer {
    private static final float MIN_SCALE=0.5f;
    @Override
    public void transformPage(View view, float v) {
        int pageWidth = view.getWidth();
        if (v<-1){
            view.setAlpha(0);
        }else if (v<=0){
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        }else if (v<=1){
            view.setAlpha(1 - v);
            view.setTranslationX(pageWidth*-v);
            float scaleFactor = MIN_SCALE+(1-MIN_SCALE)*(1-Math.abs(v));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }else {
            view.setAlpha(0);
        }
    }
}
