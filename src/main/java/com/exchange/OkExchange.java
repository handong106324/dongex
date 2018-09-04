package com.exchange;

import com.ApiFactory;
import com.ApiKey;
import com.bigone.BigOneClient;
import com.dong.invest.model.Exchange;
import com.dong.invest.model.ex.bigone.BigOneOrder;
import com.dong.invest.model.ex.bigone.BigOneTicker;
import com.dong.invest.model.pairs.SymbolPair;
import com.huobi.response.Kline;
import com.huobi.response.Symbol;
import com.okcoin.rest.StockClient;
import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;

import d.trade.duichong.CurrentMarketInfo;
import d.trade.duichong.TradeResult;

import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OkExchange extends Exchange {

    @Test
    @Override
    public List<SymbolPair> getAllSymbolPairs() {
        List<SymbolPair> symbolPairs = StockClient.getAllSymbolPairs();
        for (SymbolPair symbolPair : symbolPairs) {
            symbols.put(symbolPair.getRealToken().toLowerCase() + symbolPair.getBasicToken().toLowerCase(),symbolPair);
        }
        return symbolPairs;
    }

    @Override
    public CurrentMarketInfo getMarketTradeInfo(SymbolPair symbolPair) {
        return BigOneClient.getTickers(symbolPair);
    }

    @Override
    public TradeResult buy(double buyPrice,double buyAmount, SymbolPair symbolPair) {
        return BigOneClient.trade(buyPrice,buyAmount, symbolPair,true);
    }

    @Override
    public TradeResult sell(double sellPrice,double sellAmount, SymbolPair symbolPair) {
        return BigOneClient.trade(sellPrice,sellAmount, symbolPair,false);
    }

    @Override
    public BigOneOrder createOrder(double sellPrice, double sellAmount, SymbolPair symbolPair, boolean isBuy) {
        return BigOneClient.createOrder(symbolPair.getMarketId(),isBuy?"BID":"ASK",sellPrice+"",""+sellAmount);
    }

    @Override
    public TradeResult tradeAnsyc(double sellPrice,double sellAmount, SymbolPair symbolPair) {
        return BigOneClient.tradeAsync(sellPrice,sellAmount, symbolPair,false, 600000);
    }


    @Override
    public String getName() {
        return "OK";
    }

    @Override
    public BigOneOrder cancelOrder(String id) throws Exception {
        return BigOneClient.cancelOrder(id);
    }

    @Override
    public BigOneTicker getTicker(SymbolPair symbo) {
        return StockClient.getTicker(symbo);
    }

    public SymbolPair getByCode(String coinName,String bCoin) {
        List<SymbolPair> symbolPairs = getAllSymbolPairs();
        for (SymbolPair symbolPair : symbolPairs) {
            if (symbolPair.getRealToken().equalsIgnoreCase(coinName) &&
                    symbolPair.getBasicToken().equalsIgnoreCase(bCoin)) {
                return symbolPair;
            }
        }

        return null;
    }

    public List<Kline> klines(SymbolPair symbolPair, String s, String s1,String since) {
        return StockClient.klines(symbolPair.getMarketId(), s,s1,since);
    }
}
