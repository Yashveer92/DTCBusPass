package com.example.dtcbuspass.network;


import com.example.dtcbuspass.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitBuilder {



    private static final String BASE_URL = "http://192.168.43.27/PassApi/public/api/";



    private final static OkHttpClient client = builderClient();

    private final static Retrofit retrofit = builderRetrofit(client);

    private static OkHttpClient builderClient()
    {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()

        .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder builder = request.newBuilder()
                 .addHeader("Accept", "application/json")
                  .addHeader("Connection", "close");

                 request = builder.build();

                 return chain.proceed(request);


            }
        });


        if(BuildConfig.DEBUG)
        {

            builder.addNetworkInterceptor(new StethoInterceptor());

        }


        return builder.build();

    }


    private static Retrofit builderRetrofit(OkHttpClient client){


        return new Retrofit.Builder()

                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();


    }


    public static <T> T createService(Class<T> service)
    {

        return retrofit.create(service);



    }



    public static Retrofit getRetrofit() {
        return retrofit;
    }
}