package cdictv.englishfour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.config.AppConfig;
import cdictv.englishfour.javabean.HistoryBean;

public class ResultActivity extends AppCompatActivity {
    @InjectView(R.id.tv_score)
    TextView mTvScore;
    @InjectView(R.id.tv_total)
    TextView mTvTotal;
    @InjectView(R.id.tv_already)
    TextView mTvAlready;
    @InjectView(R.id.tv_unalready)
    TextView mTvUnalready;
    @InjectView(R.id.tv_rigth)
    TextView mTvRigth;
    @InjectView(R.id.tv_costtime)
    TextView mTvCosttime;
    @InjectView(R.id.tv_step)
    TextView mTvStep;

    long costTime;//花费时间
    int right_total;//对的
    int wrong_total;//错的
    int total;//总共

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.inject(this);
        costTime = getIntent().getLongExtra("costTime", 0);
        right_total = getIntent().getIntExtra("right_total", 0);
        wrong_total = getIntent().getIntExtra("wrong_total", 0);
        total = getIntent().getIntExtra("total", 0);

        sava();//保存成绩
        initView();
    }

    /**
     * 保存成绩
     */
    private void sava() {
        Gson gson = new Gson();
        List<HistoryBean> list = new ArrayList<>();

        LogUtils.a("historyjson--->" + SPUtils.getInstance().getString("historyList", ""));
        //把存在本地的历史成绩json转换集合
        if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(AppConfig.HISTORY_LIST, ""))) {
            //返回的hisjson不为空就去拿json
            list = gson.fromJson(SPUtils.getInstance().getString(AppConfig.HISTORY_LIST, ""), new TypeToken<List<HistoryBean>>() {
            }.getType());
        }

        HistoryBean historyBean = new HistoryBean();
        //花费的时间
        historyBean.costTime = (costTime / 60 / 1000) + "分" + costTime / 1000 % 60 + "秒";
        //获取当前时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        historyBean.date = date;
        //分数
        historyBean.score = String.valueOf(4 * right_total);
        //添加进入数组
        list.add(historyBean);
        String json = gson.toJson(list);
        LogUtils.a("json--->" + json);
        SPUtils.getInstance().put(AppConfig.HISTORY_LIST, json);
    }

    private void initView() {
        mTvScore.setText("得分:" + 4 * right_total + "分");
        mTvTotal.setText("本次考试共" + total + "题");
        mTvAlready.setText("已答：" + (right_total + wrong_total) + "题");
        mTvUnalready.setText("未答：" + (total - right_total - wrong_total) + "题");
        mTvRigth.setText("答对：" + (right_total) + "题");
        mTvCosttime.setText("耗时：" + (costTime / 60 / 1000) + "分" + costTime / 1000 % 60 + "秒");
        if (4 * right_total == 100) {
            mTvStep.setText("太牛逼了,满分");
        } else if (4 * right_total >= 80 && 4 * right_total < 100) {
            mTvStep.setText("真棒啊~");
        } else if (4 * right_total >= 60 && 4 * right_total < 80) {
            mTvStep.setText("需要多努力啊~");
        } else {
            mTvStep.setText("考试不及格，等着补考吧~");
        }
    }

    @OnClick(R.id.tv_look_wrong)
    public void onClick() {
        startActivity(new Intent(this,TestWrongActivity.class));
    }
}

