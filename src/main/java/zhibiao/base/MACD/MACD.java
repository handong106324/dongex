package zhibiao.base.MACD;


import zhibiao.dzh.Recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Luonanqin on 4/19/15.
 */
public class MACD {

	private double EMA12 = 0;
	private double EMA26 = 0;

	private double DIFF = 0;
	private double DEA = 0;

	private double MACD = 0;

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String BASE_DATA_PATH = "/Users/Luonanqin/stock/data/";
	private static final String field = "日期,macd,DIFF,DEA,EMA12,EMA26";

	public Deque<MacdData> computeStockMACD(Deque<HistoryPrice> historyPrices) {
		Deque<MacdData> macds = new ArrayDeque<MacdData>();
		boolean first = true;
		for (HistoryPrice hPrice : historyPrices) {
			String date = hPrice.getDate();
			double price = Double.valueOf(hPrice.getPrice());

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

				macdData.setEma12(EMA12);
				macdData.setEma26(EMA26);

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

			macds.addFirst(macdData);
		}
		return macds;
	}


	public void recorder(Deque<MacdData> macds, String stockCodeName) {
		String path = RECORD_PATH + stockCodeName + File.separator;
		File file = new File(path + "macd.csv");
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
			for (MacdData macd : macds) {
				raf.write(macd.toString().getBytes());
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

}
