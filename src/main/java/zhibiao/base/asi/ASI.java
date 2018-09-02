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

	public static void main(String[] args) {
		ASI asi = new ASI();
		for (String stockName : Recorder.stockList) {
			System.out.println(stockName);
			Deque<HistoryPrice> prices = asi.getPriceInfo(stockName);
			Deque<ASIData> asiDatas = asi.computeASI(prices);
			asi.recorder(asiDatas, stockName);
		}
	}

	public void recorder(Deque<ASIData> dmiDatas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "asi.csv");
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
			for (ASIData bias : dmiDatas) {
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
				String openStr = split[1];
				String priceStr = split[2];
				String minStr = split[5];
				String maxStr = split[6];

				if (split != null) {
					historyPrices
							.addFirst(new HistoryPrice(date, Float.valueOf(openStr), Float.valueOf(priceStr), Float.valueOf(maxStr), Float.valueOf(minStr)));
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
