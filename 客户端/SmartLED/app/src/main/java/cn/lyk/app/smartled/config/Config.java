package cn.lyk.app.smartled.config;

import java.util.concurrent.TimeUnit;

import cn.lyk.app.smartled.util.AddCookiesInterceptor;
import cn.lyk.app.smartled.util.ReceivedCookiesInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Config {
    private static final Config i = new Config();

    public String cookies;
    public final Retrofit retrofit;

    public final String serverAddr = "http://192.168.165.30:80/api/app/";
    public final String mqttServer = "tcp://192.168.31.127:1883";

    public boolean login = false;

    private Config() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new ReceivedCookiesInterceptor())
                .addInterceptor(new AddCookiesInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(serverAddr)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Config getInstance() {
        return i;
    }

}
