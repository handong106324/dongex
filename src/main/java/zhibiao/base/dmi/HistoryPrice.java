package zhibiao.base.dmi;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class HistoryPrice {

	private String date;
	private Float close;
	private Float max;
	private Float min;

	public HistoryPrice(String date, Float close, Float max, Float min) {
		this.date = date;
		this.close = close;
		this.max = max;
		this.min = min;
	}

	public String getDate() {
		return date;
	}

	public Float getClose() {
		return close;
	}

	public Float getMax() {
		return max;
	}

	public Float getMin() {
		return min;
	}
}
