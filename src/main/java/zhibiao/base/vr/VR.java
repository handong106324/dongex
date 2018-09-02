package zhibiao.base.vr;

import stock.dzh.Recorder;
import stock.sohu.HistorySpider;

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

	public static void main(String[] args) {
		VR vr = new VR();
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = vr.getPriceInfo(stockName);
			Deque<VRData> vrDatas = vr.computeVR(prices);
			vr.recorder(vrDatas, stockName);
		}
	}

	public void recorder(Deque<VRData> wrDatas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "vr.csv");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rw");
			long length = raf.length();
			if (length == 0) {
				raf.write(FIELD.getBytes(Recorder.charset));
				raf.writeByte((byte) 0XA);
			} else {
				raf.skipBytes((int) length);
			}
			for (VRData wr : wrDatas) {
				raf.write(wr.toString().getBytes());
				raf.writeByte((byte) 0XA);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

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

	public Deque<HistoryPrice> getPriceInfo(String stockName) {
		Deque<HistoryPrice> historyPrices = new ArrayDeque<HistoryPrice>();
		String path = Recorder.BASE_PATH + stockName + File.separator;
		File file = new File(path + HistorySpider.HISTORY_FILE);
		if (!file.exists()) {
			return historyPrices;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			String str;
			raf.readLine();
			while ((str = raf.readLine()) != null) {
				String[] split = str.split(",");

				String date = split[0];
				String priceStr = split[2];
				String volsStr = split[7];

				if (split != null) {
					historyPrices.addFirst(new HistoryPrice(date, Float.valueOf(priceStr), Long.valueOf(volsStr)));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return historyPrices;
	}
}
