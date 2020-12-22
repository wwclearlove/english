package cdictv.englishfour.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.config.AppConfig;
import cdictv.englishfour.javabean.Answer;
import cdictv.englishfour.javabean.Collect;

public class MyWrongActivity extends AppCompatActivity {

    @InjectView(R.id.tv_id)
    TextView mTvId;
    @InjectView(R.id.tv_types)
    TextView mTvTypes;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.tv_a)
    TextView mTvA;
    @InjectView(R.id.tv_b)
    TextView mTvB;
    @InjectView(R.id.tv_c)
    TextView mTvC;
    @InjectView(R.id.tv_d)
    TextView mTvD;
    @InjectView(R.id.tv_detail)
    TextView mTvDetail;
    @InjectView(R.id.tv_current_number)
    TextView mTvCurrentNumber;
    @InjectView(R.id.lin_detail)
    LinearLayout mLinDetail;
    @InjectView(R.id.iv_a)
    ImageView mIvA;
    @InjectView(R.id.lin_a)
    LinearLayout mLinA;
    @InjectView(R.id.iv_b)
    ImageView mIvB;
    @InjectView(R.id.lin_b)
    LinearLayout mLinB;
    @InjectView(R.id.iv_c)
    ImageView mIvC;
    @InjectView(R.id.lin_c)
    LinearLayout mLinC;
    @InjectView(R.id.iv_d)
    ImageView mIvD;
    @InjectView(R.id.lin_d)
    LinearLayout mLinD;
    @InjectView(R.id.tv_result)
    TextView mTvResult;
    @InjectView(R.id.lin_answer)
    LinearLayout mLinAnswer;
    @InjectView(R.id.lin_bt_unstar)
    LinearLayout mLinBtUnstar;
    @InjectView(R.id.lin_navigation_forward)
    LinearLayout mLinNavigationForward;
    @InjectView(R.id.im_sc)
    ImageView mImSc;

    private List<Answer> infosList;
    private int index = 0;//做到第几题
    private int right_total;//答对多少题
    private int wrong_total;//答错多少题
    private long[] longWrong;//错误id通过liepal传递

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wrong);
        ButterKnife.inject(this);
        fromJson();
        clearInfosReply();//清除历史做过的记录
        getInfosFromDB(index);//加载数据

    }

    /**
     * 所有的错题都从sp里面来解析
     */
    private void fromJson() {
        List<Long> wrongId = new Gson().fromJson(SPUtils.getInstance().getString(AppConfig.WRONG_JSON), new TypeToken<List<Long>>() {
        }.getType());
        longWrong = new long[wrongId.size()];
        for (int j = 0; j < wrongId.size(); j++) {
            longWrong[j] = wrongId.get(j);
        }
        LogUtils.a(longWrong.toString() + "  " + longWrong.length);
    }

    /**
     * 清除历史做过的记录
     */
    private void clearInfosReply() {
        infosList = LitePal.findAll(Answer.class);
        for (int i = 0; i < infosList.size(); i++) {
            Answer answer = new Answer();
            answer.setReply("0");
            answer.updateAll("id = ?", String.valueOf(i));
        }

    }

    /**
     * 从数据拿数据
     */
    private void getInfosFromDB(int i) {

        infosList = LitePal.findAll(Answer.class, longWrong);
        //加载界面
        //加载第几题
        mTvId.setText("第" + (index + 1) + "题");
        //加载题目类型
        mTvTypes.setText(infosList.get(i).getTypes());
        //加载题目
        mTvTitle.setText(infosList.get(i).getTimu_title());
        //加载选择
        mTvA.setText("A. " + infosList.get(i).getTimu1());
        mTvB.setText("B. " + infosList.get(i).getTimu2());
        mTvC.setText("C. " + infosList.get(i).getTimu3());
        mTvD.setText("D. " + infosList.get(i).getTimu4());
        //加载解析
        mTvDetail.setText(infosList.get(i).getDetail());
        //加载做到第几题
        mTvCurrentNumber.setText((i + 1) + "/" + infosList.size());
        //判断是否做过这题,返回0就是没有做过，返回1~4就是答案
        String done = infosList.get(i).getReply();
        if (!"0".equals(done)) {
            //去加载做过的题目答案
            doneWhich(done);
        }
        boolean flag= checkIsCollect();
        if (flag==true){
            mImSc.setImageResource(R.drawable.b_unstar2);
        }
    }


    @OnClick({R.id.lin_a, R.id.lin_b, R.id.lin_c, R.id.lin_d, R.id.lin_navigation_back, R.id.lin_answer, R.id.lin_bt_unstar, R.id.lin_navigation_forward})
    public void onClick(View view) {
        switch (view.getId()) {
            //上一题
            case R.id.lin_navigation_back:
                if (index == 0) {
                    ToastUtils.showShort("已经是第一题了");
                    return;
                }
                index--;
                clearInterface();//清除界面
                getInfosFromDB(index);//获取题目信息
                break;
            //下一题
            case R.id.lin_navigation_forward:
                if (index == infosList.size() - 1) {
                    ToastUtils.showShort("已经是最后一题了");
                    return;
                }

                index++;
                clearInterface();//清除界面
                getInfosFromDB(index);//获取题目信息
                break;
            //查看题解
            case R.id.lin_answer:
                mLinDetail.setVisibility(View.VISIBLE);
                break;
            //收藏
            case R.id.lin_bt_unstar:
                //如果该题已经收藏就不在收藏
                if (checkIsCollect()) {
                    return;
                }

                Collect collect = new Collect();
                collect.setId(Integer.parseInt(infosList.get(index).getId()));
                collect.setTimu_title(infosList.get(index).getTimu_title());
                collect.setTimu1(infosList.get(index).getTimu1());
                collect.setTimu2(infosList.get(index).getTimu2());
                collect.setTimu3(infosList.get(index).getTimu3());
                collect.setTimu4(infosList.get(index).getTimu4());
                collect.setDaan1(infosList.get(index).getDaan1());
                collect.setDaan2(infosList.get(index).getDaan2());
                collect.setDaan3(infosList.get(index).getDaan3());
                collect.setDaan4(infosList.get(index).getDaan4());
                collect.setDetail(infosList.get(index).getDetail());
                collect.setTypes(infosList.get(index).getTypes());
                collect.setReply(infosList.get(index).getReply());
                collect.save();
                mImSc.setImageResource(R.drawable.b_unstar2);
                ToastUtils.showShort("收藏成功");
                break;

            //选择A答案
            case R.id.lin_a:
                clickChoice("A");
                break;
            //选择B答案
            case R.id.lin_b:
                clickChoice("B");
                break;
            //选择C答案
            case R.id.lin_c:
                clickChoice("C");
                break;
            //选择D答案
            case R.id.lin_d:
                clickChoice("D");
                break;
        }
    }

    /**
     * 判断是否收藏过该题
     */
    private boolean checkIsCollect() {
        List<Collect> collectList = DataSupport.findAll(Collect.class);
        for (int i = 0; i < collectList.size(); i++) {

            if (collectList.get(i).getTimu_title().equals(infosList.get(index).getTimu_title())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击选择
     */
    private void clickChoice(String choice) {
        unClickAble(false);//选择过后就不能再点击
        //查询答案
        Answer answer = DataSupport.find(Answer.class, index + 1);
        String daan;//标准答案
        if (EmptyUtils.isNotEmpty(answer.getDaan1())) {
            daan = answer.getDaan1();
        } else if (EmptyUtils.isNotEmpty(answer.getDaan2())) {
            daan = answer.getDaan2();
        } else if (EmptyUtils.isNotEmpty(answer.getDaan3())) {
            daan = answer.getDaan3();
        } else if (EmptyUtils.isNotEmpty(answer.getDaan4())) {
            daan = answer.getDaan4();
        } else {
            daan = "";
        }

        LogUtils.a("第" + (index + 1) + "题的答案为：" + daan);
        //如果选择的和答案一致,选择的变绿，变勾
        if (choice.equals(daan)) {
            choiceRight(daan);
            right_total++;
        }
        //如果选择的和答案不一致，选择的变红，变×。把正确的显示出来
        else {
            choiceRight(daan);
            choiceWrong(choice);
            wrong_total++;
        }

        //做完一道题需要在数据库里面更新reply字段
        answer.setReply(choice);
        answer.updateAll("id = ?", String.valueOf(index + 1));


        mTvResult.setText("共答" + (right_total + wrong_total) + "题,答对" + right_total + "题，答错" + wrong_total + "题，正确率" + (right_total * 100 / (right_total + wrong_total)) + "%");
        mTvResult.setVisibility(View.VISIBLE);
    }

    /**
     * 选择过后就不能再点击
     */
    private void unClickAble(boolean flag) {
        mLinA.setClickable(flag);
        mLinB.setClickable(flag);
        mLinC.setClickable(flag);
        mLinD.setClickable(flag);
    }

    /**
     * 选择错误的表现
     */
    private void choiceWrong(String choice) {
        //如果选择的错误答案是A
        if ("A".equals(choice)) {
            mLinA.setBackgroundColor(getResources().getColor(R.color.wrong));
            mIvA.setImageResource(R.drawable.wrong);
        }
        //如果选择的错误答案是B
        else if ("B".equals(choice)) {
            mLinB.setBackgroundColor(getResources().getColor(R.color.wrong));
            mIvB.setImageResource(R.drawable.wrong);
        }
        //如果选择的错误答案是C
        else if ("C".equals(choice)) {
            mLinC.setBackgroundColor(getResources().getColor(R.color.wrong));
            mIvC.setImageResource(R.drawable.wrong);
        }
        //如果选择的错误答案是D
        else if ("D".equals(choice)) {
            mLinD.setBackgroundColor(getResources().getColor(R.color.wrong));
            mIvD.setImageResource(R.drawable.wrong);
        }


    }

    /**
     * 选择正确的表现
     */
    private void choiceRight(String daan) {
        //如果正确答案是A
        if ("A".equals(daan)) {
            mLinA.setBackgroundColor(getResources().getColor(R.color.right));
            mIvA.setImageResource(R.drawable.right);
        }
        //如果正确答案是B
        else if ("B".equals(daan)) {
            mLinB.setBackgroundColor(getResources().getColor(R.color.right));
            mIvB.setImageResource(R.drawable.right);
        }
        //如果正确答案是C
        else if ("C".equals(daan)) {
            mLinC.setBackgroundColor(getResources().getColor(R.color.right));
            mIvC.setImageResource(R.drawable.right);
        }
        //如果正确答案是D
        else if ("D".equals(daan)) {
            mLinD.setBackgroundColor(getResources().getColor(R.color.right));
            mIvD.setImageResource(R.drawable.right);
        }
    }

    /**
     * 清除正确错误
     */
    private void clearInterface() {
        mLinA.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mLinB.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mLinC.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mLinD.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mImSc.setImageResource(R.drawable.bt_unstar);
        mIvA.setImageResource(R.drawable.defaults);
        mIvB.setImageResource(R.drawable.defaults);
        mIvC.setImageResource(R.drawable.defaults);
        mIvD.setImageResource(R.drawable.defaults);

        mTvResult.setVisibility(View.GONE);
        mLinDetail.setVisibility(View.GONE);

        unClickAble(true);//下一题又可以选择了
    }


    /**
     * 加载做过的界面
     */
    private void doneWhich(String done) {
        unClickAble(false);

        //查询答案
        Answer answer = DataSupport.find(Answer.class, index + 1);
        String daan;//标准答案
        if (EmptyUtils.isNotEmpty(answer.getDaan1())) {
            daan = answer.getDaan1();
        } else if (EmptyUtils.isNotEmpty(answer.getDaan2())) {
            daan = answer.getDaan2();
        } else if (EmptyUtils.isNotEmpty(answer.getDaan3())) {
            daan = answer.getDaan3();
        } else if (EmptyUtils.isNotEmpty(answer.getDaan4())) {
            daan = answer.getDaan4();
        } else {
            daan = "";
        }

        //如果选择的和答案一致,选择的变绿，变勾
        if (done.equals(daan)) {
            choiceRight(daan);
        }
        //如果选择的和答案不一致，选择的变红，变×。把正确的显示出来
        else {
            choiceRight(daan);
            choiceWrong(done);
        }
    }

}
