package cdictv.englishfour.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.allure.lbanners.LMBanners;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.adapter.LocalImgAdapter;
import cdictv.englishfour.config.AppConfig;
import cdictv.englishfour.javabean.Collect;
import cdictv.englishfour.javabean.Userinfo;
import cdictv.englishfour.service.Iservice;
import cdictv.englishfour.service.MusicService;
import cdictv.englishfour.utils.DataUtil;
import cdictv.englishfour.utils.MyDialog;
import cdictv.englishfour.utils.SavePassword;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.lmbanners)
    LMBanners mBanners;
    //判断是否双击退出
    private boolean isExit;
    private int tag=0;
    //双击退出的 handler
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = true;
        }
    };

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            System.exit(0);
        } else {
            ToastUtils.showShort("再点击一次就退出应用");
            isExit = true;
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }
    private Iservice iservice;
    private Myconn conn;
    @SuppressLint("HandlerLeak")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //混合方式开启服务
        //[1]先调用srartservic 目的保证服务在后台才起运行
        Intent intent=new Intent(this,MusicService.class);
        startService(intent);
        conn = new Myconn();
        //[2]调用bandiservice 目的为了获取我们定义的中间人对象 就可以调用服务里的方法
        bindService(intent, conn,BIND_AUTO_CREATE);
        initBanner();
    }

    private void initBanner() {
        /**
         * 第一个参数是我们的适配器，第二个参数是我们传入的图片
         */
        List mList = new ArrayList();
        mList.add(R.drawable.banner2);
        mList.add(R.drawable.banner3);
        mList.add(R.drawable.banner4);
        mBanners.setAdapter(new LocalImgAdapter(this), mList);
    }

    //停止事件,节省资源
    @Override
    protected void onPause() {
        super.onPause();
        mBanners.stopImageTimerTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBanners.startImageTimerTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBanners.clearImageTimerTask();
    }
    //监听服务状态
    private  class  Myconn implements ServiceConnection {
        //服务监听成功
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //获取binder对象
            iservice = (Iservice) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
    /**
     * 点击事件
     */
    @OnClick({R.id.lin_chapterexericse, R.id.lin_test, R.id.lin_history, R.id.lin_message, R.id.lin_collect
            , R.id.lin_error, R.id.lin_statics})
    public void onClick(View view) {
        switch (view.getId()) {
            //客观题型
            case R.id.lin_chapterexericse:
                Intent chapterexericseIntent = new Intent(this, ChapterexericseActivity.class);
                startActivity(chapterexericseIntent);
                break;
            //模拟考试
            case R.id.lin_test:
                List<Userinfo> mList = LitePal.select("phone", "name").where("phone=" + "'" + SavePassword.getUserInfo(MainActivity.this).get("phone") + "'").find(Userinfo.class);
                DataUtil mData = new DataUtil(mList);
                String name = mData.getUserData().getName();

                final MyDialog myDialog = new MyDialog(MainActivity.this);
                myDialog.setTitle("温馨提示");
                myDialog.setMessage("\"考试是随机25题精选考试，满分100分，考试时间是20分钟。\n\n" + "姓名：" + name);
                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Intent testIntent = new Intent(MainActivity.this, TestActivity.class);
                        startActivity(testIntent);
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
            //历史成绩
            case R.id.lin_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            //通知公告
            case R.id.lin_message:
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;
            //收藏
            case R.id.lin_collect:
                if (LitePal.findAll(Collect.class).size() > 0) {
                    Intent collectsIntent = new Intent(this, CollectActivity.class);
                    startActivity(collectsIntent);
                } else {
                    ToastUtils.showShort("收藏列表为空");
                }
                break;
            //错题
            case R.id.lin_error:
                if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(AppConfig.WRONG_JSON))) {
                    startActivity(new Intent(this, MyWrongActivity.class));
                } else {
                    ToastUtils.showShort("我的错题列表为空");
                }
                break;
            case R.id.lin_statics:
                startActivity(new Intent(this, StaticsActivity.class));
                break;


        }
    }


}

//http://v.juhe.cn/sms/send?mobile=15328561260&tpl_id=短信模板ID&tpl_value=%23code%23%3D654654&key=