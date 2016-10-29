package net.bjyfkj.caa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import net.bjyfkj.caa.R;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity {

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (Login.ifLogin()) {
//            if (Login.Login(SharedPreferencesUtils.getParam(x.app(), LoginId.USERLOGINSTATE, "").toString())) {
//
//            }
//        } else {
//            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout
//                    .alert_view, null);
//            final EditText userid = (EditText) view.findViewById(R.id.edt_userid);
//            //给alert弹框赋值
//            alert = new AlertDialog.Builder(MainActivity.this).setTitle("请输入设备ID号")
//                    .setView(view)
//                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                           Login.Login(userid.toString());
//                        }
//                    }).setNegativeButton("取消", null).show();
//            if (Login.Login(userid.getText().toString())) {
//
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(x.app());
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(x.app());
    }
}
