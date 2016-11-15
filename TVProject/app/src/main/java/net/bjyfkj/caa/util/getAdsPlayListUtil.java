package net.bjyfkj.caa.util;

import android.util.Log;

import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.entity.AdsPlayData;
import net.bjyfkj.caa.eventBus.GetAdsPlayListEventBus;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

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
                Log.i("getAdsPlayList", result + "");
                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.getInt("status");
                    if (status == 1) {
                        AdsPlayData adsPlayData = GsonUtils.json2Bean(result, AdsPlayData.class);
                        List<AdsPlayData.DataBean> adslist = adsPlayData.getData();
                        Log.i("adslist", adslist.size() + "");
                        EventBus.getDefault().post(new GetAdsPlayListEventBus(adslist));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    /***
     * @param adsid
     */
    public static void setPlayCount(int adsid) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "setPlayCount");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("setPlayCount"));
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        params.addBodyParameter("ads_id", adsid + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("setPlayCount", result + "");
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
