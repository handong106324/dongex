package zhibiao.base.boll;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Luonanqin on 5/7/15.
 */
public class BOLL {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String FIELD = "日期,UPPER,MID,LOWER";

	private static final int MA_CYCLE = 20;
	private static final int P = 2;

	public Deque<BOLLData> computeBOLL(Deque<HistoryPrice> prices) {
		Deque<Float> bollMA = new ArrayDeque<Float>();
		Deque<BOLLData> bollDatas = new ArrayDeque<BOLLData>();

		float bollSum = 0;
		float bollAvg = 0;
		for (HistoryPrice hPrice : prices) {
			Float price = hPrice.getPrice();

			bollMA.add(price);
			bollSum += price;
			if (bollMA.size() == MA_CYCLE) {
				bollAvg = bollSum / MA_CYCLE;
			} else if (bollMA.size() > MA_CYCLE) {
				bollSum -= bollMA.removeFirst();
				bollAvg = bollSum / MA_CYCLE;
			} else {
				BOLLData BOLLData = new BOLLData(hPrice.getDate(), "0", "0", "0");
				bollDatas.addFirst(BOLLData);
				continue;
			}

			float diffSum = 0;
			for (Float close : bollMA) {
				diffSum += Math.pow(close - bollAvg, 2);
			}
			// 方差
			float s2 = diffSum / (MA_CYCLE + 1);
			// 标准差
			float std = (float) Math.sqrt(s2);

			float upper = bollAvg + P * std;
			float lower = bollAvg - P * std;

			BigDecimal upper_b = new BigDecimal(upper);
			upper_b = upper_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bollAvg_b = new BigDecimal(bollAvg);
			bollAvg_b = bollAvg_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal lower_b = new BigDecimal(lower);
			lower_b = lower_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BOLLData bollData = new BOLLData(hPrice.getDate(), upper_b.toString(), bollAvg_b.toString(), lower_b.toString());
			bollDatas.addFirst(bollData);
		}
		return bollDatas;
	}


}
