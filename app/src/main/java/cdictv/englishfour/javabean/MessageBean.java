package cdictv.englishfour.javabean;


import java.util.List;

public class MessageBean {

	/**
	 * code : 1
	 * msg : 数据返回成功！
	 * data : [{"title":"桃花眼、凤眼、狐狸眼，垂眼\u2026\u2026来看看你是哪一种眼型？","imgList":["http://bjnewsrec-cv.ws.126.net/three49443439841j00qlhdjc0031d200u000kvg00hv00cf.jpg"],"source":"上海圈","newsId":"FU2QFFR70522C1U0","digest":"","postTime":"2020-12-17 19:06:49","videoList":null},{"title":"2021年起，房价是否会下跌？开发商：这3类房子或贬值30%","imgList":["http://bjnewsrec-cv.ws.126.net/three80421383907j00qloh9100a0c000xc00m6m.jpg"],"source":"探房杜咔咔","newsId":"FUCOEGB30535AEUM","digest":"","postTime":"2020-12-21 15:43:46","videoList":null},{"title":"北汽集团雄起了，\u201cBJ40\u201d狩猎者展出，网友：没牧马人什么事了！","imgList":["http://bjnewsrec-cv.ws.126.net/three7711bdd1904p00qlmww400d1c000tl00k1g.jpg"],"source":"热血汽车资讯","newsId":"FUAGSDMH0547LAZZ","digest":"","postTime":"2020-12-20 18:53:24","videoList":null},{"title":"嫦娥五号回地球的打水漂绝技，中国用在了高超音速导弹，全球唯一","imgList":["http://bjnewsrec-cv.ws.126.net/three90015f6d348j00qlhoob0030c000xc00rbm.jpg"],"source":"胖福的小木屋","newsId":"FU388PO4053299CD","digest":"","postTime":"2020-12-17 23:07:52","videoList":null},{"title":"他汀类药物那么多，为何医生只喜欢为老年人用这两种？一文讲清楚","imgList":["http://cms-bucket.ws.126.net/2020/1221/7313edfdj00qlozm5000nc000a6006zc.jpg"],"source":"生活大火锅","newsId":"FUAPSV1S0545GTLL","digest":"","postTime":"2020-12-20 21:30:55","videoList":null},{"title":"印度突然曝光绝密项目，张口就是世界第三，中国又一次被无视了","imgList":["http://bjnewsrec-cv.ws.126.net/three32003b967c6j00qlorbi000dc000hs007fc.jpg"],"source":"阿荣谈科技","newsId":"FUD2VAR405311UHV","digest":"","postTime":"2020-12-21 18:47:43","videoList":null},{"title":"方便多了：微软欲用1年时间干掉所有密码","imgList":["http://cms-bucket.ws.126.net/2020/1220/63c384efp00qlm7va003qc0009c0070c.png"],"source":"快科技","newsId":"FU9HUEDJ001697V8","digest":"","postTime":"2020-12-20 09:52:23","videoList":null},{"title":"这些学校非\u201c985\u201d，却有着排名全国第一的名校！","imgList":["http://bjnewsrec-cv.ws.126.net/three7773718874fj00qlnx9l001dd200m800etg009e0069.jpg"],"source":"高考直通车","newsId":"FUBTQRJS05169U2B","digest":"","postTime":"2020-12-21 07:58:37","videoList":null},{"title":"国产设备的意义？","imgList":["http://bjnewsrec-cv.ws.126.net/little200e805708dc0947519a236fb8bf41a518c.jpg"],"source":"金融界","newsId":"FU99G40L0519QIKK","digest":"","postTime":"2020-12-20 07:28:22","videoList":null},{"title":"送给\u201c考研党\u201d的福利，笔试成绩过线就能录取，考研重点考虑","imgList":["http://bjnewsrec-cv.ws.126.net/three555feef4830p00qlmqea02b3c0010400s1m.jpg"],"source":"高校新视线","newsId":"FUA8U81V05450NV2","digest":"","postTime":"2020-12-20 16:34:18","videoList":null}]
	 */

	public int code;
	public String msg;
	public List<DataBean> data;

	public static class DataBean {
		/**
		 * title : 桃花眼、凤眼、狐狸眼，垂眼……来看看你是哪一种眼型？
		 * imgList : ["http://bjnewsrec-cv.ws.126.net/three49443439841j00qlhdjc0031d200u000kvg00hv00cf.jpg"]
		 * source : 上海圈
		 * newsId : FU2QFFR70522C1U0
		 * digest :
		 * postTime : 2020-12-17 19:06:49
		 * videoList : null
		 */

		public String title;
		public String source;
		public String newsId;
		public String digest;
		public String postTime;
		public Object videoList;
		public List<String> imgList;
	}
}
