package net.bjyfkj.caa.UI.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import net.bjyfkj.caa.R;
import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.eventBus.JPushEventBus;
import net.bjyfkj.caa.model.Login;
import net.bjyfkj.caa.mvp.presenter.DeviceDownLoadVideoPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceLoginPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceSdCardListPresenter;
import net.bjyfkj.caa.mvp.view.IDeviceDownLoadVideoView;
import net.bjyfkj.caa.mvp.view.IDeviceLoginView;
import net.bjyfkj.caa.mvp.view.IDeviceSdCardView;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.SharedPreferencesUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity implements IDeviceLoginView, IDeviceDownLoadVideoView, IDeviceSdCardView {


    @InjectView(R.id.videoview)
    VideoView videoview;
    private AlertDialog.Builder builder;
    private View view;
    private DeviceLoginPresenter deviceLoginPresenter;
    private DeviceDownLoadVideoPresenter deviceDownLoadVideoPresenter;
    private DeviceSdCardListPresenter deviceSdCardListPresenter;
    private EditText device_id;
    private List<VideoData.DataBean> videolist;//服务器视频列表
    private List<Map<String, String>> sdlist = new ArrayList<Map<String, String>>();


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
        videoview.setMediaController(new MediaController(this));
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        deviceLoginPresenter = new DeviceLoginPresenter(this);
        deviceDownLoadVideoPresenter = new DeviceDownLoadVideoPresenter(this);
        deviceSdCardListPresenter = new DeviceSdCardListPresenter(this);
        deviceLoginPresenter.login();
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

    //是否登录成功
    @Override
    public void isdialog(String deviceid, boolean isSuccess) {
        if (isSuccess) {
            SharedPreferencesUtils.setParam(x.app(), LoginId.DEVICELOGINSTATE, deviceid + "");
            JPushUtil.setAlias(x.app(), "d" + deviceid);
            deviceLoginPresenter.getVideoPlay();
        } else {
            builderShow();
        }
    }

    //获取服务器视频列表
    @Override
    public void getVideoPlayList(List<VideoData.DataBean> list) {
        videolist = list;
        Toast.makeText(x.app(), "CAAlist - " + list.size(), Toast.LENGTH_SHORT).show();
        deviceDownLoadVideoPresenter.downLoadVideo();
    }

    /***
     * 弹出输入框
     */
    public void builderShow() {
        SharedPreferencesUtils.setParam(x.app(), LoginId.DEVICELOGINSTATE, "");
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_view, null);
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请输入设备ID号");
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("CAAEditText", device_id.getText().toString() + "");
                deviceLoginPresenter.login();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //下载视频成功
    @Override
    public void downLoadSuccess() {
        Log.i("CAAdownLoadSuccess -- ", "下载成功");
        deviceSdCardListPresenter.getSdCardVideoList();

    }

    //下载视频失败
    @Override
    public void downLoadFailed() {
        Log.i("CAAdownLoadFailed -- ", "下载失败");
        deviceDownLoadVideoPresenter.downLoadVideo();

    }

    //下载全部完成
    @Override
    public void downloadAll() {
        deviceSdCardListPresenter.getSdCardVideoList();
    }


    /**
     * EventBus 接收广播收到的信息
     *
     * @param jpush
     */
    public void onEventMainThread(JPushEventBus jpush) {
        Log.i("CAAonEventMainThread --", jpush.message.toString() + "");
        deviceLoginPresenter.getVideoPlay();
    }

    //拿到服务器视频列表去下载
    @Override
    public List<VideoData.DataBean> VideoPlayList() {
        return videolist;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Login.logout();
    }

    //获取sdcard视频列表
    @Override
    public void getSdCardVideoList(List<Map<String, String>> list) {
        sdlist.clear();
        sdlist = list;
        for (Map<String, String> stringStringMap : sdlist) {
            Map<String, String> map = new HashMap<String, String>();
            map = stringStringMap;
            String path = map.get("path");
            playVideo(path);
        }
    }

    //播放视频
    public void playVideo(String Path) {
        Log.i("CAAplayVideo -path ", Path + "");

        Uri uri = Uri.parse(Path);
        videoview.setVideoURI(uri);
        videoview.start();
    }


    @Override
    public void SdCardDirectory() {
        Log.i("CAASdCardDirectory --", "文件夹为空");
    }
}