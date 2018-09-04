package zhibiao.base.dmi;

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
public class DMI {

	private static final String RECORD_PATH = "/Users/Luonanqin/stock/data/";
	private static final int DAY_6 = 6;
	private static final int DAY_14 = 14;
	private static String field = "日期,上升指标(+DI),下降指标(-DI),动向平均数(ADX),评估数值(ADXR),真是波幅(TR),上升动向值(+DI),下降动向值(-DI)";

	public Deque<DmiData> computeDMI(Deque<HistoryPrice> prices) {
		Deque<DmiData> dmiDatas = new ArrayDeque<DmiData>();
		Deque<Float> trList = new ArrayDeque<Float>();
		Deque<Float> dmpList = new ArrayDeque<Float>();
		Deque<Float> dmmList = new ArrayDeque<Float>();
		Deque<Float> adxList = new ArrayDeque<Float>();
		Deque<Float> lastAdx = new ArrayDeque<Float>();
		float tr = 0;
		float dmp = 0;
		float dmm = 0;
		float di1 = 0;
		float di2 = 0;
		float adxMa = 0;
		float adxr = 0;

		float lastPrice = 0;
		float lastMin = 0;
		float lastMax = 0;
		float adxSum = 0;
		boolean first = true;
		for (HistoryPrice hPrice : prices) {
			float hd = 0;
			float ld = 0;
			if (!first) {
				tr = computeTR(trList, tr, lastPrice, hPrice);

				hd = hPrice.getMax() - lastMax;
				ld = lastMin - hPrice.getMin();
				dmp = computeDMP(dmpList, dmp, hd, ld);
				dmm = computeDMM(dmmList, dmm, hd, ld);
				di1 = dmp * 100 / tr;
				di2 = dmm * 100 / tr;

				adxSum = computeADXSum(adxList, di1, di2, adxSum);
				if (adxList.size() >= DAY_6) {
					adxMa = adxSum / DAY_6;
					lastAdx.add(adxMa);
					if (lastAdx.size() > DAY_6) {
						float tmp = lastAdx.removeFirst();
						adxr = (adxMa + tmp) / 2;
					}
				}
			} else {
				tr = Math.abs(hPrice.getMax() - hPrice.getMin());
				if (tr != 0) {
					trList.add(tr);
				}
			}

			DmiData dmiData;
			if (first) {
				dmiData = new DmiData(hPrice.getDate(), "0", "0", "0", "0", "0", "0", "0");
			} else {
				BigDecimal di1_b = new BigDecimal(di1);
				di1_b = di1_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal di2_b = new BigDecimal(di2);
				di2_b = di2_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal adxMa_b = new BigDecimal(adxMa);
				adxMa_b = adxMa_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal adxr_b = new BigDecimal(adxr);
				adxr_b = adxr_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal tr_b = new BigDecimal(tr);
				tr_b = tr_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal hd_b = new BigDecimal(hd);
				hd_b = hd_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal ld_b = new BigDecimal(ld);
				ld_b = ld_b.setScale(2, BigDecimal.ROUND_HALF_UP);
				dmiData = new DmiData(hPrice.getDate(), di1_b.toString(), di2_b.toString(), adxMa_b.toString(), adxr_b.toString(), tr_b.toString(),
						hd_b.toString(), ld_b.toString());
			}
			dmiDatas.addFirst(dmiData);

			lastPrice = hPrice.getClose();
			lastMax = hPrice.getMax();
			lastMin = hPrice.getMin();
			first = false;
		}
		return dmiDatas;
	}

	private float computeADXSum(Deque<Float> adxList, float di1, float di2, float adxSum) {
		if (di1 == 0 && di2 == 0) {
			return 0;
		}
		float adx = Math.abs(di2 - di1) / (di1 + di2) * 100;
		adxList.add(adx);
		adxSum += adx;
		if (adxList.size() > DAY_6) {
			adxSum -= adxList.removeFirst();
		}

		return adxSum;
	}

	private float computeDMP(Deque<Float> dmpList, float dmp, float hd, float ld) {
		if (hd <= 0 || hd <= ld) {
			hd = 0;
		}
		dmpList.add(hd);
		dmp += hd;
		if (dmpList.size() > DAY_14) {
			dmp -= dmpList.removeFirst();
		}
		return dmp;
	}

	private float computeDMM(Deque<Float> dmmList, float dmm, float hd, float ld) {
		if (ld <= 0 || ld <= hd) {
			ld = 0;
		}
		dmmList.add(ld);
		dmm += ld;
		if (dmmList.size() > DAY_14) {
			dmm -= dmmList.removeFirst();
		}
		return dmm;
	}

	private float computeTR(Deque<Float> trList, float tr, float lastPrice, HistoryPrice hPrice) {
		float trDiffer1 = hPrice.getMax() - hPrice.getMin();
		float trDiffer2 = Math.abs(hPrice.getMax() - lastPrice);
		float trDiffer3 = Math.abs(hPrice.getMin() - lastPrice);

		float trMax;
		if (trDiffer1 > trDiffer2) {
			trMax = trDiffer1;
		} else {
			trMax = trDiffer2;
		}
		if (trMax < trDiffer3) {
			trMax = trDiffer3;
		}
		trList.add(trMax);
		tr += trMax;
		if (trList.size() > DAY_14) {
			tr -= trList.removeFirst();
		}
		return tr;
	}


}
