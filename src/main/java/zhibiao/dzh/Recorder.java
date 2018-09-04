package zhibiao.dzh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luonanqin on 4/17/15.
 */
public class Recorder {

	private static String field = "时间,当前价,涨跌额,涨跌幅,成交总手,成交金额,换手率,开盘价,最高价,最低价,昨收,量比,市盈率,60日DDX,60日DDY,委比,委差,外盘,内盘,"
			+ "买一价,买一手,买二价,买二手,买三价,买三手,买四价,买四手,买五价,买五手,卖一价,卖一手,卖二价,卖二手,卖三价,卖三手,卖四价,卖四手,卖五价,卖五手";
	public static String charset = "gbk";
	public static final String BASE_PATH = "/Users/Luonanqin/stock/data/";
	public static List<String> stockList = new ArrayList<String>();
	private static Map<String, Integer> proxys = new HashMap<String, Integer>();

	static {

		File stockName = new File(BASE_PATH + "stock.txt");
		// File stockName = new File(BASE_PATH + "stockname.txt");
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(stockName, "r");
			String str = null;
			while ((str = raf.readLine()) != null) {
				String[] split = str.split("=");
				byte[] bytes = split[1].getBytes("ISO-8859-1");
				// byte[] bytes = split[1].getBytes();
				String s = new String(bytes, "utf8");

				stockList.add(split[0] + "_" + s);
			}
			// stockList.clear();
			// stockList.add("603899_晨光文具");
			// stockList.add("600721_百花村");
			// stockList.add("600602_仪电电子");
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

		// proxys.put("119.90.127.2", 80);
		// proxys.put("202.108.50.75", 82);
		// proxys.put("124.88.67.13", 843);
		// proxys.put("124.88.67.13", 82);
		// proxys.put("111.186.100.150", 18186);
		// proxys.put("117.135.250.69", 80);

		proxys.put("120.198.243.115", 8088);
		// proxys.put("120.198.243.115", 8085);
	}

	public static void recordData(String date, String info, String stockCodeName) {
		String path = BASE_PATH + stockCodeName + File.separator + date + File.separator;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(path + date + ".csv");
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
				raf.write(field.getBytes(charset));
				raf.writeByte((byte) 0XA);
			} else {
				raf.skipBytes((int) length);
			}
			raf.write(info.toString().getBytes(charset));
			raf.writeByte((byte) 0XA);
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
