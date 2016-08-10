package com.zhiyuan3g.managementsystemdemo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.zhiyuan3g.managementsystemdemo.R;
import com.zhiyuan3g.managementsystemdemo.utils.ActivityCollector;

public class FlashActivity extends AppCompatActivity {

    RelativeLayout rel;
    SharedPreferences preferences;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ActivityCollector.addActivity(this);
        rel = (RelativeLayout) findViewById(R.id.rel);
//        PropertyValuesHolder pro1 = PropertyValuesHolder.ofFloat("alpha",1.0f,0.0f);
//        PropertyValuesHolder proX = PropertyValuesHolder.ofFloat("scaleX",1.0f,0.0f);
//        PropertyValuesHolder proY = PropertyValuesHolder.ofFloat("scaleY",1.0f,0.0f);
//        ObjectAnimator.ofPropertyValuesHolder(rel,pro1,proX,proY).setDuration(3000).start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(rel,"alpha",1.0f,0.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(rel,"scaleX",1.0f,0.0f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(rel,"scaleY",1.0f,0.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator1).with(animator2).with(animator3);
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(3000);
        set.start();
        //线程休眠2800毫秒
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    getShowHistory();
                }
            }
        }.start();
    }

    //判断是否是第一次进入
    public void getShowHistory(){
        preferences = getSharedPreferences("dan",MODE_PRIVATE);
        boolean result = preferences.getBoolean("isFirst",true);
        if (result){
            //如果是第一次进入则跳转到介绍界面
            intent = new Intent(FlashActivity.this,IntroduceActivity.class);
            startActivity(intent);
            finish();
        }else {
            //如果不是第一次进入则跳转到登录界面
            intent = new Intent(FlashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
