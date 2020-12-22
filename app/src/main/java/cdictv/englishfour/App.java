package cdictv.englishfour;


import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;

import org.litepal.LitePalApplication;


public class App extends LitePalApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化和工具类
		Utils.init(this);
		//必须调用初始化
		OkGo.init(this);
	}
}
