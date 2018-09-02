package zhibiao.base.mtm;

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
 * Created by Luonanqin on 4/26/15.
 */
public class MTM {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String FIELD = "日期,MTM,MAMTM";
	private static final int DAY_12_CYCLE = 12;
	private static final int DAY_6_CYCLE = 6;

	public static void main(String[] args) {
		MTM MTM = new MTM();
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = MTM.getPriceInfo(stockName);
			Deque<MTMData> mtmDatas = MTM.computeMTM(prices);
			MTM.recorder(mtmDatas, stockName);
		}
	}

	public void recorder(Deque<MTMData> wrDatas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "mtm.csv");
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
			for (MTMData wr : wrDatas) {
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

	public Deque<MTMData> computeMTM(Deque<HistoryPrice> prices) {
		Deque<Float> mtmList = new ArrayDeque<Float>();
		Deque<Float> mamtmList = new ArrayDeque<Float>();
		Deque<MTMData> mtms = new ArrayDeque<MTMData>();

		float mtm = 0;
		float mamtm = 0;

		float mamtmSum = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			Float price = hPrice.getPrice();

			mtmList.add(price);
			if (mtmList.size() > DAY_12_CYCLE) {
				mtm = price - mtmList.removeFirst();
				mamtmList.add(mtm);
				mamtmSum += mtm;
			}
			if (mamtmList.size() == DAY_6_CYCLE) {
				mamtm = mamtmSum / DAY_6_CYCLE;
			} else if (mamtmList.size() > DAY_6_CYCLE) {
				mamtmSum -= mamtmList.removeFirst();
				mamtm = mamtmSum / DAY_6_CYCLE;
			}

			if (first) {
				mtms.addFirst(new MTMData(hPrice.getDate(), "", ""));
				first = false;
				continue;
			}

			BigDecimal mtm_b = new BigDecimal(mtm);
			mtm_b = mtm_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal mamtm_b = new BigDecimal(mamtm);
			mamtm_b = mamtm_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			MTMData mtmData = new MTMData(hPrice.getDate(), mtm_b.toString(), mamtm_b.toString());
			mtms.addFirst(mtmData);
		}
		return mtms;
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
