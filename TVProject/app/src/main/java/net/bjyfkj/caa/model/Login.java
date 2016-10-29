package net.bjyfkj.caa.model;

import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.util.PropertiesUtils;
import net.bjyfkj.caa.util.SharedPreferencesUtils;

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
        String UserLoginState = (String) SharedPreferencesUtils.getParam(x.app(), LoginId.USERLOGINSTATE, "");
        if (UserLoginState.equals("")) {
            return false;
        }
        return true;
    }

    /***
     * 使用设备ID号登录
     *
     * @return
     */
    public static boolean Login(String userid) {
        final boolean[] bool = {false};
        RequestParams params = new RequestParams(PropertiesUtils.getpath("login"));
        params.addBodyParameter("userid", userid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                bool[0] = true;
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
        return bool[0];
    }


}
