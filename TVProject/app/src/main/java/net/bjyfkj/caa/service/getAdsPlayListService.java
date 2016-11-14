package net.bjyfkj.caa.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.bjyfkj.caa.util.getAdsPlayListUtil;

/**
 * Created by YFKJ-1 on 2016/11/9.
 */
public class getAdsPlayListService extends Service {
    public static final String ACTION = "net.bjyfkj.caa.getAdsPlayListService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        new PollingThread().start();
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     *
     * @Author Ryan
     * @Create 2013-7-13 上午10:18:34
     */
    int count = 0;

    class PollingThread extends Thread {
        @Override
        public void run() {
            getAdsPlayListUtil.getAdsPlayList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }
}
