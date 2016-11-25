package net.bjyfkj.caa.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class SdCardPath {

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "videoPlay/";
    public static final String ADS_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "adsPlay/";

}
