package net.bjyfkj.caa.view;

/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public interface IDeviceLoginView {
    String getDeviceId();

    void alertView();

    String getSPDevice_id();

    void setDeviceId();

    void isdialog(String device_id, boolean isSuccess);
}
