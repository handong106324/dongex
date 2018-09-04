package zhibiao.base.rsi;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class RSI {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static String field = "日期,";

	private int rsiCycle;

	public RSI(int rsiCycle) {
		this.rsiCycle = rsiCycle;
		field += ("rsi" + rsiCycle + ",sma_max,sma_abs");
	}


	public Deque<RsiData> computeRSI(Deque<HistoryPrice> prices) {
		Deque<RsiData> rsiDatas = new ArrayDeque<RsiData>();

		float lastSMAMax = 0;
		float lastSMAAbs = 0;
		float lastPrice = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			if (!first) {
				float priceDiffer = hPrice.getPrice() - lastPrice;
				if (priceDiffer < 0) {
					lastSMAMax = (rsiCycle - 1) * lastSMAMax / rsiCycle;
					lastSMAAbs = (Math.abs(priceDiffer) + (rsiCycle - 1) * lastSMAAbs) / rsiCycle;
				} else {
					lastSMAMax = (priceDiffer + (rsiCycle - 1) * lastSMAMax) / rsiCycle;
					lastSMAAbs = (priceDiffer + (rsiCycle - 1) * lastSMAAbs) / rsiCycle;
				}
			}

			RsiData rsiData;
			if (first) {
				rsiData = new RsiData(hPrice.getDate(), "0", "0", "0");
			} else if (lastSMAAbs == 0) {
				rsiData = new RsiData(hPrice.getDate(), "0", String.valueOf(lastSMAMax), "0");
			} else {
				float rsi = lastSMAMax / lastSMAAbs * 100;
				BigDecimal rsi_b = new BigDecimal(rsi);
				rsi_b = rsi_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				rsiData = new RsiData(hPrice.getDate(), rsi_b.toString(), String.valueOf(lastSMAMax), String.valueOf(lastSMAAbs));
			}
			rsiDatas.addFirst(rsiData);

			lastPrice = hPrice.getPrice();
			first = false;
		}
		return rsiDatas;
	}

}
