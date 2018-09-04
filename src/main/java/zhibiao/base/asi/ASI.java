package zhibiao.base.asi;


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
public class ASI {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final int ASI_SUM_CYCLE = 26;
	private static final int ASI_MA = 10;
	private static String field = "日期,ASI,ASIT";


	public Deque<ASIData> computeASI(Deque<HistoryPrice> prices) {
		Deque<ASIData> asiDatas = new ArrayDeque<ASIData>();
		Deque<Float> sis = new ArrayDeque<Float>();
		Deque<Float> asis = new ArrayDeque<Float>();

		float lastClose = 0;
		float lastMin = 0;
		float lastOpen = 0;
		float asiSum = 0;
		float asi = 0;
		float asit = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			float open = hPrice.getOpen();
			float close = hPrice.getClose();
			float max = hPrice.getMax();
			float min = hPrice.getMin();
			String date = hPrice.getDate();

			if (first) {
				lastClose = close;
				lastOpen = open;
				lastMin = min;
				asis.add(0f);
				sis.add(0f);
				asiDatas.add(new ASIData(date, "0", ""));

				first = false;
				continue;
			} else {
				float aa = Math.abs(max - lastClose);
				float bb = Math.abs(min - lastClose);
				float cc = Math.abs(max - lastMin);
				float dd = Math.abs(lastClose - lastOpen);

				float r;
				if (aa > bb && aa > cc) {
					r = aa + bb / 2 + dd / 4;
				} else {
					if (bb > cc && bb > aa) {
						r = bb + aa / 2 + dd / 4;
					} else {
						r = cc + dd / 4;
					}
				}
				float x = close - lastClose + (close - open) / 2 + lastClose - lastOpen;
				float si = 16 * x / r * Math.max(aa, bb);

				asi += si;
				sis.add(si);
				if (sis.size() > ASI_SUM_CYCLE) {
					asi -= sis.removeFirst();
				}

				asis.add(asi);
				asiSum += asi;
				if (asis.size() == ASI_MA) {
					asit = asiSum / ASI_MA;
				} else if (asis.size() > ASI_MA) {
					asiSum -= asis.removeFirst();
					asit = asiSum / ASI_MA;
				}

				lastClose = close;
				lastOpen = open;
				lastMin = min;
			}

			BigDecimal asi_b = new BigDecimal(asi);
			asi_b = asi_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (asis.size() < ASI_MA) {
				asiDatas.addFirst(new ASIData(date, asi_b.toString(), ""));
			} else {
				BigDecimal asit_b = new BigDecimal(asit);
				asit_b = asit_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				asiDatas.addFirst(new ASIData(date, asi_b.toString(), asit_b.toString()));
			}
		}
		return asiDatas;
	}


}
