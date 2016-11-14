package net.bjyfkj.caa.eventBus;

import net.bjyfkj.caa.entity.AdsPlayData;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/10.
 */
public class GetAdsPlayListEventBus {
    public List<AdsPlayData.DataBean> result;

    public GetAdsPlayListEventBus(List<AdsPlayData.DataBean> result) {
        this.result = result;
    }
}
