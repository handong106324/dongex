package zhibiao.base.boll;

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
public class BOLL {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String FIELD = "日期,UPPER,MID,LOWER";

	private static final int MA_CYCLE = 20;
	private static final int P = 2;

	public static void main(String[] args) {
		BOLL bool = new BOLL();
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = bool.getPriceInfo(stockName);
			Deque<BOLLData> bollDatas = bool.computeBOLL(prices);
			bool.recorder(bollDatas, stockName);
		}
	}

	public void recorder(Deque<BOLLData> wrDatas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "boll.csv");
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
			for (BOLLData wr : wrDatas) {
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

	public Deque<BOLLData> computeBOLL(Deque<HistoryPrice> prices) {
		Deque<Float> bollMA = new ArrayDeque<Float>();
		Deque<BOLLData> bollDatas = new ArrayDeque<BOLLData>();

		float bollSum = 0;
		float bollAvg = 0;
		for (HistoryPrice hPrice : prices) {
			Float price = hPrice.getPrice();

			bollMA.add(price);
			bollSum += price;
			if (bollMA.size() == MA_CYCLE) {
				bollAvg = bollSum / MA_CYCLE;
			} else if (bollMA.size() > MA_CYCLE) {
				bollSum -= bollMA.removeFirst();
				bollAvg = bollSum / MA_CYCLE;
			} else {
				BOLLData BOLLData = new BOLLData(hPrice.getDate(), "0", "0", "0");
				bollDatas.addFirst(BOLLData);
				continue;
			}

			float diffSum = 0;
			for (Float close : bollMA) {
				diffSum += Math.pow(close - bollAvg, 2);
			}
			// 方差
			float s2 = diffSum / (MA_CYCLE + 1);
			// 标准差
			float std = (float) Math.sqrt(s2);

			float upper = bollAvg + P * std;
			float lower = bollAvg - P * std;

			BigDecimal upper_b = new BigDecimal(upper);
			upper_b = upper_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bollAvg_b = new BigDecimal(bollAvg);
			bollAvg_b = bollAvg_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal lower_b = new BigDecimal(lower);
			lower_b = lower_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BOLLData bollData = new BOLLData(hPrice.getDate(), upper_b.toString(), bollAvg_b.toString(), lower_b.toString());
			bollDatas.addFirst(bollData);
		}
		return bollDatas;
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
