package cn.lyk.app.smartled.util;

import java.io.IOException;

import cn.lyk.app.smartled.config.Config;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Config.getInstance().cookies = originalResponse.header("Set-Cookie");
        }

        return originalResponse;
    }
}
