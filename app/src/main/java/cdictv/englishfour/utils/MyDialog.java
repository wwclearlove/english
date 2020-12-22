package cdictv.englishfour.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cdictv.englishfour.R;


/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class MyDialog extends Dialog{
    private String title,content;
    private TextView message,title_top;
    private LinearLayout sure,cancel;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    private String yesStr, noStr;

    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        init();
        initData();
        initEvent();
    }

    private void init() {
        message=$(R.id.dialog_message);
        sure=$(R.id.dialog_sure);
        cancel=$(R.id.dialog_cancel);
        title_top=$(R.id.dialog_title);

    }


    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (title!=null) {
            title_top.setText(getTitle());
        }
        if (content!=null) {
            message.setText(getContent());
        }
    }
    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param content
     */
    public void setMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public <T extends View> T $(int id){
        return (T) findViewById(id);
    }
}
