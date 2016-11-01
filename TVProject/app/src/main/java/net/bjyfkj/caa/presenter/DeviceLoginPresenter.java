package net.bjyfkj.caa.presenter;

import android.util.Log;

import net.bjyfkj.caa.biz.DeviceLoginBiz;
import net.bjyfkj.caa.biz.IDeviceLoginBiz;
import net.bjyfkj.caa.biz.OnDeviceLoginLinsterenr;
import net.bjyfkj.caa.view.IDeviceLoginView;


/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public class DeviceLoginPresenter {

    private IDeviceLoginBiz iDeviceLoginBiz;
    private IDeviceLoginView iDeviceLoginView;

    public DeviceLoginPresenter(IDeviceLoginView iDeviceLoginView) {
        this.iDeviceLoginView = iDeviceLoginView;
        this.iDeviceLoginBiz = new DeviceLoginBiz();
    }


    /***
     * 登录
     */
    public void login() {
        if (iDeviceLoginView.getSPDevice_id().equals("")) {
            if (iDeviceLoginView.getDeviceId().equals("")) {
                iDeviceLoginView.alertView();
            } else {
                loginProsenter(iDeviceLoginView.getDeviceId());
            }
        } else {
            Log.i("getDeviceId", iDeviceLoginView.getSPDevice_id() + "");
            loginProsenter(iDeviceLoginView.getSPDevice_id());
        }
    }

    /***
     * 登录
     */
    public void loginProsenter(String device_id) {
        iDeviceLoginBiz.login(device_id, new OnDeviceLoginLinsterenr() {
            @Override
            public void loginSuccess(String device_id, boolean islogin) {
                iDeviceLoginView.isdialog(true);
            }

            @Override
            public void loginFailed(boolean islogin) {
                iDeviceLoginView.setDeviceId();
                iDeviceLoginView.isdialog(false);
            }
        });
    }


    /***
     * 退出登录
     */
    public void logout() {

    }


}