package zhibiao.base.mtm;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class HistoryPrice {

	private String date;
	private float price;

	public HistoryPrice(String date, float price) {
		this.date = date;
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public Float getPrice() {
		return price;
	}
}
