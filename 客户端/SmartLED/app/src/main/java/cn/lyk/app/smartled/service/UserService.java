package cn.lyk.app.smartled.service;

import cn.lyk.app.smartled.entity.Msg;
import cn.lyk.app.smartled.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * 用户服务
 */
public interface UserService {
    /**
     * 登录
     *
     * @return
     */
    @POST("user/login")
    Call<Msg> login(@Body User user);

    /**
     * 退出
     *
     * @return
     */
    @DELETE("user/logout")
    Call<Msg> logout();

    /**
     * 注册
     *
     * @return
     */
    @POST("user/register")
    Call<Msg> register(@Body User user);

    @FormUrlEncoded
    @PUT("user/changePwd")
    Call<Msg> changePwd(@Field("oldPwd") String oldPwd, @Field("newPwd") String newPwd);

}
