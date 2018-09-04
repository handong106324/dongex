package zhibiao.base.dmi;

/**
 * Created by Luonanqin on 5/3/15.
 */
public class DmiData {

	private String date;
	private String di1; // 上升动向值
	private String di2; // 下降动向值
	private String adx; // 动向平均数
	private String adxr; // 评估数值
	private String tr;
	private String hd;
	private String ld;

	public DmiData(String date, String di1, String di2, String adx, String adxr, String tr, String hd, String ld) {
		this.date = date;
		this.di1 = di1;
		this.di2 = di2;
		this.adx = adx;
		this.adxr = adxr;
		this.tr = tr;
		this.hd = hd;
		this.ld = ld;
	}

	@Override
	public String toString() {
		return date + ',' + di1 + ',' + di2 + ',' + adx + ',' + adxr + ',' + tr + ',' + hd + ',' + ld;
	}
}
