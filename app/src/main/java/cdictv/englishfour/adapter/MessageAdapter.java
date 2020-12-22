package cdictv.englishfour.adapter;

//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
//    private List<MessageBean> mMessageBeans;
//    static class ViewHolder extends RecyclerView.ViewHolder{
//        TextView mtitle;
//        TextView mdate;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mtitle=itemView.findViewById(R.id.tv_mes_title);
//            mdate=itemView.findViewById(R.id.tv_mes_date);
//        }
//    }
//    public MessageAdapter(List<MessageBean> messageBeans){
//        mMessageBeans=messageBeans;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message,viewGroup,false);
//       ViewHolder holder=new ViewHolder(view);
//       return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        MessageBean messageBean=mMessageBeans.get(i);
//        viewHolder.mtitle.setText(messageBean.getTitle());
////        viewHolder.mdate.setText(messageBean.getDate());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mMessageBeans.size();
//    }

//}

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cdictv.englishfour.R;
import cdictv.englishfour.javabean.MessageBean;
import cdictv.englishfour.utils.GlideRoundTransform;

public class MessageAdapter extends BaseQuickAdapter<MessageBean.DataBean, BaseViewHolder> {
    private ImageView iv;
    private   List<MessageBean.DataBean>  mlist ;
    //构造方法
    public MessageAdapter(int layoutResId,   List<MessageBean.DataBean> data) {
        super(layoutResId, data);
        mlist = data;
    }
    //封装好的方法
    @Override
    protected void convert(BaseViewHolder helper, MessageBean.DataBean item) {
        for(int i = 0;i<mlist.size();i++){
            helper.setText(R.id.tv_mes_title,item.title)
                    .setText(R.id.tv_mes_date,item.postTime);
            iv=helper.itemView.findViewById(R.id.iv_img);
            try {
                Glide.with(helper.itemView.getContext()).load(item.imgList.get(0))
                        .error(R.mipmap.ic_launcher_round)//解析地址错误加载的图片
                        .placeholder(R.mipmap.ic_launcher)//正在加载过程的图片
                        .transform(new GlideRoundTransform(helper.itemView.getContext(),10))//加载圆角图片 30转化为dp单位
                        .crossFade()//渐变展示图片动画
                        .into(iv);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Glide.with(helper.itemView.getContext()).load(item.getUri()).asGif()
//                    .error(R.mipmap.ic_launcher_round)//解析地址错误加载的图片
//                    .placeholder(R.mipmap.ic_launcher)//正在加载过程的图片
//                    .into(iv);
        }
    }

}
