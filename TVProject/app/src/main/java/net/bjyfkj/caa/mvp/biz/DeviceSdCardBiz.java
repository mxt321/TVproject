package net.bjyfkj.caa.mvp.biz;

import net.bjyfkj.caa.util.SdCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class DeviceSdCardBiz implements IDeviceSdCardBiz {

    @Override
    public void getSdCradVideoList(String Path, OnDeviceSdCardListener listener) {
        List<Map<String, String>> sdlist = new ArrayList<Map<String, String>>();
        File file = new File(Path);
        File[] FileArray = file.listFiles();
        if (null != FileArray && 0 != FileArray.length) {
            for (int i = 0; i < FileArray.length; i++) {
                File files = FileArray[i];
                if (SdCardUtil.getFileType(files.getName()).equals(".mp4")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", files.getName() + "");
                    map.put("path", files.getPath() + "");
                    sdlist.add(map);
                }
            }
            listener.getSdCardSuccess(sdlist);
        } else {
            listener.getSdCardFailed();
        }
    }
}
