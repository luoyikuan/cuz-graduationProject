package cn.lyk.app.smartled.util;

import java.io.IOException;

import cn.lyk.app.smartled.config.Config;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (Config.getInstance().cookies != null) {
            builder.addHeader("Cookie", Config.getInstance().cookies);
        }
        return chain.proceed(builder.build());
    }
}
