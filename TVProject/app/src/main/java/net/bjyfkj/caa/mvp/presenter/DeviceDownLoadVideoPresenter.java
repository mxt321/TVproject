package net.bjyfkj.caa.mvp.presenter;

import android.util.Log;

import net.bjyfkj.caa.constant.SdCardPath;
import net.bjyfkj.caa.mvp.biz.DeviceDownLoadBiz;
import net.bjyfkj.caa.mvp.biz.IDeviceDownLoadBiz;
import net.bjyfkj.caa.mvp.biz.OnDeviceDownLoadVideoListener;
import net.bjyfkj.caa.mvp.view.IDeviceDownLoadVideoView;
import net.bjyfkj.caa.util.SdCardUtil;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class DeviceDownLoadVideoPresenter {

    private IDeviceDownLoadVideoView iDeviceDownLoadVideoView;
    private IDeviceDownLoadBiz deviceDownLoadBiz;

    public DeviceDownLoadVideoPresenter(IDeviceDownLoadVideoView iDeviceDownLoadVideoView) {
        this.iDeviceDownLoadVideoView = iDeviceDownLoadVideoView;
        this.deviceDownLoadBiz = new DeviceDownLoadBiz();
    }

    /***
     * 下载视频
     */
    public void downLoadVideo() {
        String url = iDeviceDownLoadVideoView.VideoPlayPath();
        String path = SdCardPath.BASE_PATH + url.substring(url.lastIndexOf("/") + 1);
        if (SdCardUtil.fileIsExists(path)) {
            Log.i("CAAdownLoadVideo -- ", "文件已存在");
            iDeviceDownLoadVideoView.downLoadSuccess();
        } else {
            Log.i("CAAdownLoadVideo -- ", "文件不存在");
            Log.i("CAAdownLoadVideo -- ", "开始下载");
            deviceDownLoadBiz.downloadFile(url, path, new OnDeviceDownLoadVideoListener() {
                @Override
                public void downLoadSuccess(boolean isdownload) {
                    iDeviceDownLoadVideoView.downLoadSuccess();
                }

                @Override
                public void downLoadFailed(boolean isdownload) {
                    iDeviceDownLoadVideoView.downLoadFailed();
                }
            });
        }
    }

}
