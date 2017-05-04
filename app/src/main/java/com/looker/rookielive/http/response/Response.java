package com.looker.rookielive.http.response;

import com.looker.rookielive.http.IDontObfuscate;

/**
 * 基础返回数据
 *
 * Created by looker on 2017/5/2.
 */
public class Response<T> extends IDontObfuscate {

    public int status;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "Response [code=" + status + ", msg=" + msg + ", data=" + data + "]";
    }
}
