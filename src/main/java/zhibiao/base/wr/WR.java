package zhibiao.base.wr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Luonanqin on 4/26/15.
 */
public class WR {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String FIELD = "日期,WR1,WR2";
	private static final int DAY_10_CYCLE = 10;
	private static final int DAY_6_CYCLE = 6;


	public Deque<WRData> computeWR(Deque<HistoryPrice> prices) {
		Deque<Float> max10List = new ArrayDeque<Float>();
		Deque<Float> min10List = new ArrayDeque<Float>();
		Deque<Float> max6List = new ArrayDeque<Float>();
		Deque<Float> min6List = new ArrayDeque<Float>();
		Deque<WRData> wrs = new ArrayDeque<WRData>();

		float max10 = Integer.MIN_VALUE;
		float min10 = Integer.MAX_VALUE;
		float max6 = Integer.MIN_VALUE;
		float min6 = Integer.MAX_VALUE;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			max10 = computeMax(DAY_10_CYCLE, max10List, max10, hPrice);
			min10 = computeMin(DAY_10_CYCLE, min10List, min10, hPrice);

			max6 = computeMax(DAY_6_CYCLE, max6List, max6, hPrice);
			min6 = computeMin(DAY_6_CYCLE, min6List, min6, hPrice);

			if (first) {
				wrs.addFirst(new WRData(hPrice.getDate(), "", ""));
				first = false;
				continue;
			}

			float wr1 = 100 * (max10 - hPrice.getPrice()) / (max10 - min10);
			float wr2 = 100 * (max6 - hPrice.getPrice()) / (max6 - min6);
			BigDecimal wr1_b = new BigDecimal(wr1);
			wr1_b = wr1_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal wr2_b = new BigDecimal(wr2);
			wr2_b = wr2_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			WRData wr = new WRData(hPrice.getDate(), wr1_b.toString(), wr2_b.toString());
			wrs.addFirst(wr);
		}
		return wrs;
	}

	private float computeMin(int dayCycle, Deque<Float> minList, float min, HistoryPrice hPrice) {
		float hPriceMin = hPrice.getMin();
		minList.add(hPriceMin);
		if (minList.size() <= dayCycle) {
			if (hPriceMin < min) {
				min = hPriceMin;
			}
		} else {
			float outMin = minList.removeFirst();
			if (min < outMin) {
				if (hPriceMin < min) {
					min = hPriceMin;
				}
			} else {
				min = Integer.MAX_VALUE;
				for (Float minPrice : minList) {
					if (minPrice < min) {
						min = minPrice;
					}
				}
			}
		}
		return min;
	}

	private float computeMax(int dayCycle, Deque<Float> maxList, float max, HistoryPrice hPrice) {
		float hPriceMax = hPrice.getMax();
		maxList.add(hPriceMax);
		if (maxList.size() <= dayCycle) {
			if (hPriceMax > max) {
				max = hPriceMax;
			}
		} else {
			float outMax = maxList.removeFirst();
			if (max > outMax) {
				if (hPriceMax > max) {
					max = hPriceMax;
				}
			} else {
				max = Integer.MIN_VALUE;
				for (Float maxPrice : maxList) {
					if (maxPrice > max) {
						max = maxPrice;
					}
				}
			}
		}
		return max;
	}


}
