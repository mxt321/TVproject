package net.bjyfkj.caa.mvp.presenter;

import android.util.Log;

import net.bjyfkj.caa.mvp.biz.DeviceLoginBiz;
import net.bjyfkj.caa.mvp.biz.IDeviceLoginBiz;
import net.bjyfkj.caa.mvp.biz.OnDeviceGetPlayListener;
import net.bjyfkj.caa.mvp.biz.OnDeviceLoginListener;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.mvp.view.IDeviceLoginView;

import java.util.List;


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
            Log.i("CAAP-getSPDevice_id", iDeviceLoginView.getSPDevice_id() + "");
            loginProsenter(iDeviceLoginView.getSPDevice_id());
        }
    }

    /***
     * 登录
     */
    public void loginProsenter(String device_id) {
        iDeviceLoginBiz.login(device_id, new OnDeviceLoginListener() {
            @Override
            public void loginSuccess(String device_id, boolean islogin) {
                iDeviceLoginView.isdialog(device_id, true);
            }

            @Override
            public void loginFailed(boolean islogin) {
                iDeviceLoginView.setDeviceId();
                iDeviceLoginView.isdialog(null, false);
            }
        });
    }

    public void getVideoPlay() {
        iDeviceLoginBiz.getVedioPlayList(iDeviceLoginView.getSPDevice_id().toString(), new OnDeviceGetPlayListener() {
            @Override
            public void loginSuccess(List<VideoData.DataBean> list) {
                iDeviceLoginView.getVideoPlayList(list);
            }

            @Override
            public void loginFailed() {

            }
        });
    }

}