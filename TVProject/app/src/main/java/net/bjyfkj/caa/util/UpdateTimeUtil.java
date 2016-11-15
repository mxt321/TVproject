package net.bjyfkj.caa.util;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by YFKJ-1 on 2016/11/7.
 */
public class UpdateTimeUtil {
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


//    判断当前时间
//    public void initWeclomeText() {
//        time = (TextView) view.findViewById(R.id.textView2);
//        WeclomeText = (TextView) view.findViewById(R.id.textView3);
//        Time t = new Time();
//        t.setToNow();
//        if (t.hour >= 3 && t.hour <= 12) {
//            time.setText("上午好");
//            WeclomeText.setText("每天醒来，都应该有所期待");
//        } else if (t.hour >= 12 && t.hour <= 18) {
//            time.setText("下午好");
//            WeclomeText.setText("把每个充实的午后当作一件礼物");
//        } else if (t.hour >= 18 || t.hour <= 3) {
//            time.setText("晚上好");
//            WeclomeText.setText("不必仰望月亮，你即星辰之光");
//        }
//
//    }
}
