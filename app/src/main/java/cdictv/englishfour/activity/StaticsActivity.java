package cdictv.englishfour.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cdictv.englishfour.R;
import cdictv.englishfour.config.AppConfig;
import cdictv.englishfour.javabean.Collect;
import cdictv.englishfour.javabean.Userinfo;
import cdictv.englishfour.utils.DataUtil;
import cdictv.englishfour.utils.SavePassword;

public class StaticsActivity extends AppCompatActivity {

    @InjectView(R.id.tv_sub_right)
    TextView mTvSubRight;
    @InjectView(R.id.tv_sub_wrong)
    TextView mTvSubWrong;
    @InjectView(R.id.tv_sub_already)
    TextView mTvSubAlready;
    @InjectView(R.id.tv_test_right)
    TextView mTvTestRight;
    @InjectView(R.id.tv_test_wrong)
    TextView mTvTestWrong;
    @InjectView(R.id.tv_test_already)
    TextView mTvTestAlready;
    @InjectView(R.id.tv_collect)
    TextView mTvCollect;
    @InjectView(R.id.tv_wrong)
    TextView mTvWrong;
    @InjectView(R.id.tv_sub_right_rate)
    TextView mTvSubRightRate;
    @InjectView(R.id.tv_sub_wrong_rate)
    TextView mTvSubWrongRate;
    @InjectView(R.id.tv_sub_already_rate)
    TextView mTvSubAlreadyRate;
    @InjectView(R.id.tv_test_right_rate)
    TextView mTvTestRightRate;
    @InjectView(R.id.tv_test_wrong_rate)
    TextView mTvTestWrongRate;
    @InjectView(R.id.tv_test_already_rate)
    TextView mTvTestAlreadyRate;
    @InjectView(R.id.user_school)
    TextView userSchool;
    @InjectView(R.id.user_name)
    TextView userName;
    @InjectView(R.id.user_gender)
    TextView userGender;
    @InjectView(R.id.user_phone)
    TextView userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics);
        ButterKnife.inject(this);
        initView();
        show();
    }

    /*
     * 显示个人信息
     * */
    private void show() {
        String name,gender,phone,school;
        List<Userinfo> mList= LitePal.select("phone","name","gender","school")
                .where("phone="+ SavePassword.getUserInfo(StaticsActivity.this).get("phone"))
                .find(Userinfo.class);
        DataUtil mData=new DataUtil(mList);

//        获取数据库中保存的数据
        name=mData.getUserData().getName();
        gender=mData.getUserData().getGender();
        phone=mData.getUserData().getPhone();
        LogUtils.a("gender",gender);

        school=mData.getUserData().getSchool();
//将数据显示在界面上
        userName.setText(name);
        userGender.setText(gender);
        userPhone.setText(phone);
        userSchool.setText(school);
    }


    /**
     * 加载界面
     */
    private void initView() {
        //客观题练习情况
        mTvSubRight.setText("答对了：" + ChapterexericseActivity.rightResult);
        if (ChapterexericseActivity.rightResult + ChapterexericseActivity.wrongResult != 0) {
            mTvSubRightRate.setText("正确率" + ChapterexericseActivity.rightResult * 100 / (ChapterexericseActivity.rightResult + ChapterexericseActivity.wrongResult) + "%");
        }
        mTvSubWrong.setText("答错了：" + ChapterexericseActivity.wrongResult);
        if (ChapterexericseActivity.rightResult + ChapterexericseActivity.wrongResult != 0) {
            mTvSubWrongRate.setText("错误率" +ChapterexericseActivity.wrongResult * 100 / (ChapterexericseActivity.rightResult + ChapterexericseActivity.wrongResult) + "%");
        }
        mTvSubAlready.setText("已答题：" + (ChapterexericseActivity.rightResult + ChapterexericseActivity.wrongResult));
        mTvSubAlreadyRate.setText("答题率：" + (ChapterexericseActivity.rightResult + ChapterexericseActivity.wrongResult) * 100 / 184 + "%");
        //模拟考试情况
        mTvTestRight.setText("答对了：" + TestActivity.right_total);
        if (TestActivity.right_total + TestActivity.wrong_total != 0) {
            mTvTestRightRate.setText("正确率" + TestActivity.right_total * 100 / (TestActivity.right_total + TestActivity.wrong_total) + "%");
        }
        mTvTestWrong.setText("答错了：" + TestActivity.wrong_total);
        if (TestActivity.right_total + TestActivity.wrong_total != 0) {
            mTvTestWrongRate.setText("错误率" + TestActivity.wrong_total * 100 / (TestActivity.right_total + TestActivity.wrong_total) + "%");
        }
        mTvTestAlready.setText("已答题：" + (TestActivity.right_total + TestActivity.wrong_total));
        mTvTestAlreadyRate.setText("答题率：" + (TestActivity.right_total + TestActivity.wrong_total) * 100 / 25 + "%");
        //收藏情况
        //我的收藏
        List<Collect> infosList = DataSupport.findAll(Collect.class);
        mTvCollect.setText("我的收藏:" + infosList.size());
        //我的错题
        List<Long> wrongId = new Gson().fromJson(SPUtils.getInstance().getString(AppConfig.WRONG_JSON), new TypeToken<List<Long>>() {
        }.getType());
        mTvWrong.setText(wrongId == null ? "我的错题：" + 0 : "我的错题：" + wrongId.size());
    }
}
