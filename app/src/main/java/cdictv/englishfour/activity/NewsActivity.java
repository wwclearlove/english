package cdictv.englishfour.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import cdictv.englishfour.R;
import cdictv.englishfour.javabean.MessageBean;
import cdictv.englishfour.utils.NewBean;
import okhttp3.Call;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {
    private TextView title;
    private HtmlTextView html;
    private ImageView back;


    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bast);

        initView();
        initData();
        initLinser();
    }

    private void initLinser() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        final Intent intent = getIntent();
        String nesId=intent.getStringExtra("id");
        Log.d("===", "initData: "+nesId);
        OkGo.post("https://www.mxnzp.com/api/news/details?newsId="+nesId)
                .params("app_id","itjclopoopmpuwtn")
                .params("app_secret","cW5XQ1hocGhFSXZxYXhCRVNzVjVZUT09")

                .execute(new StringCallback() {//回调函数
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //s网络返回来的json数据
                        //Gson的解析   要用Gson解析框架，需要yin依赖
                        /**
                         * 第一个参数是json字符串
                         * 第二个参数是类型
                         * new TypeToken<List<MessageBean>>(){}.getType()
                         */


                        NewBean dataBean = gson.fromJson(s, NewBean.class);
                        NewBean.DataBean bean= dataBean.data;
                        title.setText(bean.title);
                        html.setHtml(bean.content, new HtmlHttpImageGetter(html));

                    }
                });
    }

    private void initView() {

        html = (HtmlTextView) findViewById(R.id.html);
        title = (TextView) findViewById(R.id.title);


        back = (ImageView) findViewById(R.id.back);


    }
}