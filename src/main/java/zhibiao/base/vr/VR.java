package zhibiao.base.vr;

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
public class VR {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String FIELD = "日期,VR";

	private static final long M1 = 26;



	public Deque<VRData> computeVR(Deque<HistoryPrice> prices) {
		Deque<VRData> vrDatas = new ArrayDeque<VRData>();
		Deque<Long> volsList1 = new ArrayDeque<Long>();
		Deque<Long> volsList2 = new ArrayDeque<Long>();

		long volsSum1 = 0;
		long volsSum2 = 0;
		float lastClose = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			float price = hPrice.getPrice();
			long vols = hPrice.getVols();

			if (first) {
				lastClose = price;
				first = false;
				continue;
			}

			if (price > lastClose) {
				volsSum1 += vols;
				volsList1.add(vols);
				volsList2.add(0L);
			} else {
				volsSum2 += vols;
				volsList2.add(vols);
				volsList1.add(0L);
			}

			if (volsList1.size() > M1) {
				volsSum1 -= volsList1.removeFirst();
				volsSum2 -= volsList2.removeFirst();
			}

			lastClose = price;

			String date = hPrice.getDate();
			if (volsSum2 == 0) {
				VRData vrData = new VRData(date, "");
				vrDatas.addFirst(vrData);
				continue;
			}
			float vr = (float) volsSum1 / (float) volsSum2 * 100;

			BigDecimal vr_b = new BigDecimal(vr);
			vr_b = vr_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			VRData vrData = new VRData(date, vr_b.toString());
			vrDatas.addFirst(vrData);

		}
		return vrDatas;
	}


}
