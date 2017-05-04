package com.looker.rookielive.http.request;

import com.google.gson.Gson;
import com.looker.rookielive.http.IDontObfuscate;

import java.lang.reflect.Type;

/**
 * 网络请求基本类
 *
 * Created by looker on 2017/5/2.
 */
public abstract class IRequest extends IDontObfuscate{

    private boolean DEBUG = false;

    public static final String HOST_DEBUG = "http://192.168.31.92:8094/Api/";
    public static final String HOST_PUBLIC = "http://liveteach.zdapk.cn/Api/";

    protected RequestParams mParams = new RequestParams();
    public int mRequestId = 0;
    protected int mDraw = 0;
    protected final static Gson mGson = new Gson();

    public IRequest(){

    }

    /***
     * 接口请求参数
     *
     * @return
     */
    public RequestParams getParams(){
        return mParams;
    }

    /***
     * http直接post数据
     *
     * @return
     */
    public String getPostData(){
        return null;
    }

    /***
     * 设置接口请求唯一标识
     *
     * @param requestId
     */
    public void setRequestId(int requestId){
        mRequestId = requestId;
    }

    /***
     * 返货接口请求唯一标识
     *
     * @return
     */
    public int getRequestId(){
        return mRequestId;
    }

    /***
     * 获取当前接口的Url地址
     *
     * @return
     */
    public abstract String getUrl();

    /***
     * 获取解析类型
     *
     * @return
     */
    public abstract Type getParserType();

    /***
     * 返回服务起接口地址
     *
     * @return
     */
    protected String getHost(){
        return DEBUG ? HOST_DEBUG : HOST_PUBLIC;
    }

    @Override
    public String toString() {
        return "IRequest [DEBUG=" + DEBUG
                + ", mParams=" + mParams + ", mRequestId=" + mRequestId
                + ", mDraw=" + mDraw +"]";
    }
}
