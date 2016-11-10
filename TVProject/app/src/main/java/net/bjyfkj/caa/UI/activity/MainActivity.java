package net.bjyfkj.caa.UI.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import net.bjyfkj.caa.R;
import net.bjyfkj.caa.UI.adapter.CarouselPagerAdapter;
import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.eventBus.GetAdsPlayListEventBus;
import net.bjyfkj.caa.eventBus.JPushEventBus;
import net.bjyfkj.caa.model.CarouselViewPager;
import net.bjyfkj.caa.model.Login;
import net.bjyfkj.caa.model.ViewPagerScroller;
import net.bjyfkj.caa.mvp.presenter.DeviceDownLoadVideoPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceLoginPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceSdCardListPresenter;
import net.bjyfkj.caa.mvp.view.IDeviceDownLoadVideoView;
import net.bjyfkj.caa.mvp.view.IDeviceLoginView;
import net.bjyfkj.caa.mvp.view.IDeviceSdCardView;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.SharedPreferencesUtils;
import net.bjyfkj.caa.util.getAdsPlayListUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends FragmentActivity implements IDeviceLoginView, IDeviceDownLoadVideoView, IDeviceSdCardView {


    @InjectView(R.id.videoview)
    VideoView videoview;
    @InjectView(R.id.mCarouselView)
    CarouselViewPager mCarouselView;
    @InjectView(R.id.timer)
    TextView time;

    private AlertDialog.Builder builder;
    private View view;
    private DeviceLoginPresenter deviceLoginPresenter;
    private DeviceDownLoadVideoPresenter deviceDownLoadVideoPresenter;
    private DeviceSdCardListPresenter deviceSdCardListPresenter;
    private EditText device_id;
    private List<VideoData.DataBean> videolist;//服务器视频列表
    private List<Map<String, String>> sdlist = new ArrayList<Map<String, String>>();
    private boolean isplayvideo = true;
    private int position = 0;
    private int playlistposition = 0;
    private int recLen = 0;//倒计时时间
    private List<ImageView> ivList = new ArrayList<ImageView>();
    private int[] strimage = {R.drawable.a1024, R.drawable.a10241, R.drawable.a102430};
    private static final int UPDATE_TEXTVIEW = 0;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXTVIEW:
                    time.setText(recLen + "s");
                    break;
            }
        }
    };

    /***
     * timer定时器  开始
     */
    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    recLen--;
                }
            };
        }

        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, 1000, 1000);

    }

    /***
     * timer定时器  关闭
     */
    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.inject(this);
        initimager();
        init();

    }

    /***
     * 图片轮播
     */
    int ppp = 0;

    private void initimager() {
        recLen = (strimage.length) * (20 + 5) - 5;
        for (int i = 0; i < strimage.length; i++) {
            ImageView iv = new ImageView(getApplicationContext());
//            Glide.with(getApplicationContext())
//                    .load(strimage[i])
//                    .into(iv);
            iv.setImageResource(strimage[i]);
            ivList.add(iv);
        }

        mCarouselView.setAdapter(new CarouselPagerAdapter(ivList));
        mCarouselView.setDisplayTime(20000);
        ViewPagerScroller scroller = new ViewPagerScroller(getApplicationContext());
        scroller.setScrollDuration(5000);
        scroller.initViewPagerScroll(mCarouselView);
        mCarouselView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //正在滑动   pager处于正在拖拽中

                    Log.d("测试代码", "onPageScrollStateChanged=======正在滑动" + "SCROLL_STATE_DRAGGING");

                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                    Log.d("测试代码", "onPageScrollStateChanged=======自动沉降" + "SCROLL_STATE_SETTLING");
                    ppp++;
                    if (strimage.length == ppp) {
//
                        stopTimer();
                        ppp = 0;
                        initimager();
                    } else {
                        videoview.pause();
                    }
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //空闲状态  pager处于空闲状态
                    Log.d("测试代码", "onPageScrollStateChanged=======空闲状态" + "SCROLL_STATE_IDLE");
                    videoview.start();
                }
            }
        });
        mCarouselView.start();
        startTimer();
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
            deviceLoginPresenter.updateDeviceTime();
            getAdsPlayListUtil.getAdsPlayList();
        } else {
            builderShow();
        }
    }

    //获取服务器视频列表
    @Override
    public void getVideoPlayList(List<VideoData.DataBean> list) {
        videolist = list;
        Log.i("getVideoPlayList", "CAAlist - " + list.size());
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
        if (playlistposition < videolist.size()) {
            playlistposition += 1;
            deviceDownLoadVideoPresenter.downLoadVideo();
        } else {
            Log.i("getSdCardVideoList", "getSdCardVideoList");
            deviceSdCardListPresenter.getSdCardVideoList();
        }
    }

    //下载视频失败
    @Override
    public void downLoadFailed() {
        Log.i("CAAdownLoadFailed -- ", "下载失败");
        deviceDownLoadVideoPresenter.downLoadVideo();

    }

    /**
     * EventBus 接收广播收到的信息
     *
     * @param jpush
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(JPushEventBus jpush) {
        Log.i("CAAonEventMainThread --", jpush.message.toString() + "");
        deviceLoginPresenter.getVideoPlay();
    }

    //拿到服务器视频列表去下载
    @Override
    public String VideoPlayPath() {
        return videolist.get(position).getUrl();
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
        Log.i("asd", "获取sdcard视频列表");
        sdlist = null;
        sdlist = list;
        if (isplayvideo == true) {
            playVideo();
        }
    }

    //播放视频
    public void playVideo() {
        isplayvideo = false;
        Map<String, String> map = sdlist.get(position);
        position++;
        final String Path = map.get("path");
        Log.i("CAAplayVideo -path ", Path + "");
        videoview.setVideoPath(Path);
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (position >= sdlist.size()) {
                    position = 0;
                }
                playVideo();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getAdsPlayList(GetAdsPlayListEventBus getAdsPlayListEventBus) {
        Log.i("getAdsPlayList", getAdsPlayListEventBus.result + "");
//        Toast.makeText(x.app(), getAdsPlayListEventBus.result + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void SdCardDirectory() {
        Log.i("CAASdCardDirectory --", "文件夹为空");
    }
}
