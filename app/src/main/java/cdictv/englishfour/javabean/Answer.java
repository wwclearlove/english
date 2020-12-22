package cdictv.englishfour.javabean;

import org.litepal.crud.LitePalSupport;


public class Answer extends LitePalSupport {

	private String id;//题目编号
	private String timu_title;//题目
	private String timu1;//a
	private String timu2;//b
	private String timu3;//c
	private String timu4;//d
	private String daan1;
	private String daan2;
	private String daan3;
	private String daan4;
	private String detail;
	private String types;
	private String reply;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimu_title() {
		return timu_title;
	}

	public void setTimu_title(String timu_title) {
		this.timu_title = timu_title;
	}

	public String getTimu1() {
		return timu1;
	}

	public void setTimu1(String timu1) {
		this.timu1 = timu1;
	}

	public String getTimu2() {
		return timu2;
	}

	public void setTimu2(String timu2) {
		this.timu2 = timu2;
	}

	public String getTimu3() {
		return timu3;
	}

	public void setTimu3(String timu3) {
		this.timu3 = timu3;
	}

	public String getTimu4() {
		return timu4;
	}

	public void setTimu4(String timu4) {
		this.timu4 = timu4;
	}

	public String getDaan1() {
		return daan1;
	}

	public void setDaan1(String daan1) {
		this.daan1 = daan1;
	}

	public String getDaan2() {
		return daan2;
	}

	public void setDaan2(String daan2) {
		this.daan2 = daan2;
	}

	public String getDaan3() {
		return daan3;
	}

	public void setDaan3(String daan3) {
		this.daan3 = daan3;
	}

	public String getDaan4() {
		return daan4;
	}

	public void setDaan4(String daan4) {
		this.daan4 = daan4;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"id='" + id + '\'' +
				", timu_title='" + timu_title + '\'' +
				", timu1='" + timu1 + '\'' +
				", timu2='" + timu2 + '\'' +
				", timu3='" + timu3 + '\'' +
				", timu4='" + timu4 + '\'' +
				", daan1='" + daan1 + '\'' +
				", daan2='" + daan2 + '\'' +
				", daan3='" + daan3 + '\'' +
				", daan4='" + daan4 + '\'' +
				", detail='" + detail + '\'' +
				", types='" + types + '\'' +
				", reply='" + reply + '\'' +
				'}';
	}
}
