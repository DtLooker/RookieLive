package com.looker.rookielive.http.request;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求参数封装
 *
 * Created by looker on 2017/5/2.
 */
public class RequestParams implements Serializable{

    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public final static String APPLICATION_JSON = "application/json";

    protected final static String LOG_TAG = "RequestParams";
    protected final ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, StreamWrapper> streamParams = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, FileWrapper> fileParams = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, List<FileWrapper>> fileArrayParams = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, Object> urlParamsWithObject = new ConcurrentHashMap<>();
    protected boolean isRepeatable;
    protected boolean forceMultipartEntity = false;
    protected boolean userJsonStreamer;
    protected String elapsedFielInJsonStreamer = "_elapsed";
    protected boolean autoCloseInputStream;
    protected String contentEncoding = "utf-8";

    private Gson gson = new Gson();

    /***
     * Constructs a new empty RequestParams instance
     */
    public RequestParams(){
        this((Map<String, String>) null);
    }

    /***
     * constructs a new RequestParams instance containing the key/value string
     * from the specified map
     *
     * @param source the source key/value string map to add
     */
    public RequestParams(Map<String, String> source){
        if (source != null){
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /***
     * constructs a new RequestParams instance and populate it with a single initial key/value
     * string param
     *
     * @param key
     * @param value
     */
    public RequestParams(final String key, final String value){
        this(new HashMap<String, String>(){
            {put(key, value);}
        });
    }

    /***
     Constructs a new RequestParams instance and populate it with multiple initial key/value
     * string param.
     *
     * @param keysAndValues a sequence of keys and values. Objects are automatically converted to
     *                      Strings (including the value {@code null}).
     * @throws IllegalArgumentException if the number of arguments isn't even.
     */
    public RequestParams(Object... keysAndValues){
        int len = keysAndValues.length;
            if (len % 2 != 0){
                throw new IllegalArgumentException("Supplied arguments must be even");
            }
        for (int i = 0; i < len; i += 2) {
            String key = String.valueOf(keysAndValues[i]);
            String val = String.valueOf(keysAndValues[i + 1]);
            put(key, val);
        }
    }

    public void setContentEncoding(String encoding){
        if (encoding != null){
            contentEncoding = encoding;
        }else {
            Log.d(LOG_TAG, "setContentEncoding called with null attribute");
        }
    }

    /***
     * if set to true will force Content-Type header to multipart/form-data
     * even if there are not Files or Streams to be send
     * <p>&nbsp;</p>
     * @param force boolean, should declare content-type multipart/form-data event without files or streams present
     */
    public void setForceMultipartEntityContentType(boolean force){
        forceMultipartEntity = force;
    }

    /***
     * Adds a key/value string pair to the request
     *
     * @param key
     * @param value
     */
    public void put(String key, String value){
        if (key != null && value != null){
            urlParams.put(key, value);
        }
    }

    /***
     * Adds files array to the request
     *
     * @param key the key name for the new param
     * @param files the files array to add
     * @throws FileNotFoundException if one of passed files is not
     * found at time of assembling the requestparams into request
     */
    public void put(String key, File files[]) throws FileNotFoundException{
        put(key, files, null, null);
    }

    /***
     * Adds files array to the request with both custom provided file content-type and files name
     *
     * @param key
     * @param files
     * @param contentType
     * @param customFileName
     * @throws FileNotFoundException
     */
    public void put(String key, File files[], String contentType, String customFileName) throws FileNotFoundException{
        if (key != null){
            List<FileWrapper> fileWrappers = new ArrayList<>();
            for (File file : files) {
                if (file == null || file.exists()){
                    throw new FileNotFoundException();
                }
                fileWrappers.add(new FileWrapper(file, contentType, customFileName));
            }
            fileArrayParams.put(key, fileWrappers);
        }
    }

    /***
     * Adds a file to the request
     *
     * @param key
     * @param file
     * @throws FileNotFoundException
     */
    public void put(String key, File file) throws FileNotFoundException{
        put(key, file, null, null);
    }

    /***
     * Adds a file to the request with custom provided file name
     *
     * @param key
     * @param customFileName file name to use instead of real file name
     * @param file
     * @throws FileNotFoundException
     */
    public void put(String key, String customFileName, File file) throws FileNotFoundException{
        put(key, file, null, customFileName);
    }

    /***
     * Adds a file to the request with custom provided file content-type
     *
     * @param key
     * @param file
     * @param contentType
     * @throws FileNotFoundException
     */
    public void put(String key, File file, String contentType) throws FileNotFoundException{
        put(key, file, contentType, null);
    }

    /**
     * Adds a file to thr request with both custom provided file content-type and file name
     *
     * @param key
     * @param file
     * @param contentType
     * @param customFileName
     */
    public void put(String key, File file, String contentType, String customFileName) throws FileNotFoundException{
        if (file == null || !file.exists()){
            throw new FileNotFoundException();
        }
        if (key != null){
            fileParams.put(key, new FileWrapper(file, contentType, customFileName));
        }
    }

    /***
     * Adds on input stream to the request
     *
     * @param key
     * @param stream
     */
    public void put(String key, InputStream stream){
        put(key, stream, null);
    }

    /***
     * Adds an input stream to the request
     *
     * @param key
     * @param stream
     * @param name
     */
    public void put(String key, InputStream stream, String name){
        put(key, stream, name, null);
    }

    /***
     * Adds on input stream to the request
     *
     * @param key
     * @param stream
     * @param name
     * @param contentType
     */
    public void put(String key, InputStream stream, String name, String contentType){
        put(key, stream, name, contentType, autoCloseInputStream);
    }

    /***
     * Adds an input stream to the request
     *
     * @param key
     * @param stream
     * @param name
     * @param contentType
     * @param autoclose
     */
    public void put(String key, InputStream stream, String name, String contentType, boolean autoclose){
        if (key != null && stream != null){
            streamParams.put(key, StreamWrapper.newInstance(stream, name, contentType, autoclose));
        }
    }

    /***
     * Adds param with non-string value (e.g. Map, List, Set)
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value){
        if (key != null && value != null){
            urlParamsWithObject.put(key, value);
        }
    }

    /***
     * Adds a int value to the request
     *
     * @param key
     * @param value
     */
    public void put(String key, int value){
        if (key != null){
            urlParams.put(key, String.valueOf(value));
        }
    }

    /***
     * Adds a long value to the request
     *
     * @param key
     * @param value
     */
    public void put(String key, long value){
        if (key != null){
            urlParams.put(key, String.valueOf(value));
        }
    }

    /***
     * Adds string value to param which can have more than one value
     *
     * @param key
     * @param value
     */
    public void add(String key, String value){
        if (key != null && value != null){
            Object params = urlParamsWithObject.get(key);
            if (params == null){
                params = new HashSet<String>();
                this.put(key, params);
            }
            if (params instanceof List){
                ((List<Object>) params).add(value);

            }else if (params instanceof Set){
                ((Set<Object>) params).add(value);
            }
        }
    }

    /***
     * Removes a parameter from the request
     *
     * @param key the key name for the parameter to remove
     */
    public void remove(String key){
        urlParams.remove(key);
        streamParams.remove(key);
        fileParams.remove(key);
        urlParamsWithObject.remove(key);
        fileArrayParams.remove(key);
    }

    /***
     * Check if a parameter is defined
     *
     * @param key the key name for the parameter to check existence
     * @return
     */
    public boolean has(String key){
        return urlParams.get(key) != null ||
                streamParams.get(key) != null ||
                fileParams.get(key) != null ||
                urlParamsWithObject.get(key) != null ||
                fileArrayParams.get(key) != null;
    }

    public void setHttpEntityIsRepeattable(boolean flag){
        isRepeatable = flag;
    }

    public void setUserJsonStreamer(boolean flag){
        userJsonStreamer = flag;
    }

    /***
     * Sets an additional field when upload a JSON object through the streamer
     * to hold the time, in milliseconds, it took to upload the payload. By
     * default, this field is set to "_elapsed".
     *
     * @param value field name to add elapsed time, or null to disable
     */
    public void setElapsedFielInJsonStreamer(String value){
        elapsedFielInJsonStreamer = value;
    }

    /***
     * set global flag which determines whether to automatically close input streams on
     * successful upload
     *
     * @param flag boolean wheather to automatically close input streams
     */
    public void setAutoCloseInputStream(boolean flag){
        autoCloseInputStream = flag;
    }

    /***
     * http get builder
     *
     * @return
     */
    public GetBuilder getGetBuilder(){
        GetBuilder getBuilder = new GetBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            getBuilder.addParams(entry.getKey(), entry.getValue());
        }
        return getBuilder;
    }

    /***
     * http post builder
     *
     * @return
     */
    public PostFormBuilder getPostFormBuilder(){
        PostFormBuilder postFormBuilder = new PostFormBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            postFormBuilder.addParams(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            postFormBuilder.addFile(entry.getKey(), entry.getValue().file.getAbsolutePath(), entry.getValue().file);
        }
        return postFormBuilder;
    }

    public PostStringBuilder getPostJsonBuilder(){
        PostStringBuilder postStringBuilder = new PostStringBuilder();
        postStringBuilder.content(gson.toJson(urlParams));
        return postStringBuilder;
    }

    /***
     * 流类
     */
    public static class StreamWrapper{
        public InputStream inputStream;
        public String name;
        public String contentType;
        public boolean autoClose;

        public StreamWrapper(InputStream inputStream, String name, String contentType, boolean autoClose) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
            this.autoClose = autoClose;
        }

        static StreamWrapper newInstance(InputStream inputStream, String name, String contentType, boolean autoClose){
            return new StreamWrapper(
                    inputStream,
                    name,
                    contentType == null ? APPLICATION_OCTET_STREAM : contentType,
                    autoClose);
        }
    }

    /***
     * File类
     */
    public static class FileWrapper implements Serializable{
        public File file;
        public String contentType;
        public String customFileName;

        public FileWrapper(File file, String contentType, String customFileName) {
            this.file = file;
            this.contentType = contentType;
            this.customFileName = customFileName;
        }
    }


}
