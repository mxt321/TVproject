package net.bjyfkj.caa.util;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class DownLoadVedio {

    private void downloadFile(final String url, String path) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(x.app(), "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(x.app(), "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
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
