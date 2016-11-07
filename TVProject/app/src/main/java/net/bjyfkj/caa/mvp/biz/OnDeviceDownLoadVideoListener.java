package net.bjyfkj.caa.mvp.biz;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface OnDeviceDownLoadVideoListener {
    void downLoadSuccess(boolean isdownload);

    void downLoadFailed(boolean isdownload);
}
