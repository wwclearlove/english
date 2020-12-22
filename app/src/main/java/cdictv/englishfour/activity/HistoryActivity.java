package cdictv.englishfour.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.adapter.HisAdapter;
import cdictv.englishfour.config.AppConfig;
import cdictv.englishfour.javabean.HistoryBean;

import static org.litepal.LitePalApplication.getContext;

public class HistoryActivity extends AppCompatActivity {

    @InjectView(R.id.rv)
    RecyclerView mRv;
    HisAdapter mHisAdapter;
    @InjectView(R.id.tv_state)
    TextView mTvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.inject(this);
        initRv();
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        //得到数据
        List<HistoryBean> list = new ArrayList<>();
        //历史有成绩
        if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(AppConfig.HISTORY_LIST, ""))) {
            mRv.setVisibility(View.VISIBLE);
            mTvState.setVisibility(View.GONE);
            //返回的hisjson不为空就去拿json
            list = new Gson().fromJson(SPUtils.getInstance().getString(AppConfig.HISTORY_LIST, ""), new TypeToken<List<HistoryBean>>() {
            }.getType());

            mRv.setLayoutManager(new LinearLayoutManager(getContext()));
            mHisAdapter = new HisAdapter(R.layout.his_item, list);
            mRv.setAdapter(mHisAdapter);
        }
        //历史没有成绩
        else {
            mTvState.setVisibility(View.VISIBLE);
            mRv.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.tv_his_clear)
    public void onClick() {

        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否清除历史成绩")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //清除历史记录成绩单
                        SPUtils.getInstance().clear(true);
                        initRv();
                    }
                })
                .setNegativeButton("取消",null)
                .show();

    }
}
