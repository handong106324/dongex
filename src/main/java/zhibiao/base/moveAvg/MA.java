package zhibiao.base.moveAvg;

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
public class MA {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static String field = "日期,";

	private int maCycle;

	public MA(int maCycle) {
		this.maCycle = maCycle;
		field += (maCycle + "日均线值");
	}



	public Deque<MaData> computeMA(Deque<HistoryPrice> prices) {
		Deque<Float> maPrice = new ArrayDeque<Float>();
		Deque<MaData> mas = new ArrayDeque<MaData>();

		int i = 0;
		float sumMa = 0;
		for (HistoryPrice price : prices) {
			maPrice.add(price.getPrice());
			sumMa += price.getPrice();
			i++;
			float ma;
			if (i == maCycle) {
				ma = sumMa / maCycle;
			} else if (i > maCycle) {
				sumMa -= maPrice.removeFirst();
				ma = sumMa / maCycle;
			} else {
				MaData maData = new MaData(price.getDate(), "0");
				mas.addFirst(maData);
				continue;
			}

			BigDecimal ma_b = new BigDecimal(ma);
			ma_b = ma_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			MaData maData = new MaData(price.getDate(), ma_b.toString());
			mas.addFirst(maData);
		}
		return mas;
	}

}
