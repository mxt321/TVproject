package net.bjyfkj.caa.mvp.biz;

/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public interface OnDeviceLoginListener {
    void loginSuccess(String device_id, boolean islogin);

    void loginFailed(boolean islogin);
}
