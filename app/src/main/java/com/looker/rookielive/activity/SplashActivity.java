package com.looker.rookielive.activity;
import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.looker.rookielive.R;
import com.looker.rookielive.base.BaseActivity;


public class SplashActivity extends BaseActivity {

    private RelativeLayout rlSpash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        rlSpash = obtainView(R.id.activity_splash);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }
}
