package cdictv.englishfour.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cdictv.englishfour.R;
import cdictv.englishfour.adapter.MessageAdapter;
import cdictv.englishfour.javabean.MessageBean;
import okhttp3.Call;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {
    private MessageAdapter mMessageAdapter;
    private   List<MessageBean.DataBean>  mlist =new ArrayList<>();;
    @InjectView(R.id.rv)
    RecyclerView mRv;
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.inject(this);

        RefreshLayout refreshLayout =findViewById(R.id.refreshLayout);
        initRv();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getinfos();
                refreshLayout.finishRefresh();
                     Toast.makeText(getApplicationContext(),"刷新完成",Toast.LENGTH_LONG).show();
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getinfos();
                refreshLayout.finishRefresh();
                Toast.makeText(getApplicationContext(),"刷新完成",Toast.LENGTH_LONG).show();
            }
        });
        //网络获取数据
        getinfos();
       // initRv();
    }

    private void getinfos() {

        //网络请求地址
            //url = "http://route.showapi.com/109-35?showapi_appid=80781&showapi_sign=6b5ea453227546c0b04a77b16e9d9d07&title="+ URLEncoder.encode("篮球", "utf-8")+"&page=20&maxResult=100";
            OkGo.post("https://www.mxnzp.com/api/news/list?typeId=525&page=1")
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

//                            System.out.println(s);
                            MessageBean listTep = gson.fromJson(s, MessageBean.class);

////                            for (int i = 0;i<lists.size();i++){
////                                LogUtils.a("fromJson", lists.get(i).toString());
////                            }
////
                            mlist.clear();
                            mlist.addAll(listTep.data);
                            mMessageAdapter.notifyDataSetChanged();
                        }
                    });
    }

    private void initRv() {
        //设置recyclerview的布局样式（从上往下，还是从左往右，还是瀑布流）从左往右LinearLayoutManager.HORIZONTAL
        mRv.setLayoutManager(new LinearLayoutManager(this));//默认表示从上往下

        mMessageAdapter=new MessageAdapter(R.layout.item_message,mlist);

        //设置动画
        mMessageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        //如果设置成true,动画只加载一次
        mMessageAdapter.isFirstOnly(false);
        //设置点击事件
        //设置点击事件
        mMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort(position+"");
             Intent intent=new Intent(MessageActivity.this,NewsActivity.class);

                Log.d("===", "initData: "+mlist.get(position).newsId);
                intent.putExtra("id",mlist.get(position).newsId);
                startActivity(intent);
            }
        });
        mRv.setAdapter(mMessageAdapter);
    }

}
