package com.dong;

import com.dong.invest.model.Exchange;
import com.dong.invest.model.ex.bigone.BigOneTicker;
import com.dong.invest.model.pairs.SymbolPair;
import com.exchange.BigOneExchange;
import com.exchange.HuoBiExchange;
import com.exchange.OkExchange;
import com.huobi.response.Kline;
import com.huobi.response.Symbol;
import d.trade.duichong.DuiChongThread;
import zhibiao.base.MACD.HistoryPrice;
import zhibiao.base.kdj.KDJ;
import zhibiao.base.kdj.KdjData;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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

        KDJ kdj = new KDJ();
        List<KdjData> data = kdj.computeStockKDJ(ticker);

        for (KdjData kdjData : data) {
            System.out.println(kdjData);
        }
    }


}
