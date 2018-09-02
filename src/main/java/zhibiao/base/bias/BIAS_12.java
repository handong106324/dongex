package zhibiao.base.bias;

import stock.dzh.Recorder;

import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
// 12日乖离率
public class BIAS_12 {

	public static void main(String[] args) {
		BIAS bias_12 = new BIAS(12);
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = bias_12.getPriceInfo(stockName);
			Deque<BiasData> maDatas = bias_12.computeBIAS(prices);
			bias_12.recorder(maDatas, stockName);
		}
	}
}
