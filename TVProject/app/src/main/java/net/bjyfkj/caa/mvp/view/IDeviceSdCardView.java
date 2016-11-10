package net.bjyfkj.caa.mvp.view;

import java.util.List;
import java.util.Map;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public interface IDeviceSdCardView {
    void getSdCardVideoList(List<Map<String,String>> list);

    void SdCardDirectory();

}
