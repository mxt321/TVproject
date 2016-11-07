package net.bjyfkj.caa.mvp.view;

import net.bjyfkj.caa.entity.VideoData;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface IDeviceDownLoadVideoView {
    void downLoadSuccess();

    void downLoadFailed();

    List<VideoData.DataBean> VideoPlayList();

    void downloadAll();

}
