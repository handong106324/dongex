package zhibiao.dzh;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Luonanqin on 4/7/15.
 */
public class DzhRealTimeSpider2 {

	private static Gson gson = new Gson();
	private static String date;
	private static final String BASE_PATH = "/Users/Luonanqin/stock/data2/";

	private static List<String> stockList = new ArrayList<String>();
	private static String uri_prefix = "http://cj.gw.com.cn/img/bd/stockData/SH/";
	private static String stock_uri_suffix = "JYMX.json";
	private static String other_uri_suffix = "5DPK.json";

	private static CloseableHttpClient httpClient;

	public static void main(String[] args) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		date = sdf.format(new Date(System.currentTimeMillis()));
		int i = 1;

		while (i <= 10) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println(e);
				break;
			}
			// for (String proxyIP : proxys.keySet()) {
			System.out.println(i + ": " + new Date(System.currentTimeMillis()));

			httpClient = HttpClients.createDefault();

			for (String stockCodeName : stockList) {
				String tmp = stockCodeName.substring(4, 6);
				int codeSuffix = Integer.valueOf(tmp);
				String stockCode = stockCodeName.substring(0, 6);
				StringBuffer stockData = getStockData(uri_prefix + codeSuffix + "/SH" + stockCode + "/SH" + stockCode + stock_uri_suffix);
				if (stockData == null || stockData.length() == 0) {
					continue;
				}
				StringBuffer otherData = getOtherData(uri_prefix + codeSuffix + "/SH" + stockCode + "/SH" + stockCode + other_uri_suffix);
				if (otherData == null || otherData.length() == 0) {
					continue;
				}
				stockData.append(otherData);
				String filePath = BASE_PATH + stockCodeName + File.separator + date + File.separator;
				Recorder.recordData(date, stockData.toString(), filePath);
				System.out.println(stockCodeName);
			}
			httpClient.close();
			i++;
			// }
		}
	}

	private static StringBuffer getStockData(String uri) throws IOException {
		HttpGet get = new HttpGet(uri);
		// try {
		// get.setURI(new URI(uri, true));
		// } catch (URIException e) {
		// e.printStackTrace();
		// }
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		StringBuffer sb = new StringBuffer();
		CloseableHttpResponse resp = null;
		try {
			resp = httpClient.execute(get);

			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resp.getEntity();
				InputStream content = entity.getContent();
				byte[] response = new byte[(int) entity.getContentLength()];
				content.read(response);

				String result = new String(response, "utf-8");
				StringBuffer json = new StringBuffer(result);

				json.delete(0, 2);
				json.delete(json.length() - 2, json.length());

				String[] split = json.toString().split("},");
				if (split[0].endsWith("}")) {
					return sb;
				}
				DzhStockData dzhStockData = gson.fromJson(split[0] + "}", DzhStockData.class);
				sb.append(dzhStockData.getTime() + ",");
				sb.append(dzhStockData.getLp() + ",");
				sb.append(dzhStockData.getZd() + ",");
				sb.append(dzhStockData.getZf() + ",");
				sb.append(dzhStockData.getVol() + ",");
				sb.append(dzhStockData.getVolp() + ",");
				sb.append(dzhStockData.getHs() + ",");
				sb.append(dzhStockData.getOp() + ",");
				sb.append(dzhStockData.getPriceMax() + ",");
				sb.append(dzhStockData.getPriceMin() + ",");
				sb.append(dzhStockData.getCp() + ",");
				sb.append(dzhStockData.getLb() + ",");
				sb.append(dzhStockData.getSyl() + ",");
				// sb.append(dzhStockData.getLtsz() + ",");
				// sb.append(dzhStockData.getZsz() + ",");
				sb.append(dzhStockData.getDdx60() + ",");
				sb.append(dzhStockData.getDdy60() + ",");
			}
		} catch (Exception e) {
			System.err.println(uri);
			// System.err.println("getStockData" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (resp != null) {
				resp.close();
			}
			get.releaseConnection();
			// httpClient.close();
		}
		return sb;
	}

	private static StringBuffer getOtherData(String uri) throws IOException {
		HttpGet get = new HttpGet(uri);

		// try {
		// get.setURI(new URI(uri, true));
		// } catch (URIException e) {
		// e.printStackTrace();
		// }
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		StringBuffer sb = new StringBuffer();
		CloseableHttpResponse resp = null;
		try {
			resp = httpClient.execute(get);

			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resp.getEntity();
				byte[] response = new byte[(int) entity.getContentLength()];
				InputStream content = entity.getContent();
				content.read(response);

				String result = new String(response, "utf-8");
				StringBuffer json = new StringBuffer(result);

				json.delete(0, 2);
				json.delete(json.length() - 2, json.length());

				DzhOtherData dzhOtherData = gson.fromJson(json.toString(), DzhOtherData.class);
				// System.out.println(json);
				sb.append(dzhOtherData.getWb() + ",");
				sb.append(dzhOtherData.getWc() + ",");
				sb.append(dzhOtherData.getWp() + ",");
				sb.append(dzhOtherData.getNp() + ",");
				sb.append(dzhOtherData.getB1p() + ",");
				sb.append(dzhOtherData.getB1v() + ",");
				sb.append(dzhOtherData.getB2p() + ",");
				sb.append(dzhOtherData.getB2v() + ",");
				sb.append(dzhOtherData.getB3p() + ",");
				sb.append(dzhOtherData.getB3v() + ",");
				sb.append(dzhOtherData.getB4p() + ",");
				sb.append(dzhOtherData.getB4v() + ",");
				sb.append(dzhOtherData.getB5p() + ",");
				sb.append(dzhOtherData.getB5v() + ",");
				sb.append(dzhOtherData.getS1p() + ",");
				sb.append(dzhOtherData.getS1v() + ",");
				sb.append(dzhOtherData.getS2p() + ",");
				sb.append(dzhOtherData.getS2v() + ",");
				sb.append(dzhOtherData.getS3p() + ",");
				sb.append(dzhOtherData.getS3v() + ",");
				sb.append(dzhOtherData.getS4p() + ",");
				sb.append(dzhOtherData.getS4v() + ",");
				sb.append(dzhOtherData.getS5p() + ",");
				sb.append(dzhOtherData.getS5v() + ",");
			}
		} catch (Exception e) {
			System.err.println(uri);
			System.err.println("getOtherData" + e.getMessage());
		} finally {
			if (resp != null) {
				resp.close();
			}
			get.releaseConnection();
			// httpClient.close();
		}
		return sb;
	}

}
