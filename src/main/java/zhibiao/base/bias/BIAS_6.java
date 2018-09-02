package zhibiao.base.bias;

import stock.dzh.Recorder;

import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
// 6日乖离率
public class BIAS_6 {

	public static void main(String[] args) {
		BIAS bias_6 = new BIAS(6);
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = bias_6.getPriceInfo(stockName);
			Deque<BiasData> maDatas = bias_6.computeBIAS(prices);
			bias_6.recorder(maDatas, stockName);
		}
	}
}
