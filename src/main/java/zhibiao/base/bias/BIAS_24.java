package zhibiao.base.bias;

import stock.dzh.Recorder;

import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
// 24日乖离率
public class BIAS_24 {

	public static void main(String[] args) {
		BIAS bias_24 = new BIAS(24);
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = bias_24.getPriceInfo(stockName);
			Deque<BiasData> maDatas = bias_24.computeBIAS(prices);
			bias_24.recorder(maDatas, stockName);
		}
	}
}
