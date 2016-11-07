package net.bjyfkj.caa.mvp.biz;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface IDeviceDownLoadBiz {
    void downloadFile(final String url, String path, final OnDeviceDownLoadVideoListener listener);
}
