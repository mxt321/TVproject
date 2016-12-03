package net.bjyfkj.caa.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import net.bjyfkj.caa.util.AppUpdate;

/**
 * Created by YFKJ-1 on 2016/11/22.
 */

public class   AppUpdateService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("AppUpdateService", "onCreate......................");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("AppUpdateService", "onBind......................");
        AppUpdate app = new AppUpdate(this);
        app.checkUpdateForTV(new AppUpdate.OnResult() {
            @Override
            public void onNewVersion() {
                Log.i("onNewVersion","app有新版本");
            }

            @Override
            public void onLatestVersion() {
                Log.i("onLatestVersion","app已是最新版本");
            }

            @Override
            public void onDownloading(long current, long total) {
                Log.i("onDownloading","app正在下载");
            }

            @Override
            public void onDownLoaCompleted() {
                Log.i("onDownLoaCompleted","app下载成功");
            }

            @Override
            public void onError() {
                Log.i("onError","app下载失败");
            }
        });
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("AppUpdateService", "onUnbind......................");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("AppUpdateService", "onDestroy......................");
    }
}
