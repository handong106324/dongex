package com.dong;

import com.dong.invest.model.pairs.SymbolPair;
import com.exchange.OkExchange;
import com.huobi.response.Kline;
import utils.Indicators;
import utils.Utils;
import zhibiao.base.MACD.MACD;
import zhibiao.base.MACD.MacdData;

import java.util.List;
import java.util.Map;

public class ZhiBiaoTest {

    /**
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        OkExchange bigOneClient = new OkExchange();

        SymbolPair symbolPair = new SymbolPair();
        symbolPair.setRealToken("btc");
        symbolPair.setBasicToken("usdt");
        symbolPair.setMarketId("btc_usd");
        /**
         *
         */
        List<Kline> ticker = bigOneClient.klines(symbolPair,"1hour", "2000",System.currentTimeMillis() - 3600000*120 +"");

        MACD macd = new MACD();
        List<MacdData> macdDatas = macd.computeStockMACD(ticker);
        for (MacdData kdjData : macdDatas) {
            System.out.println(kdjData);
        }

        System.out.println();

        Indicators indicators = new Indicators();
        List<MacdData> macdDataList = indicators.macd(ticker);
        for (MacdData kdjData : macdDataList) {
            System.out.println(kdjData);
        }

    }


}
