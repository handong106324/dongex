package zhibiao.base.rsi;

import stock.dzh.Recorder;

import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
// RSI24
public class RSI_24 {

	public static void main(String[] args) {
		RSI rsi_24 = new RSI(24);
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = rsi_24.getPriceInfo(stockName);
			Deque<RsiData> rsiDatas = rsi_24.computeRSI(prices);
			rsi_24.recorder(rsiDatas, stockName);
		}
	}
}
