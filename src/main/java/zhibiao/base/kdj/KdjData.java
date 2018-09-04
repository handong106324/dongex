package zhibiao.base.kdj;

/**
 * Created by Luonanqin on 4/23/15.
 */
public class KdjData {

	private String date;
	private String k;
	private String d;
	private String j;
	private String lastK;
	private String lastD;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getJ() {
		return j;
	}

	public void setJ(String j) {
		this.j = j;
	}

	public String getLastK() {
		return lastK;
	}

	public void setLastK(String lastK) {
		this.lastK = lastK;
	}

	public String getLastD() {
		return lastD;
	}

	public void setLastD(String lastD) {
		this.lastD = lastD;
	}

	@Override
	public String toString() {
		return "KdjData{" +
				"date='" + date + '\'' +
				", k='" + k + '\'' +
				", d='" + d + '\'' +
				", j='" + j + '\'' +
				", lastK='" + lastK + '\'' +
				", lastD='" + lastD + '\'' +
				'}';
	}
}
