package d.trade.duichong;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dong.invest.model.Exchange;
import com.dong.invest.model.ex.bigone.BigOneTicker;
import com.dong.invest.model.ex.bigone.BigPrice;

import d.trade.DongLogFactory;

public class DuiChongThread extends Thread{


    private File file;
    private File judgeFile;

    private SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    private double totalGet = 0;
    private double realGet = 0;
    private int judgeTime = 0;
    private int dealTime = 0;
    private double totalAmount =0;
    private double realAmount =0;
    private List<Exchange> exchanges;
    private String flag;
    public DuiChongThread(List<Exchange> ex) {
		this.exchanges = ex;
		flag = ex.get(0).getName() +"->" + ex.get(1).getName();
		file = new File("C://Users/Administrator/Desktop/"+flag+"real.log");
		judgeFile = new File("C://Users/Administrator/Desktop/"+flag+"judge.log");
	}
	@Override
	public void run() {
		 List<String> haveCoins = new ArrayList<>();
	        for (String coin : exchanges.get(0).symbols().keySet()) {
	            if (exchanges.get(1).symbols().containsKey(coin)) {
	                haveCoins.add(coin);
	            }
	        }
	        
	        int time = 0;
	        while (true) {
	                for (String coin : haveCoins) {
	                    try {
	                        if (!coin.contains("usdt")) {
	                            continue;
	                        }
	                        checkPriceCanBuy(coin,exchanges);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }

	                }
	            
	            try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        }
		// TODO Auto-generated method stub
	}

    private void checkPriceCanBuy(String coin, List<Exchange> exchanges) {
        List<BigOneTicker> tickers = getTickers(coin,exchanges);
        BigOneTicker askTicker = null;

        double highestBidPrice = 0;
        BigOneTicker bidTicker = null;

        double amount = 0;
        double minAskPrice = 999999999;
        for (BigOneTicker ticker : tickers) {
            //交易所卖价 < 另外交易所的买价
            if (ticker.getAsk().getPrice().doubleValue() < minAskPrice) {
                minAskPrice = ticker.getAsk().getPrice().doubleValue();
                askTicker = ticker;
            }

            if (ticker.getBid().getPrice().doubleValue() > highestBidPrice ) {
                highestBidPrice = ticker.getBid().getPrice().doubleValue();
                bidTicker = ticker;
            }

        }

        if (askTicker.getExchange().getName().equalsIgnoreCase(bidTicker.getExchange().getName())) {
            return;
        }

        if (askTicker != null && bidTicker != null) {
        	if(askTicker.getAsk().getAmount() > bidTicker.getBid().getAmount()) {
        		amount = bidTicker.getBid().getAmount();
        	} else {
        		amount = askTicker.getAsk().getAmount();
        	}
            double get = highestBidPrice - minAskPrice;
            if (get > 0) {

                List<String> logs = new ArrayList<>();
//                System.out.println("["+flag+"]["+coin +"决策满足条件:" + toStr(highestBidPrice) + "  -  " + toStr(minAskPrice) +" = " + toStr(get));

                if (get <= (minAskPrice + highestBidPrice) * 0.002) {
//                	System.out.println(flag + "    FAIL:"+coin +"盈利不满足条件:" + toStr(highestBidPrice) + "  -  " + toStr(minAskPrice) +" = " + toStr(get - (minAskPrice + highestBidPrice) * 0.002));
                 
                    return;
                } 
                double currentGet = (amount * (get - (minAskPrice + highestBidPrice) * 0.002));
                totalGet += currentGet;
                judgeTime ++;
                totalAmount += amount;
                
                DongLogFactory.addJudge("["+flag+"]["+simp.format(new Date()) + "]["+coin +"][数量="+amount +" *( " + toStr(highestBidPrice) + "  -  " + toStr(minAskPrice) +" - "
                +(minAskPrice + highestBidPrice) * 0.002 +") = " + toStr(currentGet) +"][totalGet:"+ toStr(totalGet) +"]");
                if(checkDeal(askTicker,bidTicker,exchanges,coin)) {
                	dealTime ++;
                }
                
            } else {
//                System.out.println("失败决策："+coin +": " + askTicker.getAsk() +" "+minAskPrice + ":" + bidTicker.getBid() + " " + highestBidPrice);

            }
        }
    }


    private List<BigOneTicker> getTickers(String coin, List<Exchange> exchanges) {
		// TODO Auto-generated method stub
    	List<BigOneTicker> tickers = new ArrayList<>();
        for (Exchange exchange : exchanges) {
            BigOneTicker ticker = exchange.getTicker(exchange.symbols.get(coin));
            if(null == ticker) {
            	continue;
            }
            tickers.add(ticker);
            ticker.setExchange(exchange);
        }
		return tickers;
	}

	private boolean checkDeal(BigOneTicker askTickerOld, BigOneTicker bidTickerOld,List<Exchange> exchanges,String coin) {
		// TODO Auto-generated method stub
		
		List<BigOneTicker> tickers = getTickers(coin, exchanges);
		BigOneTicker askTicker = null;

        double highestBidPrice = 0;
        BigOneTicker bidTicker = null;

        double amount =0;
        double minAskPrice = 999999999;
        for (BigOneTicker ticker : tickers) {
            //交易所卖价 < 另外交易所的买价
            if (ticker.getAsk().getPrice().doubleValue() < minAskPrice) {
                minAskPrice = ticker.getAsk().getPrice().doubleValue();
                askTicker = ticker;
            }

            if (ticker.getBid().getPrice().doubleValue() > highestBidPrice ) {
                highestBidPrice = ticker.getBid().getPrice().doubleValue();
                bidTicker = ticker;
            }

        }
        if(askTicker == null || bidTicker == null) {
        	DongLogFactory.addJudge("order despear");
        	return false;
        }
        if(askTicker.getAsk().getAmount() > bidTicker.getBid().getAmount()) {
    		amount = bidTicker.getBid().getAmount();
    	} else {
    		amount = askTicker.getAsk().getAmount();
    	}
        double currentGet = amount * (minAskPrice - highestBidPrice - (minAskPrice + highestBidPrice) * 0.002);

        if(currentGet <= 0) {
        	System.out.println("judge and deal is not intime:" + "["+flag+"]["+"current get lower than judge "+currentGet+"/"+(askTickerOld.getAsk().getPrice() - bidTickerOld.getBid().getPrice()));
        	DongLogFactory.addJudge("["+flag+"]["+"current get lower than judge "+currentGet+"/"+(askTickerOld.getAsk().getPrice() - bidTickerOld.getBid().getPrice()));
        	return false;
        }
        realGet += currentGet;
        realAmount += amount;
    	DongLogFactory.addDeal("["+flag+"]["+askTicker.getExchange().getName() + "-"+bidTicker.getExchange().getName()+"]realDeal [get="+currentGet+"]/["+(askTickerOld.getAsk().getPrice() - bidTickerOld.getBid().getPrice()+"]"));
		return true;
	}

	public double[] countPriceAndAmount(List<BigPrice> bigPrices) {
        double amount = 0;
        double priceTotal = 0;
        for (BigPrice bigPrice : bigPrices) {
            amount += bigPrice.getAmount();
            priceTotal += bigPrice.getPrice() * bigPrice.getAmount();
            System.out.println(bigPrice.getAmount() + "_" + bigPrice.getPrice());
        }
        return new double[]{amount,priceTotal};

    }

    public String toStr(Double d) {
        DecimalFormat decimalFormat = new DecimalFormat("##.#######");
        return decimalFormat.format(d);
    }
}
