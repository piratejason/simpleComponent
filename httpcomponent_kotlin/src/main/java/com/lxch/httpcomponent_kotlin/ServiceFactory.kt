package com.lxch.httpcomponent_kotlin

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by luoxiaocheng on 2018/3/28.
 */
class ServiceFactory {
    companion object {
        var httpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE))
        var RetrofitBuilder = Retrofit.Builder()
                .baseUrl(BuildConfig.appUrl)
                .addConverterFactory(GsonConverterFactory.create())
        var maps = HashMap<String, Int>()
        var list = mutableListOf<Any>()
    }

    fun addInterceptor(interceptor: Interceptor): ServiceFactory {
        httpClient.addInterceptor(interceptor)
        return this
    }

    fun <T> createService(clz: Class<T>): Any {
        val get = maps.get(clz.toString())
        if (get != null) {
            return list.get(get)
        } else {
            var retrofit = RetrofitBuilder.client(httpClient.build()).build()
            val create = retrofit.create(clz)
            maps.put(clz.toString(), list.size)
            list.add(create!!)
            return create
        }
    }
}