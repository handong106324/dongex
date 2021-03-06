package com.dong;

import com.dong.invest.model.Exchange;
import com.dong.invest.model.ex.bigone.BigOneTicker;
import com.dong.invest.model.ex.bigone.BigPrice;
import com.dong.invest.model.pairs.SymbolPair;
import com.exchange.BigOneExchange;
import com.exchange.HuoBiExchange;
import com.exchange.HuoBiExchangeAu;
import com.exchange.OkExchange;

import d.trade.DongLogFactory;
import d.trade.duichong.DuiChongThread;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BigOneAPITest {

    /**
     * [BTG-BTC, IOST-ETH, AIT-ETH, UIP-BTC, SNGLS-ETH, TCT-ETH, VEN-ETH, BTO-ETH, INK-BTC, TNB-BTC, CHAT-BTC, ZRX-BTC, GCT-ETH, GXS-BTC, XIN-ETH, QUBE-ETH, JLP-ETH, ZEC-BTC, ELF-BTC, EOSDAC-ETH, BCH-BTC, ADX-BTC, CVC-BTC, LUN-BTC, EKT-BTC, CRE-BTC, BTS-BTC, ETC-BTC, NEO-BTC, SHOW-BTC, EOS-BTC, BCD-BTC, BTO-BTC, CHAT-ETH, MAG-ETH, ETH-BTC, LINK-BTC, SNGLS-BTC, PIX-BTC, QTUM-BTC, BTM-BTC, MTN-BTC, DEW-ETH, MCO-BTC, GNT-BTC, AIT-BTC, FGC-ETH, BOE-ETH, DEW-BTC, EDG-BTC, HMC-ETH, CRE-ETH, DTA-ETH, ATN-BTC, FAIR-BTC, PRS-BTC, TCT-BTC, IOST-BTC, GCS-ETH, OCT-ETH, LTC-BTC, OMG-BTC, MAG-BTC, LUN-ETH, EOS-ETH, MANA-BTC, IDT-ETH, MANA-ETH, EOSDAC-BTC, XIN-BTC, SBTC-BTC, HOT-ETH, BOT-ETH, MYST-BTC, TNT-BTC, GCS-BTC, AE-BTC, GCT-BTC, MUSK-BTC, CDT-BTC, UIP-ETH, DTA-BTC, FGC-BTC, VEN-BTC, PST-BTC, IDT-BTC, CANDY-ETH, BCDN-BTC, 1ST-BTC, READ-BTC, HMC-BTC, QUBE-BTC, KNC-BTC, GNX-BTC, MTN-ETH, TNB-ETH, PRS-ETH, OMG-ETH, DGD-BTC, PAY-BTC, SNT-BTC, QUN-BTC, BAT-BTC, STORJ-BTC, AT-BTC, UTO-ETH, BCH-USDT, EOS-USDT, BTC-USDT, ETH-USDT, ONE-USDT, BIG-BTC, BIG-ETH, ONE-EOS]

     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        HuoBiExchange huoBiExchange = new HuoBiExchange();

        BigOneExchange bigOneClient = new BigOneExchange();
        OkExchange okEx = new OkExchange();
        

       

        List<Exchange> exchanges1 = new ArrayList<>();
        List<Exchange> exchanges2 = new ArrayList<>();
        List<Exchange> exchanges3 = new ArrayList<>();

        exchanges1.add(bigOneClient);
        exchanges1.add(huoBiExchange);
//        
//        exchanges2.add(okEx);
//        exchanges2.add(huoBiExchange);
//
//        exchanges3.add(bigOneClient);
//        exchanges3.add(okEx);
        
        new DuiChongThread(exchanges1).start();
//        new DuiChongThread(exchanges2).start();
//        new DuiChongThread(exchanges3).start();


    }


}
