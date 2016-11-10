package net.bjyfkj.caa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.bjyfkj.caa.eventBus.JPushEventBus;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * Created by YFKJ-1 on 2016/10/28.
 */
public class JPushReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("onReceive - - ", "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息不会展示在通知栏
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.i("收到了自定义消息@@消息内容是:", content + "");
            Toast.makeText(context, "收到了自定义消息@@消息内容是:" + content, Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new JPushEventBus(content + ""));
        }
    }
}
