package com.zhiyuan3g.managementsystemdemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**屏幕适配
 * Created by Administrator on 2015/12/2 0002.
 */
public class DisplayUtil {

    //region 获取屏幕的宽度的像素
    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    //endregion

    //region 获取屏幕高度的像素
    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return  displayMetrics.heightPixels;
    }
    //endregion

    //region 把dp单位的尺寸值转换为px单位
    public static  int dip2Px(Context context,float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale+0.5f);
    }
    //endregion

    //region 把px单位的尺寸值转换为dp单位
    public static int px2Dip(Context context,float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }
    //endregion
}
