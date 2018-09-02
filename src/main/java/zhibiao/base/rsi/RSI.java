package zhibiao.base.rsi;


import zhibiao.dzh.Recorder;

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
public class RSI {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static String field = "日期,";

	private int rsiCycle;

	public RSI(int rsiCycle) {
		this.rsiCycle = rsiCycle;
		field += ("rsi" + rsiCycle + ",sma_max,sma_abs");
	}

	public void recorder(Deque<RsiData> rsiDatas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "rsi_" + rsiCycle + ".csv");
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
				raf.write(field.getBytes(Recorder.charset));
				raf.writeByte((byte) 0XA);
			} else {
				raf.skipBytes((int) length);
			}
			for (RsiData bias : rsiDatas) {
				raf.write(bias.toString().getBytes());
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

	public Deque<RsiData> computeRSI(Deque<stock.base.rsi.HistoryPrice> prices) {
		Deque<RsiData> rsiDatas = new ArrayDeque<RsiData>();

		float lastSMAMax = 0;
		float lastSMAAbs = 0;
		float lastPrice = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			if (!first) {
				float priceDiffer = hPrice.getPrice() - lastPrice;
				if (priceDiffer < 0) {
					lastSMAMax = (rsiCycle - 1) * lastSMAMax / rsiCycle;
					lastSMAAbs = (Math.abs(priceDiffer) + (rsiCycle - 1) * lastSMAAbs) / rsiCycle;
				} else {
					lastSMAMax = (priceDiffer + (rsiCycle - 1) * lastSMAMax) / rsiCycle;
					lastSMAAbs = (priceDiffer + (rsiCycle - 1) * lastSMAAbs) / rsiCycle;
				}
			}

			RsiData rsiData;
			if (first) {
				rsiData = new RsiData(hPrice.getDate(), "0", "0", "0");
			} else if (lastSMAAbs == 0) {
				rsiData = new RsiData(hPrice.getDate(), "0", String.valueOf(lastSMAMax), "0");
			} else {
				float rsi = lastSMAMax / lastSMAAbs * 100;
				BigDecimal rsi_b = new BigDecimal(rsi);
				rsi_b = rsi_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				rsiData = new RsiData(hPrice.getDate(), rsi_b.toString(), String.valueOf(lastSMAMax), String.valueOf(lastSMAAbs));
			}
			rsiDatas.addFirst(rsiData);

			lastPrice = hPrice.getPrice();
			first = false;
		}
		return rsiDatas;
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
			String str = null;
			raf.readLine();
			while ((str = raf.readLine()) != null) {
				String[] split = str.split(",");

				String date = split[0];
				String priceStr = split[2];

				if (split != null) {
					historyPrices.addFirst(new HistoryPrice(date, Float.valueOf(priceStr)));
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
