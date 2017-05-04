package com.looker.rookielive.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/***
 * 界面抽取，包括标题，网络，图片加载等
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Handler mHandler = new Handler();

    /***
     * 图片加载
     */
    public ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBeforeLayout();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        initView();
        initData();
        setListener();
    }

    protected void setBeforeLayout() {
    }

    /***
     * 返回当前界面布局
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /***
     * 初始化所有的view
     */
    protected abstract void initView();

    /***
     * 初始化所有的数据
     */
    protected abstract void initData();

    /***
     * 设置所有事件监听
     */
    protected abstract void setListener();

    @Override
    protected void onResume() {
        super.onResume();
    }

    /***
     * 获取控件
     *
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T obtainView(int resId) {
        return (T) findViewById(resId);
    }

    /***
     * 显示toast
     *
     * @param resId
     */
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    /***
     * 显示toast， 便于控制toast的显示与关闭
     *
     * @param resStr
     * @return
     */
    public Toast showToast(final String resStr) {

        if (TextUtils.isEmpty(resStr)) {
            return null;
        }

        final Toast[] toast = {null};

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                toast[0] = Toast.makeText(BaseActivity.this, resStr,
                        Toast.LENGTH_SHORT);
                toast[0].show();
            }
        });
        return toast[0];
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


