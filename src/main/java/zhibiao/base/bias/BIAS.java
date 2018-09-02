package zhibiao.base.bias;

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
public class BIAS {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static String field = "日期,";

	private int biasCycle;

	public BIAS(int biasCycle) {
		this.biasCycle = biasCycle;
		field += (biasCycle + "日乖离率");
	}

	public void recorder(Deque<BiasData> biasDatas, String stockName) {
		String path = RECORD_PATH + stockName + File.separator;
		File file = new File(path + "bias_" + biasCycle + ".csv");
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
			for (BiasData bias : biasDatas) {
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

	public Deque<BiasData> computeBIAS(Deque<HistoryPrice> prices) {
		Deque<Float> biasPrice = new ArrayDeque<Float>();
		Deque<BiasData> biases = new ArrayDeque<BiasData>();

		int i = 0;
		float sumPrice = 0;
		for (HistoryPrice hPrice : prices) {
			biasPrice.add(hPrice.getPrice());
			sumPrice += hPrice.getPrice();
			i++;
			float avgPrice;
			if (i == biasCycle) {
				avgPrice = sumPrice / biasCycle;
			} else if (i > biasCycle) {
				sumPrice -= biasPrice.removeFirst();
				avgPrice = sumPrice / biasCycle;
			} else {
				BiasData biasData = new BiasData(hPrice.getDate(), "0");
				biases.addFirst(biasData);
				continue;
			}

			float bias = (hPrice.getPrice() - avgPrice) / avgPrice * 100;
			BigDecimal bias_b = new BigDecimal(bias);
			bias_b = bias_b.setScale(2, BigDecimal.ROUND_HALF_UP);
			BiasData biasData = new BiasData(hPrice.getDate(), bias_b.toString());
			biases.addFirst(biasData);
		}
		return biases;
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
