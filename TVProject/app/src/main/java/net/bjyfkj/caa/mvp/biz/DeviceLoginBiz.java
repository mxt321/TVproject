package net.bjyfkj.caa.mvp.biz;

import android.util.Log;

import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.util.GsonUtils;
import net.bjyfkj.caa.util.MD5Util;
import net.bjyfkj.caa.util.PropertiesUtils;
import net.bjyfkj.caa.util.TimeUtil;
import net.bjyfkj.caa.util.UpdateVideo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public class DeviceLoginBiz implements IDeviceLoginBiz {

    @Override
    public void login(final String device_id, final OnDeviceLoginListener linsterenr) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "device_login");
        Log.i("sign", sign + "");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("login"));
        params.addBodyParameter("device_id", device_id);
        params.addBodyParameter("sign", sign);
        Log.i("Device-device_id", device_id + "");
        Log.i("Device-sign", sign + "");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("Device-result", result + "");
                try {
                    JSONObject json = new JSONObject(result);
                    int jsonInt = json.getInt("status");
                    if (jsonInt == 1) {
                        linsterenr.loginSuccess(device_id, true);
                    } else {
                        linsterenr.loginFailed(false);
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

    @Override
    public void getVedioPlayList(String device_id, final OnDeviceGetPlayListener linsterenr) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "getVideoPlayList");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("getVideoPlayList"));
        params.addBodyParameter("sign", sign + "");
        params.addBodyParameter("device_id", device_id);
        params.setConnectTimeout(15000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("getVideoPlayList", result);
                try {
                    JSONObject json = new JSONObject(result);
                    int jsonInt = json.getInt("status");
                    if (jsonInt == 1) {
                        VideoData vd = GsonUtils.json2Bean(result, VideoData.class);
                        List<VideoData.DataBean> list = vd.getData();
                        UpdateVideo.UpdateVideolist(list);
                        linsterenr.loginSuccess(list);
                    } else {
                        UpdateVideo.deleteFile();
                        linsterenr.loginFailed();
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

    @Override
    public void updateDevicetime(String device_id, final OnUpdateDeviceTimeListener listener) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "getDateTime");
        Log.i("sign", sign + "");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("getDateTime"));
        params.addBodyParameter("device_id", device_id);
        params.addBodyParameter("sign", sign);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int jsonInt = jsonObject.getInt("status");
                    if (jsonInt == 1) {
                        JSONObject jsonObject1 = new JSONObject(jsonObject.get("data").toString());
                        String datetime = jsonObject1.getString("datetime");
                        TimeUtil.testDate(datetime);
                        Log.i("datetime", datetime + "");
                        listener.updateSuccess();
                    } else {
                        listener.updateFailed();
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


}
