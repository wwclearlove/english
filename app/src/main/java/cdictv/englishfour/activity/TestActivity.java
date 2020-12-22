package cdictv.englishfour.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.config.AppConfig;
import cdictv.englishfour.javabean.Answer;
import cdictv.englishfour.javabean.Collect;

public class TestActivity extends AppCompatActivity {
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
    @InjectView(R.id.timer)
    TextView mTimer;
    @InjectView(R.id.im_sc)
    ImageView mImSc;

    private List<Answer> infosList;
    private long[] select;//随机题
    private int index = 0;//做到第几题
    public static int right_total;//答对多少题
    public static int wrong_total;//答错多少题
    private long costTime;//花费时间

    //倒计时时间
    private CountDownTimer timer = new CountDownTimer(20 * 60 * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTimer.setText("剩余时间  " + (millisUntilFinished / 60 / 1000) + "分" + millisUntilFinished / 1000 % 60 + "秒");
            TestActivity.this.costTime = 20 * 60 * 1000 - millisUntilFinished;
        }

        @Override
        public void onFinish() {
            mTimer.setEnabled(true);
            mTimer.setText("考试完毕");
            jumpToNext();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
        select = selectTest();
        clearInfosReply();//清除历史做过的记录
        getInfosFromDB(index);//加载数据
        timer.start();

    }

    /**
     * 25道随机题
     */
    private long[] selectTest() {
        List<Integer> data = new ArrayList<Integer>();
        long[] number = new long[25];
        Random rand = new Random();

        for (int i = 0; i < 25; i++) {
            int j = rand.nextInt(184) + 1;
            if (data.contains(j)) {
                i--;
                continue;
            } else {
                data.add(j);
                number[i] = j;
            }
        }
        return number;
    }

    /**
     * 清除历史做过的记录
     */
    private void clearInfosReply() {
        //退出就清除错误的题
        SPUtils.getInstance().remove(AppConfig.WRONG_JSON);

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

        infosList = LitePal.findAll(Answer.class, select);
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


    @OnClick({R.id.tv_handin, R.id.lin_a, R.id.lin_b, R.id.lin_c, R.id.lin_d, R.id.lin_navigation_back, R.id.lin_answer, R.id.lin_bt_unstar, R.id.lin_navigation_forward})
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
                unClickAble(true);
                mLinA.setClickable(false);
                break;
            //选择B答案
            case R.id.lin_b:
                clickChoice("B");
                unClickAble(true);
                mLinB.setClickable(false);
                break;
            //选择C答案
            case R.id.lin_c:
                clickChoice("C");
                unClickAble(true);
                mLinC.setClickable(false);
                break;
            //选择D答案
            case R.id.lin_d:
                clickChoice("D");
                unClickAble(true);
                mLinD.setClickable(false);
                break;
            //交卷
            case R.id.tv_handin:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否交卷？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                jumpToNext();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }


    /**
     * 跳转结果界面
     */
    private void jumpToNext() {
        Intent resultIntent = new Intent(TestActivity.this, ResultActivity.class);
        resultIntent.putExtra("costTime", costTime);
        resultIntent.putExtra("right_total", right_total);
        resultIntent.putExtra("wrong_total", wrong_total);
        resultIntent.putExtra("total", infosList.size());
        TestActivity.this.startActivity(resultIntent);
        finish();
    }

