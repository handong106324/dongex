package zhibiao.base.MACD;

/**
 * Created by Luonanqin on 4/19/15.
 */
public class MacdData {

	private String date;
	private String macd;
	private String diff;
	private String dea;

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

	@Override
	public String toString() {
		return "MacdData{" +
				"date='" + date + '\'' +
				", macd='" + macd + '\'' +
				", diff='" + diff + '\'' +
				", dea='" + dea + '\'' +
				'}';
	}
}
