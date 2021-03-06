package net.bjyfkj.caa.util;

import net.bjyfkj.caa.entity.DanmakuItemData;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/22.
 */

public class DanmakuUtil {

    /***
     * 过滤重复广告
     */
    public static List<DanmakuItemData.DataBean> Danmaku(List<DanmakuItemData.DataBean> itemDataBeen) {
        for (int i = 0; i < itemDataBeen.size() - 1; i++) {
            for (int j = itemDataBeen.size() - 1; j > i; j--) {
                if (itemDataBeen.get(i).getDevice_id() == itemDataBeen.get(j).getDevice_id()) {
                    itemDataBeen.remove(i);
                }
            }
        }
        return itemDataBeen;
    }

}
