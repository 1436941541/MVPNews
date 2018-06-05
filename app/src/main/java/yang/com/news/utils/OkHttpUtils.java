package yang.com.news.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpUtils封装请求类
 * Created by 杨云杰 on 2018/5/31.
 */

public class OkHttpUtils {
    private static OkHttpUtils mInstance;//单例模式
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30,TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10,TimeUnit.SECONDS);
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }
//   /*防止线程异步*/
    public synchronized static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    private void getRequest(String url,ResultCallback resultCallback) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(resultCallback,request);
    }
    private void deliveryResult(final ResultCallback resultCallback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(resultCallback,e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                if (resultCallback.mType == String.class) {
                    sendSuccessCallBack(resultCallback,str);
                } else {
                    Object object = JsonUtils.deserialize(str,resultCallback.mType);
                    sendSuccessCallBack(resultCallback,object);
                }
            }
        });
    }

    private void sendSuccessCallBack(final ResultCallback resultCallback, final Object object) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
               if (resultCallback != null) {
                   resultCallback.onSuccess(object);
               }
            }
        });
    }
    /**
     * 让响应的事件由handler发送到主线程中去执行
     * @param resultCallback
     * @param e
     */
    private void sendFailCallback(final ResultCallback resultCallback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (resultCallback != null) {
                    resultCallback.onFailure(e);
                }
            }
        });
    }
    private void postRequest(String url,ResultCallback resultCallback,List<Param> params) {
        Request request = buildPostRequest(url,params);
        deliveryResult(resultCallback,request);
    }

    /**
     * 构建请求正文
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, List<Param> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param:params) {
            builder.add(param.key,param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }
    /**
     *get请求
     * @param url 请求url
     * @param resultCallback  请求回调
     */
    public static void get(String url,ResultCallback resultCallback) {
        getmInstance().getRequest(url,resultCallback);
    }

    /**
     * post请求
     * @param url
     * @param resultCallback
     * @param params
     */
    public static void post(String url, ResultCallback resultCallback, List<Param> params) {
        getmInstance().postRequest(url,resultCallback,params);
    }
    /**
     * 请求回调类
      * @param <T>
     */
    public static abstract class ResultCallback<T> {
        Type mType;
        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }
        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Exception e);
    }
    /**
     * post上传时的request里的键值对
     */
    public static class Param {
        String key;
        String value;
        public Param(String key,String value) {
            this.key = key;
            this.value = value;
        }
        }
    }

