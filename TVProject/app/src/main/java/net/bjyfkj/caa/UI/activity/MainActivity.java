package net.bjyfkj.caa.UI.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.autolayout.AutoLayoutActivity;

import net.bjyfkj.caa.R;
import net.bjyfkj.caa.UI.adapter.CarouselPagerAdapter;
import net.bjyfkj.caa.constant.LoginId;
import net.bjyfkj.caa.entity.AdsPlayData;
import net.bjyfkj.caa.entity.DanmakuItemData;
import net.bjyfkj.caa.entity.VideoData;
import net.bjyfkj.caa.eventBus.GetAdsPlayListEventBus;
import net.bjyfkj.caa.eventBus.JPushEventBus;
import net.bjyfkj.caa.eventBus.UpdateVideoEventBus;
import net.bjyfkj.caa.model.CarouselViewPager;
import net.bjyfkj.caa.util.Login;
import net.bjyfkj.caa.model.ViewPagerScroller;
import net.bjyfkj.caa.model.opendanmaku.DanmakuItem;
import net.bjyfkj.caa.model.opendanmaku.DanmakuView;
import net.bjyfkj.caa.model.opendanmaku.IDanmakuItem;
import net.bjyfkj.caa.mvp.presenter.DeviceDownLoadVideoPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceLoginPresenter;
import net.bjyfkj.caa.mvp.presenter.DeviceSdCardListPresenter;
import net.bjyfkj.caa.mvp.view.IDeviceDownLoadVideoView;
import net.bjyfkj.caa.mvp.view.IDeviceLoginView;
import net.bjyfkj.caa.mvp.view.IDeviceSdCardView;
import net.bjyfkj.caa.service.getAdsPlayListService;
import net.bjyfkj.caa.util.AppUpdate;
import net.bjyfkj.caa.util.DanmakuUtil;
import net.bjyfkj.caa.util.GsonUtils;
import net.bjyfkj.caa.util.JPushUtil;
import net.bjyfkj.caa.util.NetworkUtils;
import net.bjyfkj.caa.util.NoConnectedJsonUtil;
import net.bjyfkj.caa.util.PollingUtils;
import net.bjyfkj.caa.util.SharedPreferencesUtils;
import net.bjyfkj.caa.util.StringUtil;
import net.bjyfkj.caa.util.TimeUtil;
import net.bjyfkj.caa.util.getAdsPlayListUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AutoLayoutActivity implements IDeviceLoginView, IDeviceDownLoadVideoView, IDeviceSdCardView {


    @InjectView(R.id.videoview)
    VideoView videoview;
    @InjectView(R.id.mCarouselView)
    CarouselViewPager mCarouselView;
    @InjectView(R.id.flytxtview)
    TextView flytxtview;
    @InjectView(R.id.qrcode)
    ImageView qrcode;
    @InjectView(R.id.danmakuView)
    DanmakuView danmakuView;
    @InjectView(R.id.content)
    TextView content;
    @InjectView(R.id.item_img)
    ImageView itemImg;
    @InjectView(R.id.description)
    TextView description;
    @InjectView(R.id.finger)
    SimpleDraweeView finger;
    private AlertDialog.Builder builder;
    private View view;
    private DeviceLoginPresenter deviceLoginPresenter;//MVP
    private DeviceDownLoadVideoPresenter deviceDownLoadVideoPresenter;//MVP
    private DeviceSdCardListPresenter deviceSdCardListPresenter;//MVP
    private EditText device_id;
    private List<VideoData.DataBean> videolist;//服务器视频列表
    private List<Map<String, String>> sdlist = new ArrayList<Map<String, String>>();
    private int videoposition = 0;//
    private int playlistposition = 0;
    private int adsposition = 0;//广告下标13
    private List<ImageView> ivList;//图片轮播控件列表
    private List<AdsPlayData.DataBean> adslist = new ArrayList<>();//广告列表
    private boolean isRotation = false;//是否正在轮播
    private String strimage[] = null;//轮播图片列表
    private CarouselPagerAdapter carouselPagerAdapter;
    private int imageposition = 0;
    private List<IDanmakuItem> danmakuItemList;//弹幕列表
    private boolean isPlaying = false; //视频是否正在播放
    private boolean adscount = true;//判断当前广告列表是否存在当前时间段的广告
    private ImageView iv;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//注册Fresco0
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏
        setContentView(R.layout.activity_main1);//加载布局
        ButterKnife.inject(this);//注册ButterKnife
        EventBus.getDefault().register(this);//注册EventBus
        danmakuView.show();//显示弹幕
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NetworkUtils.isConnected(this)) {
            JPushInterface.onPause(x.app());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isConnected(this)) {
            JPushInterface.onResume(x.app());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (NetworkUtils.isConnected(this)) {
            PollingUtils.stopPollingService(this, getAdsPlayListService.class, getAdsPlayListService.ACTION);
            Login.logout();
        }

    }

    /***
     * 初始化
     */
    public void init() {
        Uri uri = Uri.parse("http://pics.sc.chinaz.com/Files/pic/faces/4360/01.gif");
        DraweeController dc = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
        finger.setController(dc);
        videoview.setMediaController(new MediaController(this));//
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_login_view, null);
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        deviceLoginPresenter = new DeviceLoginPresenter(this);
        deviceDownLoadVideoPresenter = new DeviceDownLoadVideoPresenter(this);
        deviceSdCardListPresenter = new DeviceSdCardListPresenter(this);
        if (NetworkUtils.isConnected(this)) {
            PollingUtils.startPollingService(this, 300, getAdsPlayListService.class, getAdsPlayListService.ACTION);
            deviceLoginPresenter.login();
        } else {
            deviceSdCardListPresenter.getSdCardVideoList();
            if (NoConnectedJsonUtil.noCongetAdsList(SharedPreferencesUtils.getParam(this, LoginId.JSONCACHE, "").toString()) != null) {
                adslist = NoConnectedJsonUtil.noCongetAdsList(SharedPreferencesUtils.getParam(this, LoginId.JSONCACHE, "").toString());
                initimager();
            }
        }

    }

    /**
     * * 图片轮播
     */
    private void initimager() {
        if (strimage != null) {
            for (int i = 0; i < strimage.length; i++) {
                strimage[i] = null;
            }
            strimage = null;
        }
        if (carouselPagerAdapter != null) {
            carouselPagerAdapter = null;
        }
        if (adsposition == adslist.size()) {
            adsposition = 0;
            if (!adscount) {//如果当前广告列表没有当前时间段要播放的广告 则停止
                return;
            }
        }
        if (ivList != null) {
            ivList = null;
        }
        final AdsPlayData.DataBean adsData = adslist.get(adsposition);//广告数据
        if (TimeUtil.isNEWDay(adsData.getTime())) { //判断是否是今天
            if (!TimeUtil.initWeclomeText().equals(adsData.getType())) {//判断是否是当前时间段
                adsposition++;
                initimager();
            } else {
                adscount = true;
                if (NetworkUtils.isConnected(x.app())) {
                    getAdsPlayListUtil.setPlayCount(adsData.getId());//广告自增
                }
                Log.i("adsPlay_id", adsData.getTime() + "");
                description.setText(adsData.getDescription());
                flytxtview.setText("本视频由\"" + adsData.getShop_name() + "\"特约播出 (" + adsData.getShop_address() + ")");
                content.setText(adsData.getContent());
                ivList = new ArrayList<ImageView>();
                strimage = StringUtil.StringSplit(adsData.getImglist());
                for (int i = 0; i < strimage.length; i++) {
                    if (iv == null) {
                        iv = new ImageView(getApplicationContext());
                    }
                    Glide.with(getApplicationContext())
                            .load(strimage[i])
                            .into(iv);
                    ivList.add(iv);
                }
                Glide.with(x.app()).load(adsData.getQrcode()).into(qrcode);
                Glide.with(x.app()).load(adsData.getItem_img()).into(itemImg);
                carouselPagerAdapter = new CarouselPagerAdapter(ivList);
                int width = mCarouselView.getWidth();
                int height = mCarouselView.getHeight();
                Log.i("width", width + "");
                Log.i("height", height + "");
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
                            //pager 正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
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
        } else {
            adsposition++;
            initimager();
        }

    }

    /**
     * app自动更新
     */
    public void appUpdate() {
        if (NetworkUtils.isConnected(MainActivity.this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppUpdate app = new AppUpdate(MainActivity.this);
                    Log.i("AppUpdate", "检查应用版本号");
                    app.checkUpdateForTV(new AppUpdate.OnResult() {
                        @Override
                        public void onNewVersion() {
                            Log.i("onNewVersion", "app有新版本");
                        }

                        @Override
                        public void onLatestVersion() {
                            Log.i("onLatestVersion", "app已是最新版本");
                        }

                        @Override
                        public void onDownloading(long current, long total) {
                            Log.i("onDownloading", "app正在下载");
                        }

                        @Override
                        public void onDownLoaCompleted() {
                            Log.i("onDownLoaCompleted", "app下载成功");
                        }

                        @Override
                        public void onError() {
                            Log.i("onError", "app下载失败");
                        }
                    });
                }
            }).start();
        } else {
            Log.i("AppUpdate", "网络没有连接");
        }
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
            appUpdate();
            SharedPreferencesUtils.setParam(x.app(), LoginId.DEVICELOGINSTATE, deviceid + "");
            JPushUtil.setAlias(x.app(), "d" + deviceid);
            deviceLoginPresenter.updateDeviceTime();
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
        if (isPlaying) {
            isPlaying = false;
        }
        deviceDownLoadVideoPresenter.downLoadVideo();
    }

    /***
     * 弹出输入框
     */

    public void builderShow() {
        SharedPreferencesUtils.setParam(x.app(), LoginId.DEVICELOGINSTATE, "");
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout
                .alert_login_view, null);
        device_id = (EditText) view.findViewById(R.id.edt_userid);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请输入设备ID号");
        builder.setCancelable(false);
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
        if (playlistposition < videolist.size() - 1) {
            playlistposition += 1;
            deviceDownLoadVideoPresenter.downLoadVideo();
        } else {
            Log.i("getSdCardVideoList", "getSdCardVideoList");
            playlistposition = 0;
        }
        deviceSdCardListPresenter.getSdCardVideoList();
    }

    //下载视频失败
    @Override
    public void downLoadFailed() {
        Log.i("CAAdownLoadFailed -- ", "下载失败");
        deviceDownLoadVideoPresenter.downLoadVideo();

    }

    /**
     * 获取SD卡视频列表
     *
     * @param updateVideoEventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateVideo(UpdateVideoEventBus updateVideoEventBus) {
        if (updateVideoEventBus.isupdate) {
            deviceSdCardListPresenter.getSdCardVideoList();
        }
    }

    /**
     * EventBus 接收广播收到的信息
     *
     * @param jpush
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final JPushEventBus jpush) {
        Log.i("CAAonEventMainThread --", jpush.message.toString() + "");
        try {
            JSONObject jsonObject = new JSONObject(jpush.message);
            String cmd = jsonObject.getString("cmd");
            switch (cmd) {
                case "video_playlist_changed":
                    Log.i("video_playlist_changed", "视频更新了");
                    deviceLoginPresenter.getVideoPlay();
                    break;
                case "promotion_get":
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("promotion_get", "弹幕更新了" + jpush.message);
                            danmakuItemList = new ArrayList<>();
                            DanmakuItemData danmakuItemData = GsonUtils.json2Bean(jpush.message, DanmakuItemData.class);
                            List<DanmakuItemData.DataBean> list = danmakuItemData.getData();
                            list = DanmakuUtil.Danmaku(list);//筛选重复的弹幕
                            for (DanmakuItemData.DataBean dataBean : list) {
                                if (dataBean.getDevice_id().equals(SharedPreferencesUtils.getParam(x.app(), LoginId.DEVICELOGINSTATE, "").toString())) {
                                    IDanmakuItem item = new DanmakuItem(MainActivity.this, "扫码领取(" + dataBean.getContent() + ")成功 " + dataBean.getNickname(), danmakuView.getWidth());
                                    danmakuItemList.add(item);
                                }
                            }
                            danmakuView.addItem(danmakuItemList, true);//弹幕列表
                            danmakuView.show();
                            Log.i("danmakuItemList", "弹幕数量" + danmakuItemList.size());
                        }
                    }).start();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //拿到服务器视频列表去下载
    @Override
    public String VideoPlayPath() {
        return videolist.get(playlistposition).getUrl();
    }

    //获取sdcard视频列表
    @Override
    public void getSdCardVideoLists(List<Map<String, String>> list) {
        Log.i("asd", "获取sdcard视频列表" + list.size());
        sdlist = null;
        sdlist = list;
        if (sdlist.size() > 0) {
            if (!isPlaying) {
                playVideo();
            }
        }
    }

    //播放视频
    public void playVideo() {
        isPlaying = true;
        if (videoposition > sdlist.size() - 1) {
            videoposition = 0;
        }
        Map<String, String> map = sdlist.get(videoposition);
        final String Path = map.get("path");
        Log.i("CAAplayVideo -path ", Path + "");
        videoview.setVideoPath(Path);
        videoview.start();
        videoposition++;
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                deviceSdCardListPresenter.getSdCardVideoList();
                isPlaying = false;
                playVideo();
            }
        });
    }

    //获取广告信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAdsPlayList(GetAdsPlayListEventBus getAdsPlayListEventBus) {
        adscount = true;
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
