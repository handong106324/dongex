package zhibiao.base.trix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Luonanqin on 5/7/15.
 */
public class TRIX {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String FIELD = "日期,TRIX,TRMA,EMA1,EMA2,EMA3";

	private static final int M1 = 12;
	private static final int M2 = 20;



	public Deque<TRIXData> computeTRIX(Deque<HistoryPrice> prices) {
		Deque<TRIXData> trixDatas = new ArrayDeque<TRIXData>();
		Deque<Float> trixList = new ArrayDeque<Float>();

		float lastEMA1 = 0;
		float lastEMA2 = 0;
		float lastEMA3 = 0;
		float lastTR = 0;
		float trix = 0;
		float trixSum = 0;
		float trma = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			float price = hPrice.getPrice();
			String date = hPrice.getDate();

			if (first) {
				lastEMA1 = price;
				lastEMA2 = price;
				lastEMA3 = price;
				lastTR = price;
				first = false;

				trixDatas.addFirst(new TRIXData(date, String.valueOf(price), "0", String.valueOf(price), String.valueOf(price), String.valueOf(price)));
				continue;
			}

			float ema1 = lastEMA1 * (M1 - 1) / (M1 + 1) + price * 2 / (M1 + 1);
			float ema2 = lastEMA2 * (M1 - 1) / (M1 + 1) + ema1 * 2 / (M1 + 1);
			float ema3 = lastEMA3 * (M1 - 1) / (M1 + 1) + ema2 * 2 / (M1 + 1);
			float tr = ema3;
			trix = (tr - lastTR) / lastTR * 100;

			trixSum += trix;
			trixList.add(trix);
			if (trixList.size() == M2) {
				trma = trixSum / M2;
			} else if (trixList.size() > M2) {
				trixSum -= trixList.removeFirst();
				trma = trixSum / M2;
			}

			BigDecimal trix_b = new BigDecimal(trix);
			if (trix < 1) {
				trix_b = trix_b.setScale(3, BigDecimal.ROUND_HALF_UP);
			} else {
				trix_b = trix_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal trma_b = new BigDecimal(trma);
			if (trma < 1) {
				trma_b = trma_b.setScale(3, BigDecimal.ROUND_HALF_UP);
			} else {
				trma_b = trma_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			trixDatas.addFirst(new TRIXData(date, trix_b.toString(), trma_b.toString(), String.valueOf(ema1), String.valueOf(ema2), String.valueOf(ema3)));

			lastEMA1 = ema1;
			lastEMA2 = ema2;
			lastEMA3 = ema3;
			lastTR = tr;

		}
		return trixDatas;
	}


}
