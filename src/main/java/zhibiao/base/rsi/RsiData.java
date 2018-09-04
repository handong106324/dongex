package zhibiao.base.rsi;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class RsiData {

	private String date;
	private String rsi;
	private String sma_max;
	private String sma_abs;

	public RsiData(String date, String rsi, String sma_max, String sma_abs) {
		this.date = date;
		this.rsi = rsi;
		this.sma_max = sma_max;
		this.sma_abs = sma_abs;
	}

	@Override
	public String toString() {
		return date + ',' + rsi + ',' + sma_max + ',' + sma_abs;
	}
}
