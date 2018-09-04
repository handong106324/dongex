package zhibiao.base.boll;

/**
 * Created by Luonanqin on 5/8/15.
 */
public class BOLLData {

	private String date;
	private String upper;
	private String mid;
	private String lower;

	public BOLLData(String date, String upper, String mid, String lower) {
		this.date = date;
		this.upper = upper;
		this.mid = mid;
		this.lower = lower;
	}

	@Override
	public String toString() {
		return date + ',' + upper + ',' + mid + ',' + lower;
	}
}
