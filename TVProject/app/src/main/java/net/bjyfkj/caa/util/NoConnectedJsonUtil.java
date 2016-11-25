package net.bjyfkj.caa.util;

import net.bjyfkj.caa.entity.AdsPlayData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/25.
 */

public class NoConnectedJsonUtil {


    /***
     * 本地缓存的广告信息
     *
     * @param json
     * @return
     */
    public static List<AdsPlayData.DataBean> noCongetAdsList(String json) {
        List<AdsPlayData.DataBean> list = new ArrayList<>();
        if (!json.equals("")) {
            AdsPlayData adsPlayData = GsonUtils.json2Bean(json, AdsPlayData.class);
            list = adsPlayData.getData();
            return list;
        }
        return null;
    }

}
