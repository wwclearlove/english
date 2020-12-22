package cdictv.englishfour.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cdictv.englishfour.R;
import cdictv.englishfour.javabean.HistoryBean;



public class HisAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {

	public HisAdapter(int layoutResId, List data) {
		super(layoutResId, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, HistoryBean item) {
		helper.setText(R.id.tv_his_num, String.valueOf(helper.getPosition()))
				.setText(R.id.tv_his_score, item.score+"分")
				.setText(R.id.tv_his_costtime, item.costTime)
				.setText(R.id.tv_his_date, item.date);

		if (Integer.parseInt(item.score) < 60) {
			helper.setText(R.id.tv_his_type, "不及格");
		} else if (Integer.parseInt(item.score) < 70 && Integer.parseInt(item.score) >= 60) {
			helper.setText(R.id.tv_his_type, "及格");
		} else if (Integer.parseInt(item.score) < 85 && Integer.parseInt(item.score) >= 70) {
			helper.setText(R.id.tv_his_type, "良好");
		} else if (Integer.parseInt(item.score) <= 100 && Integer.parseInt(item.score) >= 85) {
			helper.setText(R.id.tv_his_type, "优秀");
		}

	}
}

