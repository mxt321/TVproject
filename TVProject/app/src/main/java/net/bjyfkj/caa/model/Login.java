package net.bjyfkj.caa.model;

import android.util.Log;
import android.widget.Toast;

import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.MD5Util;
import net.bjyfkj.caa.util.PropertiesUtils;
import net.bjyfkj.caa.util.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by YFKJ-1 on 2016/10/28.
 */
public class Login {


    /***
     * 判断本地是否拥有设备ID号
     *
     * @return
     */
    public static boolean ifLogin() {
        String UserLoginState = (String) SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "");
        if (UserLoginState.equals("")) {
            return false;
        }
        return true;
    }

    /***
     * 退出登录
     */
    public static void logout() {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "device_logout");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("logout"));
        params.addBodyParameter("device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        params.addBodyParameter("sign", sign + "");
        Log.i("logout  -- device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString() + "");
        Log.i("logout  -- sign", sign + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(x.app(), "退出登录成功", Toast.LENGTH_SHORT).show();
                        JPushUtil.setAlias(x.app(), "");
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
