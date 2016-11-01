package net.bjyfkj.caa.biz;

import android.util.Log;

import net.bjyfkj.caa.util.MD5Util;
import net.bjyfkj.caa.util.PropertiesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public class DeviceLoginBiz implements IDeviceLoginBiz {

    @Override
    public void login(final String device_id, final OnDeviceLoginLinsterenr linsterenr) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "device_login");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("login"));
        params.addBodyParameter("device_id", device_id);
        params.addBodyParameter("sign", sign);
        Log.i("sign", sign + "");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("result", result + "");
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


}
