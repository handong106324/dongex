package zhibiao.base.asi;

/**
 * Created by Luonanqin on 5/3/15.
 */
public class ASIData {

	private String date;
	private String asi;
	private String asit;

	public ASIData(String date, String asi, String asit) {
		this.date = date;
		this.asi = asi;
		this.asit = asit;
	}

	@Override
	public String toString() {
		return date + ',' + asi + ',' + asit;
	}
}
