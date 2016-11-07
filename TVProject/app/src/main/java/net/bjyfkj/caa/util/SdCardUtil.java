package net.bjyfkj.caa.util;

import java.io.File;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class SdCardUtil {

    public static boolean fileIsExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

}
