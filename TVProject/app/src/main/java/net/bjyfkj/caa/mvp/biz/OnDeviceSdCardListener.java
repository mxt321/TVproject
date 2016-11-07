package net.bjyfkj.caa.mvp.biz;

import java.util.List;
import java.util.Map;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface OnDeviceSdCardListener {
    void getSdCardSuccess(List<Map<String, String>> sdlist);

    void getSdCardFailed();
}
