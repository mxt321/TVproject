package net.bjyfkj.caa.util;

import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.eventBus.GetAdsPlayListEventBus;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import de.greenrobot.event.EventBus;

/**
 * Created by YFKJ-1 on 2016/11/10.
 */
public class getAdsPlayListUtil {

    /**
     * 获取广告视频列表
     */
    public static void getAdsPlayList() {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "getAdsPlayList");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("getAdsPlayList"));
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                EventBus.getDefault().post(new GetAdsPlayListEventBus(result));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
