package cdictv.englishfour.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.litepal.LitePal;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.javabean.Answer;
import cdictv.englishfour.javabean.Collect;

public class ChapterexericseActivity extends AppCompatActivity {
    @InjectView(R.id.tv_index)
    TextView mTvIndex;
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
    @InjectView(R.id.tv_result)
    TextView mTvResult;
    @InjectView(R.id.tv_detail)
    TextView mTvDetail;
    @InjectView(R.id.tv_current_number)
    TextView mTvCurrentNumber;
    @InjectView(R.id.lin_daan_answer)
    LinearLayout mLinDaanAnswer;
    @InjectView(R.id.lin_a)
    LinearLayout mLinA;
    @InjectView(R.id.lin_b)
    LinearLayout mLinB;
    @InjectView(R.id.lin_c)
    LinearLayout mLinC;
    @InjectView(R.id.lin_d)
    LinearLayout mLinD;
    @InjectView(R.id.iv_a)
    ImageView mIvA;
    @InjectView(R.id.iv_b)
    ImageView mIvB;
    @InjectView(R.id.iv_c)
    ImageView mIvC;
    @InjectView(R.id.iv_d)
    ImageView mIvD;
    @InjectView(R.id.im_sc)
    ImageView mImSc;
    //索引,现在做到第几题了
    private int index;
    private List<Answer> list;
    //正确的题目个数
    public static int  rightResult;
    //错误的题目个数
    public static int wrongResult;
    int tag = 0;//二次点击

    /**
     * 1.第一步我们要从数据库拿回题目信息，装在list里面
     * 2.第二步我们从list里去获取answer对象，来渲染我的textview
     * 3.第三步--->上一题 ，下一题-->list，通过里面的get(position)-->Answer对象
     * 4.第四步答案排错
     * 1>首先要找到我们的正确答案
     * 2>点击的答案，传递一个选择的答案，A
     * 3>通过选择的答案和我的正确答案进行比较
     * 4>渲染我们该有的界面展示
     * 5.对做过的题目进行数据库填充
     * ---->reply 进行我做过的题统计,通过我的数据库框架改变我的数据库内容
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapterexericse);
        ButterKnife.inject(this);
        //把每条信息返回到list里面
        list = LitePal.findAll(Answer.class);
        //一开始我会去清除reply字段，让reply变成0；
        clearReply();
        getDatas();
    }

    /**
     * 清除reply
     */
    private void clearReply() {
        for (int i = 0; i < list.size(); i++) {
            //list.get(i)代表一个answer对象
            Answer answer = list.get(i);
            //设置值
            answer.setReply("0");
            //通过id进行每个reply的值变成0
            answer.updateAll("id = ?", list.get(i).getId());
        }
    }

    //从数据里面拿数据
    private void getDatas() {

        Answer answer = list.get(index);
        //当前做到第几题？
        mTvCurrentNumber.setText((index + 1) + "/" + list.size());
        //题号
        mTvIndex.setText("第" + answer.getId() + "题");
        //类型
        mTvTypes.setText(answer.getTypes());
        //题目
        mTvTitle.setText(answer.getTimu_title());
        //选择abcd
        mTvA.setText("A. " + answer.getTimu1());
        mTvB.setText("B. " + answer.getTimu2());
        mTvC.setText("C. " + answer.getTimu3());
        mTvD.setText("D. " + answer.getTimu4());
        //解析
        mTvDetail.setText(answer.getDetail());


        //这里来判断这道题是否做过
        if ("0".equals(answer.getReply())) {
            //这道题没做过
        } else {
            //这道题做过
            String hisReply = answer.getReply();//之前做过的答案
            getDaanCom(hisReply);//数据渲染
        }
        boolean flag= checkIsCollect();
        if (flag==true){
            mImSc.setImageResource(R.drawable.b_unstar2);
        }
    }

    @OnClick({R.id.lin_navigation_back, R.id.lin_answer, R.id.lin_bt_unstar, R.id.lin_navigation_forward,
            R.id.lin_a, R.id.lin_b, R.id.lin_c, R.id.lin_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //上一题
            case R.id.lin_navigation_back:
                //如果为第一题，那么return出去
                if (index == 0) {
                    Toast.makeText(this, "已经是第一题咯", Toast.LENGTH_SHORT).show();
                    return;
                }
                clearLin();
                index--;
                getDatas();
                break;
            case R.id.lin_answer:
                //题解
                change();
                break;
            case R.id.lin_bt_unstar:
//如果该题已经收藏就不在收藏
                if (checkIsCollect()) {
                    return;
                }
                Collect collect = new Collect();
                collect.setId(Integer.parseInt(list.get(index).getId()));
                collect.setTimu_title(list.get(index).getTimu_title());
                collect.setTimu1(list.get(index).getTimu1());
                collect.setTimu2(list.get(index).getTimu2());
                collect.setTimu3(list.get(index).getTimu3());
                collect.setTimu4(list.get(index).getTimu4());
                collect.setDaan1(list.get(index).getDaan1());
                collect.setDaan2(list.get(index).getDaan2());
                collect.setDaan3(list.get(index).getDaan3());
                collect.setDaan4(list.get(index).getDaan4());
                collect.setDetail(list.get(index).getDetail());
                collect.setTypes(list.get(index).getTypes());
                collect.setReply(list.get(index).getReply());
                collect.save();
                mImSc.setImageResource(R.drawable.b_unstar2);
                ToastUtils.showShort("收藏成功");
                break;
            //下一题
            case R.id.lin_navigation_forward:
                //如果为第一题，那么return出去
                if (index == (list.size() - 1)) {
                    Toast.makeText(this, "已经是最后一题咯", Toast.LENGTH_SHORT).show();
                    return;
                }
                clearLin();
                index++;
                getDatas();
                break;
            //点击A
            case R.id.lin_a:
//                ToastUtils.showShort("选择了A");
                getDaan("A");
                break;
            //点击B
            case R.id.lin_b:
//                ToastUtils.showShort("选择了B");
                getDaan("B");
                break;
            //点击C
            case R.id.lin_c:
//                ToastUtils.showShort("选择了C");
                getDaan("C");

                break;
            //点击D
            case R.id.lin_d:
//                ToastUtils.showShort("选择了D");
                getDaan("D");
                break;
        }
    }

