package com.app.smartbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.app.smartbj.utils.PrefUtils;

public class SplashActivity extends Activity {
    RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splash= (RelativeLayout) findViewById(R.id.rl_splash);

        //旋转动画
        RotateAnimation rotateAnim=new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnim.setDuration(2000);//动画时间
        rotateAnim.setFillAfter(true);//保持动画结束状态

        //缩放动画
        ScaleAnimation scaleAnim=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnim.setDuration(2000);
        scaleAnim.setFillAfter(true);

        //渐变动画
        AlphaAnimation alphaAnim=new AlphaAnimation(0,1);
        alphaAnim.setDuration(2000);
        alphaAnim.setFillAfter(true);

        //创建动画集合
        AnimationSet set=new AnimationSet(true);
        set.addAnimation(rotateAnim);
        set.addAnimation(scaleAnim);
        set.addAnimation(alphaAnim);

        //启动动画
        rl_splash.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束，跳转页面
                //第一次进入，则跳转新手指导，否则跳转到主页面
                boolean isFirstEnter= PrefUtils.getBoolean(SplashActivity.this,"is_first_enter",true);
                Intent intent;
                if(isFirstEnter){
                    // 新手引导界面
                    intent=new Intent(SplashActivity.this,GuideActivity.class);
                }else{
                    //主界面
                    intent=new Intent(SplashActivity.this,MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
