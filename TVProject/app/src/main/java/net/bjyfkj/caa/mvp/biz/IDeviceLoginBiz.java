package net.bjyfkj.caa.mvp.biz;

/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public interface IDeviceLoginBiz {
    void login(String device_id, OnDeviceLoginListener linsterenr);

    void getVedioPlayList(String device_id, OnDeviceGetPlayListener linsterenr);
}
