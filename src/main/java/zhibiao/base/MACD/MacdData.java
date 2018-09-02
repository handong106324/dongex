package zhibiao.base.MACD;

/**
 * Created by Luonanqin on 4/19/15.
 */
public class MacdData {

	private String date;
	private String macd;
	private String diff;
	private String dea;
	private double ema12 = 0;
	private double ema26 = 0;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMacd() {
		return macd;
	}

	public void setMacd(String macd) {
		this.macd = macd;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public String getDea() {
		return dea;
	}

	public void setDea(String dea) {
		this.dea = dea;
	}

	public double getEma12() {
		return ema12;
	}

	public void setEma12(double ema12) {
		this.ema12 = ema12;
	}

	public double getEma26() {
		return ema26;
	}

	public void setEma26(double ema26) {
		this.ema26 = ema26;
	}

	@Override
	public String toString() {
		return date + ',' + macd + ',' + diff + ',' + dea + ',' + ema12 + ',' + ema26;
	}
}
