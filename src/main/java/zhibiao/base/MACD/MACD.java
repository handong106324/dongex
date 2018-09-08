package zhibiao.base.MACD;


import com.huobi.response.Kline;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Luonanqin on 4/19/15.
 */
public class MACD {

	private double EMA12 = 0;
	private double EMA26 = 0;

	private double DIFF = 0;
	private double DEA = 0;

	private double MACD = 0;


	public List<MacdData> computeStockMACD(List<Kline> historyPrices) {
		List<MacdData> macds = new ArrayList<>();
		boolean first = true;
		for (Kline hPrice : historyPrices) {
			String date = DateFormatUtils.format(new Date(hPrice.getTimestamp()), "yyyy-MM-dd HH:mm:ss");
			double price = Double.valueOf(hPrice.getClose());

			MacdData macdData = new MacdData();
			macdData.setDate(date);
			if (first) {
				macdData.setMacd("0");
				macdData.setDiff("0");
				macdData.setDea("0");
				EMA12 = price;
				EMA26 = price;
				first = false;
			} else {
				EMA12 = EMA12 * 11 / 13 + price * 2 / 13;
				EMA26 = EMA26 * 25 / 27 + price * 2 / 27;
				DIFF = EMA12 - EMA26;
				DEA = DEA * 8 / 10 + DIFF * 2 / 10;
				MACD = 2 * (DIFF - DEA);


				BigDecimal DIFF_B = new BigDecimal(DIFF);
				if (DIFF > 1) {
					DIFF_B = DIFF_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					DIFF_B = DIFF_B.setScale(3, BigDecimal.ROUND_HALF_UP);
				}
				macdData.setDiff(DIFF_B.toString());
				BigDecimal DEA_B = new BigDecimal(DEA);
				if (DEA > 1) {
					DEA_B = DEA_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					DEA_B = DEA_B.setScale(3, BigDecimal.ROUND_HALF_UP);
				}
				macdData.setDea(DEA_B.toString());
				BigDecimal MACD_B = new BigDecimal(MACD);
				if (MACD > 1) {
					MACD_B = MACD_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					MACD_B = MACD_B.setScale(3, BigDecimal.ROUND_HALF_UP);
				}
				macdData.setMacd(MACD_B.toString());
			}

			macds.add(macdData);
		}
		return macds;
	}

}
