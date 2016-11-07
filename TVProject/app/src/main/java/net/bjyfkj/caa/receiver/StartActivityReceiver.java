package net.bjyfkj.caa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.bjyfkj.caa.UI.activity.MainActivity;

import org.xutils.x;

/**
 * Created by YFKJ-1 on 2016/11/7.
 */
public class StartActivityReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Toast.makeText(x.app(), "开机了", Toast.LENGTH_SHORT).show();
            Intent StartIntent = new Intent(context, MainActivity.class); //接收到广播后，跳转到MainActivity
            StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(StartIntent);
        }
    }
}
