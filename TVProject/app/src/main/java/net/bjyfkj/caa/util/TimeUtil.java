package net.bjyfkj.caa.util;

import android.text.format.Time;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by YFKJ-1 on 2016/11/7.
 */
public class TimeUtil {
    /**
     * 修改系统时间
     */
    public static void testDate(String time) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + time + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String initWeclomeText() {
        Time t = new Time();
        t.setToNow();
        if (t.hour >= 6 && t.hour <= 14) {
            return "0";
        } else if (t.hour >= 15 && t.hour <= 23) {
            return "1";
        }
        return null;
    }
}