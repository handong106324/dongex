package zhibiao.base.wr;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class HistoryPrice {

	private String date;
	private float price;
	private float max;
	private float min;

	public HistoryPrice(String date, float price, float max, float min) {
		this.date = date;
		this.price = price;
		this.max = max;
		this.min = min;
	}

	public String getDate() {
		return date;
	}

	public Float getPrice() {
		return price;
	}

	public float getMax() {
		return max;
	}

	public float getMin() {
		return min;
	}
}
