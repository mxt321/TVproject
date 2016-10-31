package net.bjyfkj.caa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.bjyfkj.caa.R;
import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.model.Login;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.MD5Util;
import net.bjyfkj.caa.util.PropertiesUtils;
import net.bjyfkj.caa.util.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity {

    @InjectView(R.id.button)
    Button button;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //本地是否保留有设备ID
        if (Login.ifLogin()) {
            Toast.makeText(this.getApplicationContext(), "本地有保存设备信息", Toast.LENGTH_SHORT).show();
            Login(SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        } else {
            Toast.makeText(this.getApplicationContext(), "本地没有保存设备信息", Toast.LENGTH_SHORT).show();
            alertView();
        }
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


    /****
     * 弹出的输入框
     *
     * @return
     */
    public void alertView() {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_view, null);
        final EditText userid = (EditText) view.findViewById(R.id.edt_userid);
        //给alert弹框赋值
        alert = new AlertDialog.Builder(MainActivity.this).setTitle("请输入设备ID号")
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("EditText", userid.getText().toString() + "");
                        Login(userid.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();
    }


    /***
     * 使用设备ID号登录
     *
     * @return
     */
    public void Login(final String device_id) {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "device_login");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("login"));
        params.addBodyParameter("device_id", device_id);
        params.addBodyParameter("sign", sign);
        Log.i("sign", sign + "");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("result", result + "");
                try {
                    JSONObject json = new JSONObject(result);
                    int jsonInt = json.getInt("status");
                    if (jsonInt == 1) {
                        Toast.makeText(x.app(), "登陆成功", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtils.setParam(x.app(), LoginId.DEVICELOGINSTATE, device_id + "");
                        JPushUtil.setAlias(x.app(), "d" + device_id);
                    } else {
                        alertView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /***
     * 退出登录
     */
    public void logout() {
        String sign = MD5Util.encrypt("Device" + MD5Util.encrypt("bjyfkj4006010136") + "device_logout");
        RequestParams params = new RequestParams(PropertiesUtils.getpath("logout"));
        params.addBodyParameter("device_id", SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString());
        params.addBodyParameter("sign", sign + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        Toast.makeText(x.app(), "退出登录成功", Toast.LENGTH_SHORT).show();
                        JPushUtil.setAlias(x.app(), "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @OnClick(R.id.button)
    public void onClick() {
        logout();
    }
}
