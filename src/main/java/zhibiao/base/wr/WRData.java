package zhibiao.base.wr;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class WRData {

	private String date;
	private String wr1;
	private String wr2;

	public WRData(String date, String wr1, String wr2) {
		this.date = date;
		this.wr1 = wr1;
		this.wr2 = wr2;
	}

	@Override
	public String toString() {
		return date + ',' + wr1 + ',' + wr2;
	}
}
