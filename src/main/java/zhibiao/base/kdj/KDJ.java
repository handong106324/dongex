package zhibiao.base.kdj;


import com.huobi.response.Kline;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Created by Luonanqin on 4/19/15.
 */
public class KDJ {

	private double RSV = 0;

	private double lastK = 0;
	private double lastD = 0;

	private Double minPrice = Double.MAX_VALUE;
	private Double maxPrice = Double.MIN_VALUE;

	private Deque<Double> nineDayMin = new ArrayDeque<Double>(9);
	private Deque<Double> nineDayMax = new ArrayDeque<Double>(9);

	public Deque<KdjData> computeStockKDJ(List<Kline> historyPrices) {
		Deque<KdjData> kdjs = new ArrayDeque<KdjData>();
		boolean first = true;
		for (Kline hPrice : historyPrices) {
			double price = hPrice.getClose();
			double min = hPrice.getLow();
			double max = hPrice.getHigh();

			nineDayMax.add(max);
			nineDayMin.add(min);
			if (nineDayMin.size() > 9) {
				Double removeMax = nineDayMax.removeFirst();
				if (removeMax == maxPrice) {
					if (max >= removeMax) {
						maxPrice = max;
					} else {
						// 遍历一次
						maxPrice = Double.MIN_VALUE;
						for (Double nine : nineDayMax) {
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
				Double removeMin = nineDayMin.removeFirst();
				if (removeMin == minPrice) {
					if (min <= removeMin) {
						minPrice = min;
					} else {
						// 遍历一次
						minPrice = Double.MAX_VALUE;
						for (Double nine : nineDayMin) {
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
				Double k = 2 * lastK / 3 + 1 * RSV / 3;
				Double d = 2 * lastD / 3 + 1 * k / 3;
				Double j = 3 * k - 2 * d;

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
