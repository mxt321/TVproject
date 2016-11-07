package net.bjyfkj.caa.mvp.biz;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface IDeviceSdCardBiz {
    void getSdCradVideoList(String Path, OnDeviceSdCardListener listener);
}
