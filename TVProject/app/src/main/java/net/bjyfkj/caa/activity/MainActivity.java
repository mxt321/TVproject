package net.bjyfkj.caa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.bjyfkj.caa.R;
import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.presenter.DeviceLoginPresenter;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.MD5Util;
import net.bjyfkj.caa.util.PropertiesUtils;
import net.bjyfkj.caa.util.SharedPreferencesUtils;
import net.bjyfkj.caa.view.IDeviceLoginView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity implements IDeviceLoginView {

    private AlertDialog.Builder builder;
    private View view;
    private DeviceLoginPresenter deviceLoginPresenter;
    private EditText device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }

    /***
     * 初始化
     */
    public void init() {
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_view, null);
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        deviceLoginPresenter = new DeviceLoginPresenter(this);
        deviceLoginPresenter.login();
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


    @Override
    public String getDeviceId() {
        return device_id.getText().toString();
    }

    @Override
    public void alertView() {
        builderShow();
    }


    @Override
    public String getSPDevice_id() {
        return SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString();
    }

    @Override
    public void setDeviceId() {
        device_id.setText("");
    }

    @Override
    public void isdialog(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(x.app(), "登陆成功", Toast.LENGTH_SHORT).show();
            SharedPreferencesUtils.setParam(x.app(), LoginId.DEVICELOGINSTATE, device_id + "");
            JPushUtil.setAlias(x.app(), "d" + device_id);
        } else {
            Toast.makeText(x.app(), "登录失败", Toast.LENGTH_SHORT).show();
            builderShow();
        }
    }


    /***
     * 弹出输入框
     */
    public void builderShow() {
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_view, null);
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请输入设备ID号");
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("EditText", device_id.getText().toString() + "");
                deviceLoginPresenter.login();
                dialog.dismiss();
            }
        });
        builder.show();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }
}
