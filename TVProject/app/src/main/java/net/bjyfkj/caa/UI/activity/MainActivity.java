package net.bjyfkj.caa.UI.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

import net.bjyfkj.caa.R;
import net.bjyfkj.caa.UI.adapter.CarouselPagerAdapter;
import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.entity.AdsPlayData;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.eventBus.GetAdsPlayListEventBus;
import net.bjyfkj.caa.eventBus.JPushEventBus;
import net.bjyfkj.caa.model.AlwaysMarqueeTextView;
import net.bjyfkj.caa.model.CarouselViewPager;
import net.bjyfkj.caa.model.Login;
import net.bjyfkj.caa.model.ViewPagerScroller;
import net.bjyfkj.caa.mvp.presenter.DeviceDownLoadVideoPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceLoginPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceSdCardListPresenter;
import net.bjyfkj.caa.mvp.view.IDeviceDownLoadVideoView;
import net.bjyfkj.caa.mvp.view.IDeviceLoginView;
import net.bjyfkj.caa.mvp.view.IDeviceSdCardView;
import net.bjyfkj.caa.service.getAdsPlayListService;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.PollingUtils;
import net.bjyfkj.caa.util.SharedPreferencesUtils;
import net.bjyfkj.caa.util.StringUtil;
import net.bjyfkj.caa.util.TimeUtil;
import net.bjyfkj.caa.util.getAdsPlayListUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends FragmentActivity implements IDeviceLoginView, IDeviceDownLoadVideoView, IDeviceSdCardView {


    @InjectView(R.id.videoview)
    VideoView videoview;
    @InjectView(R.id.mCarouselView)
    CarouselViewPager mCarouselView;
    @InjectView(R.id.flytxtview)
    TextView flytxtview;
    @InjectView(R.id.led1)
    AlwaysMarqueeTextView led1;
    @InjectView(R.id.led2)
    AlwaysMarqueeTextView led2;
    @InjectView(R.id.qrcode)
    ImageView qrcode;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.content)
    TextView content;


    private AlertDialog.Builder builder;
    private View view;
    private DeviceLoginPresenter deviceLoginPresenter;
    private DeviceDownLoadVideoPresenter deviceDownLoadVideoPresenter;
    private DeviceSdCardListPresenter deviceSdCardListPresenter;
    private EditText device_id;
    private List<VideoData.DataBean> videolist;//服务器视频列表
    private List<Map<String, String>> sdlist = new ArrayList<Map<String, String>>();
    private boolean isplayvideo = true;//是否正在播放视频
    private int videoposition = 0;//
    private int playlistposition = 0;
    private int adsposition = 0;//广告下标
    private List<ImageView> ivList;//图片轮播控件列表
    private List<AdsPlayData.DataBean> adslist;//广告列表
    private boolean isRotation = false;//是否正在轮播
    private String strimage[] = null;//轮播图片列表
    private CarouselPagerAdapter carouselPagerAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.inject(this);
        init();

    }

    /***
     * 初始化
     */
    public void init() {

        PollingUtils.startPollingService(this, 300, getAdsPlayListService.class, getAdsPlayListService.ACTION);
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_view, null);
        videoview.setMediaController(new MediaController(this));
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        deviceLoginPresenter = new DeviceLoginPresenter(this);
        deviceDownLoadVideoPresenter = new DeviceDownLoadVideoPresenter(this);
        deviceSdCardListPresenter = new DeviceSdCardListPresenter(this);
        deviceLoginPresenter.updateDeviceTime();
        deviceLoginPresenter.login();
    }

    /***
     * 图片轮播
     */
    int imageposition = 0;

    private void initimager() {
        if (strimage != null) {
            strimage = null;
        }
        if (carouselPagerAdapter != null) {
            carouselPagerAdapter = null;
        }
        if (adsposition == adslist.size()) {
            adsposition = 0;
        }
        if (ivList != null) {
            ivList = null;
        }
        final AdsPlayData.DataBean adsData = adslist.get(adsposition);//广告数据
        if (!TimeUtil.initWeclomeText().equals(adsData.getType())) {
            adsposition++;
            initimager();
        }
        getAdsPlayListUtil.setPlayCount(adsData.getId());//广告自增
        Glide.with(x.app()).load(adsData.getQrcode()).into(qrcode);
        title.setText(adsData.getTitle());
        content.setText(adsData.getContent());
        led1.setText(adsData.getShop_name());
        led2.setText(adsData.getShop_address());
        ivList = new ArrayList<ImageView>();
        strimage = StringUtil.StringSplit(adsData.getImglist());
        for (int i = 0; i < strimage.length; i++) {
            ImageView iv = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext())
                    .load(strimage[i])
                    .into(iv);
            ivList.add(iv);
        }
        carouselPagerAdapter = new CarouselPagerAdapter(ivList);
        mCarouselView.setAdapter(carouselPagerAdapter);
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
                    imageposition++;
                    if (strimage.length == imageposition) {
                        imageposition = 0;
                        adsposition++;
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JPushEventBus jpush) {
        Log.i("CAAonEventMainThread --", jpush.message.toString() + "");
        deviceLoginPresenter.getVideoPlay();
    }

    //拿到服务器视频列表去下载
    @Override
    public String VideoPlayPath() {
        return videolist.get(videoposition).getUrl();
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
        PollingUtils.stopPollingService(this, getAdsPlayListService.class, getAdsPlayListService.ACTION);
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
        Map<String, String> map = sdlist.get(videoposition);
        videoposition++;
        final String Path = map.get("path");
        Log.i("CAAplayVideo -path ", Path + "");
        videoview.setVideoPath(Path);
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (videoposition >= sdlist.size()) {
                    videoposition = 0;
                }
                playVideo();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAdsPlayList(GetAdsPlayListEventBus getAdsPlayListEventBus) {
        adslist = getAdsPlayListEventBus.result;
        if (!isRotation) {
            isRotation = true;
            initimager();
        }
    }


    @Override
    public void SdCardDirectory() {
        Log.i("CAASdCardDirectory --", "文件夹为空");
    }
}