    /**
     * 判断是否收藏过该题
     */
    private boolean checkIsCollect() {
        List<Collect> collectList = LitePal.findAll(Collect.class);
        for (int i = 0; i < collectList.size(); i++) {
            if (collectList.get(i).getTimu_title().equals(list.get(index).getTimu_title())) {

                return true;
            }
        }
        return false;
    }

    public void change() {
        if (tag == 0) {
            mLinDaanAnswer.setVisibility(View.VISIBLE);
            tag = 1;
        } else {
            mLinDaanAnswer.setVisibility(View.GONE);
            tag = 0;
        }
    }

    /**
     * 渲染做过的题目的界面（不需要统计）
     */
    private void getDaanCom(String choice) {
        //做完一题过后不能再让他去点击
        clickFlag(false);
        Answer answer = list.get(index);
        //定义一个字符串
        String daan;

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
        LogUtils.a("===", "正确答案是：" + daan);
        //判断选择的答案是否我的正确答案一致
        if (choice.equals(daan)) {
            //做我选择正确的界面渲染
            initRight(choice);
        } else {
            //做我选择错误的界面渲染
            initWrong(choice);
            initRight(daan);
        }


    }

    /**
     * 渲染没做过的题目的界面（需要统计）
     */
    private void getDaan(String choice) {

        Answer answer = list.get(index);
        answer.setReply(choice);
        //更新数据库reply字段
        //？是sql注入，防止 修改数据库结构
        answer.updateAll("id = ?", answer.getId());
        //定义一个字符串
        String daan;
//
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
        LogUtils.a("===", "正确答案是：" + daan);
//        //判断选择的答案是否我的正确答案一致
        if (choice.equals(daan)) {
            //做我选择正确的界面渲染
            initRight(choice);
            rightResult++;//自增长1个

        } else {
            //做我选择错误的界面渲染
            initWrong(choice);
            initRight(daan);
            wrongResult++;
        }
        //做完一题过后不能再让他去点击
        clickFlag(false);
        //结果进行统计
        mTvResult.setVisibility(View.VISIBLE);
        mTvResult.setText("共答" + (rightResult + wrongResult) + "题,答对" + rightResult + "题,答错" + wrongResult + "题,正确率" + (rightResult * 100 / (rightResult + wrongResult)) + "%");
    }

    /**
     * 渲染正确的界面
     */
    private void initRight(String choice) {
        if ("A".equals(choice)) {
            mIvA.setImageResource(R.drawable.right);
            mLinA.setBackgroundColor(getResources().getColor(R.color.right));
        } else if ("B".equals(choice)) {
            mIvB.setImageResource(R.drawable.right);
            mLinB.setBackgroundColor(getResources().getColor(R.color.right));
        } else if ("C".equals(choice)) {
            mIvC.setImageResource(R.drawable.right);
            mLinC.setBackgroundColor(getResources().getColor(R.color.right));
        } else if ("D".equals(choice)) {
            mIvD.setImageResource(R.drawable.right);
            mLinD.setBackgroundColor(getResources().getColor(R.color.right));
        }


    }

    /**
     * 渲染错误的界面
     */
    private void initWrong(String choice) {

        if ("A".equals(choice)) {
            mIvA.setImageResource(R.drawable.wrong);
            mLinA.setBackgroundColor(getResources().getColor(R.color.wrong));
        } else if ("B".equals(choice)) {
            mIvB.setImageResource(R.drawable.wrong);
            mLinB.setBackgroundColor(getResources().getColor(R.color.wrong));
        } else if ("C".equals(choice)) {
            mIvC.setImageResource(R.drawable.wrong);
            mLinC.setBackgroundColor(getResources().getColor(R.color.wrong));
        } else if ("D".equals(choice)) {
            mIvD.setImageResource(R.drawable.wrong);
            mLinD.setBackgroundColor(getResources().getColor(R.color.wrong));
        }

    }

    private void clearLin() {
        //题解的消失
        mLinDaanAnswer.setVisibility(View.GONE);
        //结果xiaoshi
        mTvResult.setVisibility(View.GONE);

        mLinA.setBackgroundColor(getResources().getColor(R.color.choice));
        mLinB.setBackgroundColor(getResources().getColor(R.color.choice));
        mLinC.setBackgroundColor(getResources().getColor(R.color.choice));
        mLinD.setBackgroundColor(getResources().getColor(R.color.choice));
        mImSc.setImageResource(R.drawable.bt_unstar);
        mIvA.setImageResource(R.drawable.defaults);
        mIvB.setImageResource(R.drawable.defaults);
        mIvC.setImageResource(R.drawable.defaults);
        mIvD.setImageResource(R.drawable.defaults);

        //做完一题过后不能再让他去点击
        clickFlag(true);

    }

    /**
     * 设置是否可以点击四个选择按钮
     */
    private void clickFlag(boolean b) {
        mLinA.setClickable(b);
        mLinB.setClickable(b);
        mLinC.setClickable(b);
        mLinD.setClickable(b);
    }
}
