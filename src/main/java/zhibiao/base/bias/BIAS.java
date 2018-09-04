package zhibiao.base.bias;

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

}
