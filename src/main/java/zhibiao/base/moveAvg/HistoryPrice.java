package zhibiao.base.moveAvg;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class HistoryPrice {

	private String date;
	private Float price;

	public HistoryPrice(String date, Float price) {
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
