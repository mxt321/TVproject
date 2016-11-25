package net.bjyfkj.caa.util;

import android.util.Log;

import net.bjyfkj.caa.constant.SdCardPath;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.eventBus.UpdateVideoEventBus;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/24.
 */

public class UpdateVideo {

    /***
     * 对比本地视频列表
     * 服务器列表不存在 则删除
     *
     * @param list
     */
    public static void UpdateVideolist(final List<VideoData.DataBean> list) {
        File files = new File(SdCardPath.BASE_PATH);
        List<String> sdlist = getVideoList();
        List<String> fwqlist = getfwqlist(list);
        if (files.exists()) {
            if (sdlist.size() > 0) {
                if (fwqlist.size() > 0) {
                    for (int i = 0; i < sdlist.size(); i++) {
                        boolean isExists = true;
                        for (int j = 0; j < fwqlist.size(); j++) {
                            if (sdlist.get(i).equals(fwqlist.get(j))) {
                                isExists = false;
                            }
                        }
                        if (isExists == true) {
                            File file = new File(SdCardPath.BASE_PATH + sdlist.get(i));
                            file.delete();
                            Log.i("file", file.getName() + "删除了");
                        }
                    }
                } else {
                    files.delete();
                }
            }
        }
        EventBus.getDefault().post(new UpdateVideoEventBus(true));
    }


    /***
     * 获取sd卡下的视频名称列表
     *
     * @param
     */
    public static List<String> getVideoList() {
        List<String> sdlist = new ArrayList<>();
        File file = new File(SdCardPath.BASE_PATH);
        File[] FileArray = file.listFiles();
        if (null != FileArray && 0 != FileArray.length) {
            for (int i = 0; i < FileArray.length; i++) {
                File files = FileArray[i];
                if (SdCardUtil.getFileType(files.getName()).equals(".mp4")) {
                    sdlist.add(files.getName());
                }
            }
        }
        return sdlist;
    }

    /***
     * 获取服务器视频名称列表
     *
     * @param list
     * @return
     */
    public static List<String> getfwqlist(List<VideoData.DataBean> list) {
        List<String> fwqlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUrl() != null) {
                String path = list.get(i).getUrl().substring(list.get(i).getUrl().lastIndexOf("/") + 1);
                fwqlist.add(path);
            }
        }
        return fwqlist;
    }


}
