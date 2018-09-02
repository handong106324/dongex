package zhibiao.base.kdj;


import zhibiao.base.MACD.HistoryPrice;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Luonanqin on 4/19/15.
 */
public class KDJ {

	private float RSV = 0;

	private float lastK = 0;
	private float lastD = 0;

	private float minPrice = Integer.MAX_VALUE;
	private float maxPrice = Integer.MIN_VALUE;

	private Deque<Float> nineDayMin = new ArrayDeque<Float>(9);
	private Deque<Float> nineDayMax = new ArrayDeque<Float>(9);

	public Deque<KdjData> computeStockKDJ(Deque<HistoryPrice> historyPrices) {
		Deque<KdjData> kdjs = new ArrayDeque<KdjData>();
		boolean first = true;
		for (HistoryPrice hPrice : historyPrices) {
			String date = hPrice.getDate();
			float price = Float.valueOf(hPrice.getPrice());
			float min = Float.valueOf(hPrice.getMinPrice());
			float max = Float.valueOf(hPrice.getMaxPrice());

			nineDayMax.add(max);
			nineDayMin.add(min);
			if (nineDayMin.size() > 9) {
				Float removeMax = nineDayMax.removeFirst();
				if (removeMax == maxPrice) {
					if (max >= removeMax) {
						maxPrice = max;
					} else {
						// 遍历一次
						maxPrice = Integer.MIN_VALUE;
						for (Float nine : nineDayMax) {
							if (nine > maxPrice) {
								maxPrice = nine;
							}
						}
					}
				} else {
					if (max > maxPrice) {
						maxPrice = max;
					}
				}
				Float removeMin = nineDayMin.removeFirst();
				if (removeMin == minPrice) {
					if (min <= removeMin) {
						minPrice = min;
					} else {
						// 遍历一次
						minPrice = Integer.MAX_VALUE;
						for (Float nine : nineDayMin) {
							if (nine < minPrice) {
								minPrice = nine;
							}
						}
					}
				} else {
					if (min < minPrice) {
						minPrice = min;
					}
				}
			} else {
				if (max > maxPrice) {
					maxPrice = max;
				}
				if (min < minPrice) {
					minPrice = min;
				}
			}

			KdjData kdj = new KdjData();
			kdj.setDate(date);
			if (first) {
				kdj.setK("0");
				kdj.setD("0");
				kdj.setJ("0");
				kdj.setLastD("0");
				kdj.setLastK("0");

				first = false;
			} else {

				/**
				 * n日RSV=（Cn－Ln）/（Hn－Ln）×100
				 公式中，Cn为第n日收盘价；Ln为n日内的最低价；Hn为n日内的最高价。
				 其次，计算K值与D值：
				 当日K值=2/3×前一日K值+1/3×当日RSV
				 当日D值=2/3×前一日D值+1/3×当日K值
				 若无前一日K 值与D值，则可分别用50来代替。
				 J值=3*当日K值-2*当日D值
				 以9日为周期的KD线为例，即未成熟随机值，计算公式为
				 9日RSV=（C－L9）÷（H9－L9）×100
				 公式中，C为第9日的收盘价；L9为9日内的最低价；H9为9日内的最高价。
				 K值=2/3×第8日K值+1/3×第9日RSV
				 D值=2/3×第8日D值+1/3×第9日K值
				 J值=3*第9日K值-2*第9日D值
				 若无前一日K
				 值与D值，则可以分别用50代替
				 */

				RSV = (price - minPrice) / (maxPrice - minPrice) * 100;
				float k = 2 * lastK / 3 + 1 * RSV / 3;
				float d = 2 * lastD / 3 + 1 * k / 3;
				float j = 3 * k - 2 * d;

				lastK = k;
				lastD = d;

				kdj.setLastK(String.valueOf(lastK));
				kdj.setLastD(String.valueOf(lastD));

				BigDecimal K_B = new BigDecimal(k);
				K_B = K_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				kdj.setK(K_B.toString());
				BigDecimal D_B = new BigDecimal(d);
				D_B = D_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				kdj.setD(D_B.toString());
				if (j < 100 && j > 0) {
					BigDecimal J_B = new BigDecimal(j);
					J_B = J_B.setScale(2, BigDecimal.ROUND_HALF_UP);
					kdj.setJ(J_B.toString());
				} else if (j > 100) {
					kdj.setJ("100.0");
				} else if (j < 0) {
					kdj.setJ("0");
				}
			}

			kdjs.addFirst(kdj);
		}
		return kdjs;
	}


}
