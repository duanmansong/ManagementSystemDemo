package com.zhiyuan3g.managementsystemdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyuan3g.managementsystemdemo.R;
import com.zhiyuan3g.managementsystemdemo.utils.DepthPageTransforme;
import com.zhiyuan3g.managementsystemdemo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class IntroduceActivity extends AppCompatActivity {

    LinearLayout linear;
    ViewPager viewPager;
    List<View> listView = null;
    int oldItem = 0;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        linear = (LinearLayout) findViewById(R.id.linear);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        View view1 = LayoutInflater.from(this).inflate(R.layout.layout1,null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.layout2,null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.layout3,null);
        Button button = (Button) view3.findViewById(R.id.btn);//对第三个布局中的按钮实例化
        listView = new ArrayList<>();
        //将布局添加到ListView集合中
        listView.add(view1);
        listView.add(view2);
        listView.add(view3);
        //将布局通过适配器和ViewPager绑定，显示
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return listView.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(listView.get(position));
                return listView.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(listView.get(position));
            }
        });

        viewPager.setCurrentItem(0);
        showPoint();
        viewPager.setOnPageChangeListener(pageChangeListener);
        viewPager.setPageTransformer(true, new DepthPageTransforme());//为ViewPager添加动画效果
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮后 将是否是第一次进入设置成false
                preferences = getSharedPreferences("dan",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirst",false);
                editor.commit();
                Intent intent = new Intent(IntroduceActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //显示点的位置
    public void showPoint(){
        for (int i=0;i<3;i++){
            if (i==0) {
                //第一次进来最左边的点为实心点
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.point);
                linear.addView(imageView);
            }else {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.select);
                DisplayUtil displayUtil = new DisplayUtil();
                int left = displayUtil.dip2Px(this,16);
                imageView.setPadding(left,0,0,0);
                linear.addView(imageView);
            }
        }
    }
    //为ViewPager添加监听事件
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            ImageView perPoint = (ImageView) linear.getChildAt(oldItem);
            perPoint.setImageResource(R.drawable.select);//将划过去的视图中的点设置成空心的
            ImageView currentPoint = (ImageView) linear.getChildAt(i);
            currentPoint.setImageResource(R.drawable.point);//将当前的视图中的点设置成实心的点
            oldItem = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
