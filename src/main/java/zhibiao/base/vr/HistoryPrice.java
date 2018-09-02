package zhibiao.base.vr;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class HistoryPrice {

	private String date;
	private float price;
	private long vols;

	public HistoryPrice(String date, float price, long vols) {
		this.date = date;
		this.price = price;
		this.vols = vols;
	}

	public String getDate() {
		return date;
	}

	public Float getPrice() {
		return price;
	}

	public long getVols() {
		return vols;
	}
}
