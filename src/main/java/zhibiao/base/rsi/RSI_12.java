package zhibiao.base.rsi;

import stock.dzh.Recorder;

import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
// RSI12
public class RSI_12 {

	public static void main(String[] args) {
		RSI rsi_12 = new RSI(12);
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = rsi_12.getPriceInfo(stockName);
			Deque<RsiData> rsiDatas = rsi_12.computeRSI(prices);
			rsi_12.recorder(rsiDatas, stockName);
		}
	}
}
