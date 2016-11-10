package net.bjyfkj.caa.mvp.biz;

import net.bjyfkj.caa.entity.VideoData;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface OnDeviceGetPlayListener {
    void loginSuccess(List<VideoData.DataBean> list);

    void loginFailed();
}
