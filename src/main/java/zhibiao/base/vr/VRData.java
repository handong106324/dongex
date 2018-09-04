package zhibiao.base.vr;

/**
 * Created by Luonanqin on 5/8/15.
 */
public class VRData {

	private String date;
	private String vr;

	public VRData(String date, String vr) {
		this.date = date;
		this.vr = vr;
	}

	public String toString() {
		return date + ',' + vr;
	}
}
