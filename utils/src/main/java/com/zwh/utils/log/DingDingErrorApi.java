package com.zwh.utils.log;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DingDingErrorApi {

    // 演示版 上报
    @POST("robot/send?access_token=3cf395b671ce2e08e203c1ae43eb689cf84cc1abdac519485a8e6797ea9e7f64")
    Observable<Object> postLandscapeError(@Body ErrorBean bean);
}
