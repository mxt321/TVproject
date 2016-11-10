package net.bjyfkj.caa.mvp.biz;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class DeviceDownLoadBiz implements IDeviceDownLoadBiz {


    @Override
    public void downloadFile(String url, String path, final OnDeviceDownLoadVideoListener listener) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                listener.downLoadSuccess(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                listener.downLoadFailed(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
