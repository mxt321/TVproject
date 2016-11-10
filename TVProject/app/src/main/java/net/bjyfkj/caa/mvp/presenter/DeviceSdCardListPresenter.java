package net.bjyfkj.caa.mvp.presenter;

import net.bjyfkj.caa.mvp.biz.DeviceSdCardBiz;
import net.bjyfkj.caa.mvp.biz.IDeviceSdCardBiz;
import net.bjyfkj.caa.mvp.biz.OnDeviceSdCardListener;
import net.bjyfkj.caa.constant.SdCardPath;
import net.bjyfkj.caa.mvp.view.IDeviceSdCardView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class DeviceSdCardListPresenter {
    private IDeviceSdCardBiz iDeviceSdCardBiz;
    private IDeviceSdCardView iDeviceSdCardView;

    public DeviceSdCardListPresenter(IDeviceSdCardView iDeviceSdCardView) {
        this.iDeviceSdCardView = iDeviceSdCardView;
        iDeviceSdCardBiz = new DeviceSdCardBiz();
    }

    /***
     * 获取SdCrad中的视频列表
     */
    public void getSdCardVideoList() {
        File file = new File(SdCardPath.BASE_PATH);
        if (file.exists() && file.isDirectory()) {
            if (file.list().length > 0) {
                iDeviceSdCardBiz.getSdCradVideoList(SdCardPath.BASE_PATH, new OnDeviceSdCardListener() {
                    @Override
                    public void getSdCardSuccess(List<Map<String, String>> sdlist) {
                        iDeviceSdCardView.getSdCardVideoList(sdlist);
                    }

                    @Override
                    public void getSdCardFailed() {
                        iDeviceSdCardView.SdCardDirectory();
                    }
                });
            } else {
                iDeviceSdCardView.SdCardDirectory();
            }
        }

    }

}
