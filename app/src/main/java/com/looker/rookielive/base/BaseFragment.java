package com.looker.rookielive.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by looker on 2017/5/2.
 */

public abstract class BaseFragment extends Fragment {

    /***
     * 图片加载
     */
    protected View mRootView;
    protected Intent mBundleIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() != 0){
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        initView(mRootView);
        initData();
        setUserVisibleHint(true);
        setListener();
        return mRootView;
    }

    /***
     * fragment的隐藏监听
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            mBundleIntent = null;
        }
        super.onHiddenChanged(hidden);
    }

    /***
     * 切换fragment的时候初始化数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /***
     * 获取布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /***
     * 初始化界面
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /***
     * 初始化数据
     */
    protected abstract void initData();

    /***
     * 设置监听
     */
    protected abstract void setListener();

    /***
     *
     *
     * @param activity
     */
    public void initActionBar(Activity activity){}

    public void setBundleIntent(Intent bundleIntent) {
        mBundleIntent = bundleIntent;
    }

    /***
     * 实例化控件
     *
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T obtainView(int resId){
        return (T) mRootView.findViewById(resId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /***
     * 按返回键时候的炒作
     */
    public void onBackPressed(){}

}
