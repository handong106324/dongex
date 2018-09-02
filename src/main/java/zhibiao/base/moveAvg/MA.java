package zhibiao.base.moveAvg;

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
public class MA {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static String field = "日期,";

	private int maCycle;

	public MA(int maCycle) {
		this.maCycle = maCycle;
		field += (maCycle + "日均线值");
	}

	public void recorder(Deque<MaData> mas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "ma_" + maCycle + ".csv");
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
			for (MaData ma : mas) {
				raf.write(ma.toString().getBytes());
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
