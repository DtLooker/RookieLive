package com.looker.rookielive.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * 列表的adapter封装，包括viewholder item显示，数据加载
 */
public abstract class BaseAdapter<T> extends ArrayAdapter<T> {

    public BaseAdapter(Context context, int resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        T data = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(getViewLayoutId(), parent, false);
            viewHolder = new ViewHolder(getContext(), convertView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initData(viewHolder, data, position);
        return convertView;
    }

    /***
     * 获取template布局
     *
     * @return
     */
    protected abstract int getViewLayoutId();

    protected void initItemView(ViewHolder viewHolder, T data){

    }

    protected abstract void initData(ViewHolder holder, T data, int position);

    public static class ViewHolder{
        private SparseArray<View> mViews;
        private View mItemView;
        private int mLayoutId;

        public ViewHolder(Context context, View itemView) {
            mItemView = itemView;
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int viewId){
            View view = mViews.get(viewId);
            if (view == null){
                view = mItemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView(){
            return mItemView;
        }

        public int getLayoutId(){
            return mLayoutId;
        }

        /***
         * 关于事件
         */
        public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener){
            View view = getView(viewId);
            view.setOnClickListener(listener);
            return this;
        }

        public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener){
            View view = getView(viewId);
            view.setOnTouchListener(listener);
            return this;
        }

        public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener){
            View view = getView(viewId);
            view.setOnLongClickListener(listener);
            return this;
        }




    }
}
