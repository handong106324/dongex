package com.dong;

import java.util.ArrayList;
import java.util.List;

import com.dong.invest.model.pairs.SymbolPair;
import com.exchange.BigOneExchange;

import d.trade.mine.TradeStreagy;

public class TradencyCheck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigOneExchange bigOneExchange = new BigOneExchange();
		List<SymbolPair> pairs = bigOneExchange.getAllSymbolPairs();
    		for (SymbolPair sym : pairs) {
    			if(!sym.getBasicToken().equalsIgnoreCase("usdt")) {
    				continue;
    			}
    			new TradeStreagy(bigOneExchange, sym).start();
    		}
    	
       
	}

}
