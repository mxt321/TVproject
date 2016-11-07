package net.bjyfkj.caa.mvp.presenter;

import android.util.Log;

import net.bjyfkj.caa.constant.SdCardPath;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.mvp.biz.DeviceDownLoadBiz;
import net.bjyfkj.caa.mvp.biz.IDeviceDownLoadBiz;
import net.bjyfkj.caa.mvp.biz.OnDeviceDownLoadVideoListener;
import net.bjyfkj.caa.mvp.view.IDeviceDownLoadVideoView;
import net.bjyfkj.caa.util.SdCardUtil;

import java.util.List;

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
        List<VideoData.DataBean> videolist = iDeviceDownLoadVideoView.VideoPlayList();
        for (VideoData.DataBean dataBean : videolist) {
            String url = dataBean.getUrl();
            String path = videolist.get(0).getUrl();
            path = SdCardPath.BASE_PATH + path.substring(path.lastIndexOf("/") + 1);

            if (SdCardUtil.fileIsExists(path)) {
                Log.i("CAAdownLoadVideo -- ", "文件已存在");
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
        iDeviceDownLoadVideoView.downloadAll();


    }

}
