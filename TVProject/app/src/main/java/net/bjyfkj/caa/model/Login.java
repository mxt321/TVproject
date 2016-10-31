package net.bjyfkj.caa.model;

import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.util.SharedPreferencesUtils;

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




}
