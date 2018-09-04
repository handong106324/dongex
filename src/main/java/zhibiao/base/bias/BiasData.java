package zhibiao.base.bias;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class BiasData {

	private String date;
	private String bias;

	public BiasData(String date, String bias) {
		this.date = date;
		this.bias = bias;
	}

	@Override
	public String toString() {
		return date + ',' + bias;
	}
}
