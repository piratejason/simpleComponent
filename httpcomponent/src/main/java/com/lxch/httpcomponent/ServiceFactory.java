package com.lxch.httpcomponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright (c) 2017 KUAICTO
 * Use is subject to license terms
 * Created by zhuyijun on 2017/12/14 10:30.
 * Email:2759826340@qq.com
 * Description:OKHttp + Retrofit封装
 */

public class ServiceFactory {

    //创建OKHttp对象
    private static OkHttpClient.Builder httpClient;
    //创建Retrofit对象
    private static Retrofit.Builder RetrofitBuilder;

    private static ServiceFactory instance;
    private Map<String, Integer> maps;
    private List list;

    private ServiceFactory() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder();
            //添加拦截器
            httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE));
        }
        if (RetrofitBuilder == null) {
            RetrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.appUrl)
                    .addConverterFactory(GsonConverterFactory.create());
        }
        maps = new HashMap<>();
        list = new ArrayList();
    }

    public static ServiceFactory newInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    /**
     * 创建接口
     *
     * @param clz
     * @param <S>
     * @return
     */
    public synchronized <S> S createService(Class<S> clz) {
//        S s1 = (S) maps.get(clz.toString());
//        if (s1 == null) {
//            Retrofit retrofit = RetrofitBuilder.client(httpClient.build()).build();
//            s1 = retrofit.create(clz);
//            maps.put(clz.toString(), s1);
//        }
        Integer integer = maps.get(clz.toString());
        if (integer != null) {
            return (S) list.get(integer);
        } else {
            Retrofit retrofit = RetrofitBuilder.client(httpClient.build()).build();
            S s = retrofit.create(clz);
            maps.put(clz.toString(), list.size());
            list.add(s);
            return s;
        }

    }

    public ServiceFactory addInterceptor(Interceptor interceptor) {
        httpClient.addInterceptor(interceptor);
        return this;
    }

}
