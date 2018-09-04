package zhibiao.base.trix;

/**
 * Created by Luonanqin on 5/8/15.
 */
public class TRIXData {

	private String date;
	private String trix;
	private String trma;
	private String ema1;
	private String ema2;
	private String ema3;

	public TRIXData(String date, String trix, String trma, String ema1, String ema2, String ema3) {
		this.date = date;
		this.trix = trix;
		this.trma = trma;
		this.ema1 = ema1;
		this.ema2 = ema2;
		this.ema3 = ema3;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTrix(String trix) {
		this.trix = trix;
	}

	public void setTrma(String trma) {
		this.trma = trma;
	}

	public void setEma1(String ema1) {
		this.ema1 = ema1;
	}

	public void setEma2(String ema2) {
		this.ema2 = ema2;
	}

	public void setEma3(String ema3) {
		this.ema3 = ema3;
	}

	@Override
	public String toString() {
		return date + ',' + trix + ',' + trma + "," + ema1 + ',' + ema2 + ',' + ema3;
	}
}
