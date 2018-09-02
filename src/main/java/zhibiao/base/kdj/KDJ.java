package zhibiao.base.kdj;


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
public class KDJ {

	private float RSV = 0;

	private float lastK = 0;
	private float lastD = 0;

	private float minPrice = Integer.MAX_VALUE;
	private float maxPrice = Integer.MIN_VALUE;

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final String BASE_DATA_PATH = "/Users/Luonanqin/stock/data/";
	private static final String field = "日期,K,D,J,lastK,lastD";

	private Deque<Float> nineDayMin = new ArrayDeque<Float>(9);
	private Deque<Float> nineDayMax = new ArrayDeque<Float>(9);

	public Deque<KdjData> computeStockKDJ(Deque<HistoryPrice> historyPrices) {
		Deque<KdjData> kdjs = new ArrayDeque<KdjData>();
		boolean first = true;
		for (HistoryPrice hPrice : historyPrices) {
			String date = hPrice.getDate();
			float price = Float.valueOf(hPrice.getPrice());
			float min = Float.valueOf(hPrice.getMinPrice());
			float max = Float.valueOf(hPrice.getMaxPrice());

			nineDayMax.add(max);
			nineDayMin.add(min);
			if (nineDayMin.size() > 9) {
				Float removeMax = nineDayMax.removeFirst();
				if (removeMax == maxPrice) {
					if (max >= removeMax) {
						maxPrice = max;
					} else {
						// 遍历一次
						maxPrice = Integer.MIN_VALUE;
						for (Float nine : nineDayMax) {
							if (nine > maxPrice) {
								maxPrice = nine;
							}
						}
					}
				} else {
					if (max > maxPrice) {
						maxPrice = max;
					}
				}
				Float removeMin = nineDayMin.removeFirst();
				if (removeMin == minPrice) {
					if (min <= removeMin) {
						minPrice = min;
					} else {
						// 遍历一次
						minPrice = Integer.MAX_VALUE;
						for (Float nine : nineDayMin) {
							if (nine < minPrice) {
								minPrice = nine;
							}
						}
					}
				} else {
					if (min < minPrice) {
						minPrice = min;
					}
				}
			} else {
				if (max > maxPrice) {
					maxPrice = max;
				}
				if (min < minPrice) {
					minPrice = min;
				}
			}

			KdjData kdj = new KdjData();
			kdj.setDate(date);
			if (first) {
				kdj.setK("0");
				kdj.setD("0");
				kdj.setJ("0");
				kdj.setLastD("0");
				kdj.setLastK("0");

				first = false;
			} else {
				RSV = (price - minPrice) / (maxPrice - minPrice) * 100;
				float k = 2 * lastK / 3 + 1 * RSV / 3;
				float d = 2 * lastD / 3 + 1 * k / 3;
				float j = 3 * k - 2 * d;

//				System.out.println(RSV);
//				System.out.println(lastK);
//				System.out.println(lastD);
				lastK = k;
				lastD = d;

				kdj.setLastK(String.valueOf(lastK));
				kdj.setLastD(String.valueOf(lastD));

				BigDecimal K_B = new BigDecimal(k);
				K_B = K_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				kdj.setK(K_B.toString());
				BigDecimal D_B = new BigDecimal(d);
				D_B = D_B.setScale(2, BigDecimal.ROUND_HALF_UP);
				kdj.setD(D_B.toString());
				if (j < 100 && j > 0) {
					BigDecimal J_B = new BigDecimal(j);
					J_B = J_B.setScale(2, BigDecimal.ROUND_HALF_UP);
					kdj.setJ(J_B.toString());
				} else if (j > 100) {
					kdj.setJ("100.0");
				} else if (j < 0) {
					kdj.setJ("0");
				}
			}

			kdjs.addFirst(kdj);
		}
		return kdjs;
	}

	public Deque<HistoryPrice> getLastInfo(String stockCodeName) {
		Deque<HistoryPrice> historyPrices = new ArrayDeque<HistoryPrice>();
		String path = BASE_DATA_PATH + stockCodeName + File.separator;
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

				if (split != null) {
					HistoryPrice hPrice = new HistoryPrice();
					hPrice.setDate(split[0]);
					hPrice.setPrice(split[2]);
					hPrice.setMinPrice(split[5]);
					hPrice.setMaxPrice(split[6]);
					hPrice.setStock(stockCodeName);

					historyPrices.addFirst(hPrice);
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

	public void recorder(Deque<KdjData> kdjDatas, String stockCodeName) {
		String path = RECORD_PATH + stockCodeName + File.separator;
		File file = new File(path + "kdj.csv");
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
			for (KdjData kdjData : kdjDatas) {
				raf.write(kdjData.toString().getBytes());
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

	public static void main(String[] args) {
		KDJ kdj = new KDJ();
		for (String stockCodeName : Recorder.stockList) {
			System.out.println(stockCodeName);
			Deque<HistoryPrice> lastPrices = kdj.getLastInfo(stockCodeName);
			Deque<KdjData> kdjDatas = kdj.computeStockKDJ(lastPrices);
			kdj.recorder(kdjDatas, stockCodeName);
		}
	}

}
