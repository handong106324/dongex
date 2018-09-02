package zhibiao.base;

import com.google.gson.Gson;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import stock.dzh.Recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luonanqin on 4/18/15.
 */
public class StockInMarket {

	private static final String BASE_PATH = "/Users/Luonanqin/stock/stockInMarket/";
	public static PoolingHttpClientConnectionManager connectionManager;
	public static CloseableHttpClient httpClient;
	public static Gson gson = new Gson();

	public static String uri_prefix = "http://quote.eastmoney.com/sh";
	public static String uri_suffix = ".html";

	public static Pattern p = Pattern.compile("上市时间：[0-9]{4}+\\-[0-9]{2}+\\-[0-9]{2}+");

	static {
		CrawlConfig config = new CrawlConfig();
		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(false).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
		  .setRedirectsEnabled(false).setSocketTimeout(50000).setConnectTimeout(50000).build();

		RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
		connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);

		Registry<ConnectionSocketFactory> connRegistry = connRegistryBuilder.build();
		connectionManager = new PoolingHttpClientConnectionManager(connRegistry);
		connectionManager.setMaxTotal(config.getMaxTotalConnections());
		connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerHost());

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setConnectionManager(connectionManager);

		httpClient = clientBuilder.build();
	}

	public static void main(String[] args) {
		Map<String, String> stockInMarkets = new HashMap<String, String>();

		for (String stockCodeName : Recorder.stockList) {
			System.out.println(stockCodeName);
			String stockCode = stockCodeName.substring(0, 6);
			String inMarketTime = getStockInMarketTime(uri_prefix + stockCode + uri_suffix);
			if (inMarketTime == null || inMarketTime.equals("")) {
				continue;
			}
			stockInMarkets.put(stockCode, inMarketTime);
		}
		recordInMarketTime(stockInMarkets);
	}

	private static void recordInMarketTime(Map<String, String> stockInMarkets) {
		File dir = new File(BASE_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(BASE_PATH + "inMarket.txt");
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
			for (String stockCode : stockInMarkets.keySet()) {
				String content = stockCode + "_" + stockInMarkets.get(stockCode);
				raf.write(content.getBytes());
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

	private static String getStockInMarketTime(String url) {
		HttpGet get = null;
		String inMarket = "";
		try {
			get = new HttpGet(url);

			HttpResponse resp = httpClient.execute(get);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resp.getEntity();
				byte[] response = EntityUtils.toByteArray(entity);

				String result = new String(response, "gbk");
				Matcher m = p.matcher(result);
				if (m.find()) {
					String temp = m.group();
					inMarket = temp.substring(5);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}
		return inMarket;
	}
}
