package zhibiao.dzh;

/**
 * Created by Luonanqin on 4/9/15.
 */
public class DzhStockData {

	private String lp;
	private String zd;
	private String zf;
	private String vol;
	private String xs;
	private String volp;
	private String hs;
	private String wp;
	private String ap;// 不知道
	private String op;
	private String priceMax;
	private String priceMin;
	private String cp;
	private String lb;
	private String syl;
	private String np;
	private String code;
	private String name;
	private String zhenf;
	private String time;
	private String ltsz;
	private String zsz;
	private String ddx60;
	private String ddy60;

	public String getLp() {
		return lp;
	}

	public void setLp(String lp) {
		this.lp = lp;
	}

	public String getZd() {
		return zd;
	}

	public void setZd(String zd) {
		this.zd = zd;
	}

	public String getZf() {
		return zf;
	}

	public void setZf(String zf) {
		this.zf = zf;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getXs() {
		return xs;
	}

	public void setXs(String xs) {
		this.xs = xs;
	}

	public String getVolp() {
		return volp;
	}

	public void setVolp(String volp) {
		this.volp = volp;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public String getWp() {
		return wp;
	}

	public void setWp(String wp) {
		this.wp = wp;
	}

	public String getAp() {
		return ap;
	}

	public void setAp(String ap) {
		this.ap = ap;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}

	public String getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(String priceMin) {
		this.priceMin = priceMin;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getSyl() {
		return syl;
	}

	public void setSyl(String syl) {
		this.syl = syl;
	}

	public String getNp() {
		return np;
	}

	public void setNp(String np) {
		this.np = np;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZhenf() {
		return zhenf;
	}

	public void setZhenf(String zhenf) {
		this.zhenf = zhenf;
	}

	public String getTime() {
		String[] split = time.split(" ");
		if (split != null) {
			return split[2];
		} else {
			return time;
		}
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLtsz() {
		return ltsz;
	}

	public void setLtsz(String ltsz) {
		this.ltsz = ltsz;
	}

	public String getZsz() {
		return zsz;
	}

	public void setZsz(String zsz) {
		this.zsz = zsz;
	}

	public String getDdx60() {
		return ddx60;
	}

	public void setDdx60(String ddx60) {
		this.ddx60 = ddx60;
	}

	public String getDdy60() {
		return ddy60;
	}

	public void setDdy60(String ddy60) {
		this.ddy60 = ddy60;
	}
}
