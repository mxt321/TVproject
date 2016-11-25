package net.bjyfkj.caa.util;

import android.util.Log;

import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.entity.AdsPlayData;
import net.bjyfkj.caa.eventBus.GetAdsPlayListEventBus;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import static net.bjyfkj.caa.constant.SdCardPath.ADS_PATH;


/**
 * Created by YFKJ-1 on 2016/11/10.
 */
public class getAdsPlayListUtil {

    private static int adspisotion = 0;

    /**
     * 获取广告视频列表
     */
    public static void getAdsPlayList() {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "getAdsPlayList");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("getAdsPlayList"));
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("getAdsPlayList", result + "");
                try {
                    SharedPreferencesUtils.setParam(x.app(), LoginId.JSONCACHE, "");
                    SharedPreferencesUtils.setParam(x.app(), LoginId.JSONCACHE, result);
                    JSONObject json = new JSONObject(result);
                    int status = json.getInt("status");
                    if (status == 1) {
                        AdsPlayData adsPlayData = GsonUtils.json2Bean(result, AdsPlayData.class);
                        List<AdsPlayData.DataBean> list = adsPlayData.getData();
                        Log.i("adslist", list.size() + "");
                        EventBus.getDefault().post(new GetAdsPlayListEventBus(list));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /***
     * 广告自增
     *
     * @param adsid
     */
    public static void setPlayCount(String adsid) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "setPlayCount");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("setPlayCount"));
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        params.addBodyParameter("ads_id", adsid + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("setPlayCount", result + "");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void downLoadImages(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            downloads(list.get(i));
        }
    }

    public static String downLoadImage(String url) {
        String path = "";
        return path;
    }

    /***
     * 下载图片到本地
     * 访问网络
     *
     * @param url
     */
    public static void downloads(String url) {
        final String path = ADS_PATH + url.substring(url.lastIndexOf("/") + 1);
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true);
        params.setSaveFilePath(path);
        params.setCancelFast(true);
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
