package cn.lyk.app.smartled.service;

import java.util.List;

import cn.lyk.app.smartled.entity.CmdPacket;
import cn.lyk.app.smartled.entity.Data;
import cn.lyk.app.smartled.entity.Mcu;
import cn.lyk.app.smartled.entity.Msg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface McuService {

    @DELETE("mcu/del/{mcuId}")
    Call<Msg> delMcu(@Path("mcuId") Long mcuId);

    @GET("mcu/list")
    Call<List<Mcu>> list();

    @POST("mcu/add")
    Call<Msg> add(@Body Mcu mcu);

    @POST("mcu/cmd/{mcuId}")
    Call<Msg> cmd(@Path("mcuId") Long mcuId, @Body CmdPacket cmdPacket);

    @GET("mcu/data/{mcuId}/{p}")
    Call<List<Data>> data(@Path("mcuId") Long mcuId, @Path("p") Integer p);


    @DELETE("mcu/data/{dataId}")
    Call<Msg> delData(@Path("dataId") Long dataId);
}
