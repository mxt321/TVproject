package net.bjyfkj.caa.mvp.view;

import net.bjyfkj.caa.entity.VideoData;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/10/31.
 */
public interface IDeviceLoginView {
    String getDeviceId();

    void alertView();

    String getSPDevice_id();

    void setDeviceId();

    void isdialog(String device_id, boolean isSuccess);

    void getVideoPlayList(List<VideoData.DataBean> list);
}
