package d.trade.mine;

import com.bigone.BigOneClient;
import com.dong.invest.model.Exchange;
import com.dong.invest.model.ex.bigone.BigOneAsset;
import com.dong.invest.model.ex.bigone.BigOneOrder;
import com.dong.invest.model.ex.bigone.BigOneTicker;
import com.exchange.BigOneExchange;
import com.dong.invest.model.pairs.SymbolPair;

import d.trade.DongLogFactory;
import d.trade.duichong.TradeResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author handong
 * @date 2018-08-10 10:56
 */
public class TradeStreagy extends Thread {

    Exchange bigOneExchange;
    File file;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

    private int stragy = 0;
	private SymbolPair sympair;
    
    
    public TradeStreagy(Exchange exchange,SymbolPair sym) {
		// TODO Auto-generated constructor stub
    	this.bigOneExchange = exchange;
    	this.sympair = sym;
	}
    @Override
    public void run() {
    	file = new File("C://" + sympair.getRealToken()+"_"+stragy+".log");

    	
        try {
			trade();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      

    }
	private void trade() {
		// TODO Auto-generated method stub
		double totalGet = 0;

        //买卖和跑
        int total = 0;
        double lastAskPrice = 0;
        double lastBidPrice = 0;
        boolean tradeOK = false;
        boolean askOk = false;
        boolean bidOK = false;
        System.out.println("start" + sympair.getRealToken());
        while (true) {
           
            total ++;
        
            BigOneTicker bigOneTicker = bigOneExchange.getTicker(sympair);
            double thisAskPrice = bigOneTicker.getAsk().getPrice();
            double thisBidPrice = bigOneTicker.getBid().getPrice();
            if(!tradeOK) {
            	lastAskPrice = thisAskPrice;
            	lastBidPrice = thisBidPrice;
            	tradeOK = true;
            	continue;
            }
            if ((thisAskPrice - lastAskPrice)/lastAskPrice > 0.01) {
            	if (askOk) {
            		continue;
            	}
            	String logs =this.sympair.getRealToken() + " sell =" + lastAskPrice +"\n";
            	
            	System.out.println(logs);
            	try {
					FileUtils.write(file, logs, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	askOk = true;
            }
            
            if ((lastBidPrice-thisBidPrice)/lastBidPrice > 0.01) {
            	if(bidOK) {
            		continue;
            	}
            	String logs =this.sympair.getRealToken() + " buy =" + lastBidPrice+"\n";
            	
            	System.out.println(logs);
            	try {
					FileUtils.write(file, logs, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	bidOK = true;
            }
            if (askOk && bidOK) {
            	totalGet += (lastAskPrice - lastBidPrice)/lastBidPrice*0.098;
            	askOk = false;
            	bidOK = false;
            	tradeOK = false;
            	String log = "[judge"+total+" ok][ask = "+lastAskPrice+"][bid ="+lastBidPrice+"][get%="+totalGet+"]";
            	total = 0;

            	System.out.println(log);
            	DongLogFactory.addDeal(log);
            } else {
            	if (total % 500 == 0) {
                	String log = "[   "+sympair.getMarketId()+"   no   ][judge"+ total +" ok][ask = "+lastAskPrice+"][bid ="+lastBidPrice+"][get="+totalGet+"]";
                	System.out.println(log);
                	try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        }
	}


}
