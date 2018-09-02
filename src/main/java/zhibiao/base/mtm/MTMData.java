package zhibiao.base.mtm;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class MTMData {

	private String date;
	private String mtm;
	private String mamtm;

	public MTMData(String date, String mtm, String mamtm) {
		this.date = date;
		this.mtm = mtm;
		this.mamtm = mamtm;
	}

	@Override
	public String toString() {
		return date + ',' + mtm + ',' + mamtm;
	}
}