    /**
     * 判断是否收藏过该题
     */
    private boolean checkIsCollect() {
        List<Collect> collectList = LitePal.findAll(Collect.class);
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
        clearInter();//清除其他选中的

        String daan;//标准答案
        //查询答案
        Answer answer = LitePal.find(Answer.class, Long.valueOf(infosList.get(index).getId()));

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

        //考试选中的答案
        choiceRight(choice);

        //这里是判断分数，逻辑有点麻烦，得注意，
        //做过一次
        if (!"0".equals(answer.getReply())) {
            //之前是对的
            if (answer.getReply().equals(daan)) {

                right_total--;
                //如果选择的和答案一致,
                if (choice.equals(daan)) {
                    right_total++;
                }
                //如果选择的和答案不一致
                else {
                    saveWrongId();//之前对的，现在错了，添加一个
                    wrong_total++;
                }
            }
            //之前是错的
            else {
                wrong_total--;
                //如果选择的和答案一致,
                if (choice.equals(daan)) {
                    clearWrongId();//之前错的，现在对了，将jsonlist清除一个
                    right_total++;
                }
                //如果选择的和答案不一致
                else {
                    wrong_total++;
                }
            }

        } else {
            //如果选择的和答案一致,
            if (choice.equals(daan)) {
                right_total++;
            }
            //如果选择的和答案不一致
            else {
                saveWrongId();//记录错误题目的id
                wrong_total++;
            }
        }


        //做完一道题需要在数据库里面更新reply字段
        answer.setReply(choice);
        answer.updateAll("id = ?", String.valueOf(infosList.get(index).getId()));

//		mTvResult.setText("共答" + (right_total + wrong_total) + "题,答对" + right_total + "题，答错" + wrong_total + "题，正确率" + (right_total * 100 / (right_total + wrong_total)) + "%");
//		mTvResult.setVisibility(View.VISIBLE);

        LogUtils.a("错误题id" + SPUtils.getInstance().getString(AppConfig.WRONG_JSON));
    }

    /**
     * 记录错误题型，存入sp中
     */
    private void saveWrongId() {
        Long wrongId = Long.valueOf(infosList.get(index).getId());
        //如果错误id的sp里面没有值
        if (EmptyUtils.isEmpty(SPUtils.getInstance().getString(AppConfig.WRONG_JSON, ""))) {
            List<Long> list = new ArrayList<>();
            //如果之前有
            if (list.contains(wrongId)) {
                return;
            }
            list.add(wrongId);
            //把集合转换成json保存起来
            SPUtils.getInstance().put(AppConfig.WRONG_JSON, new Gson().toJson(list));
        }
        //如果sp里面有值
        else {
            //拿到sp里面的值
            String json = SPUtils.getInstance().getString(AppConfig.WRONG_JSON);
            //把sp拿到的json转换成list
            List<Long> list = new Gson().fromJson(json, new TypeToken<List<Long>>() {
            }.getType());
            if (list.contains(wrongId)) {
                return;
            }
            list.add(wrongId);
            //把集合转换成json保存起来
            SPUtils.getInstance().put(AppConfig.WRONG_JSON, new Gson().toJson(list));
        }

    }

    /**
     * 清除一个错误答案
     */
    private void clearWrongId() {
        //拿到sp里面的值
        String json = SPUtils.getInstance().getString(AppConfig.WRONG_JSON);
        //把sp拿到的json转换成list
        List<Long> list = new Gson().fromJson(json, new TypeToken<List<Long>>() {
        }.getType());
        //根据id移除这个错误的题
        list.remove(Long.valueOf(infosList.get(index).getId()));
        //把集合转换成json保存起来
        SPUtils.getInstance().put(AppConfig.WRONG_JSON, new Gson().toJson(list));

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
     * 选择正确的表现
     */
    private void choiceRight(String daan) {
        //如果正确答案是A
        if ("A".equals(daan)) {
            mIvA.setImageResource(R.drawable.more_select);
        }
        //如果正确答案是B
        else if ("B".equals(daan)) {
            mIvB.setImageResource(R.drawable.more_select);
        }
        //如果正确答案是C
        else if ("C".equals(daan)) {
            mIvC.setImageResource(R.drawable.more_select);
        }
        //如果正确答案是D
        else if ("D".equals(daan)) {
            mIvD.setImageResource(R.drawable.more_select);
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
        mIvA.setImageResource(R.drawable.defaults);
        mIvB.setImageResource(R.drawable.defaults);
        mIvC.setImageResource(R.drawable.defaults);
        mIvD.setImageResource(R.drawable.defaults);
        mImSc.setImageResource(R.drawable.bt_unstar);
        mTvResult.setVisibility(View.GONE);
        mLinDetail.setVisibility(View.GONE);
        unClickAble(true);//下一题又可以选择了
    }
    private void clearInter() {
        mLinA.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mLinB.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mLinC.setBackgroundColor(getResources().getColor(R.color.question_choice));
        mLinD.setBackgroundColor(getResources().getColor(R.color.question_choice));
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

        //之前做好选中的
        choiceRight(done);
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("真的要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //退出就清除错误的题
                        SPUtils.getInstance().remove(AppConfig.WRONG_JSON);
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}