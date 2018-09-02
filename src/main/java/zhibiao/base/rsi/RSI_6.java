package zhibiao.base.rsi;

import stock.dzh.Recorder;

import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
// RSI6
public class RSI_6 {

	public static void main(String[] args) {
		RSI rsi_6 = new RSI(6);
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = rsi_6.getPriceInfo(stockName);
			Deque<RsiData> rsiDatas = rsi_6.computeRSI(prices);
			rsi_6.recorder(rsiDatas, stockName);
		}
	}
}
