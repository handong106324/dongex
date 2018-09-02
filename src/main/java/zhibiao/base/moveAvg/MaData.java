package zhibiao.base.moveAvg;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class MaData {

	private String date;
	private String ma;

	public MaData(String date, String ma) {
		this.date = date;
		this.ma = ma;
	}

	@Override
	public String toString() {
		return date + "," + ma;
	}
}
