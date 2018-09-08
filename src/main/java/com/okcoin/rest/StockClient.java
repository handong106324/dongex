package com.okcoin.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ApiFactory;
import com.ApiKey;

import com.huobi.response.Kline;
import com.utils.ExchangeUrlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dong.invest.model.ex.bigone.BigOneTicker;
import com.dong.invest.model.ex.bigone.BigPrice;
import com.dong.invest.model.pairs.SymbolPair;
import com.google.common.base.Ticker;
import com.huobi.response.Symbols;
import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;

import utils.HttpUtil;

/**
 * 现货 REST API 客户端请求
 *
 * @author zhangchi
 */
public class StockClient {
	static ApiKey apiKey = ApiFactory.getKey("OK");

    static String url_prex = "https://www.okcoin.com/";  //注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn

    /**
     * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
     *
     */
    static IStockRestApi stockGet = new StockRestApi(url_prex);

    /**
     * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
     * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
     * 发送post请求之前，程序会做自动加密，生成签名。
     *{"date":"1534948374","ticker":{"high":"6875.7699","vol":"28565.0000","last":"6646.0009","low":"6374.1631","buy":"6646.4867","sell":"6650.0232"}}
     */
    static IStockRestApi stockPost = new StockRestApi(url_prex, apiKey.getApiKey(), apiKey.getSecret());


    public static BigOneTicker getTicker(SymbolPair sym) {
        //现货行情
        String tikerInfo;
		try {
			tikerInfo = stockGet.depth(sym.getMarketId());
			if(StringUtils.isBlank(tikerInfo)) {
				return null;
			}
			JSONObject jsonObject = JSONObject.parseObject(tikerInfo);
			 
		        BigOneTicker bigOneTicker = new BigOneTicker();
		        JSONArray askArray = jsonObject.getJSONArray("asks");
		        JSONArray ask = askArray.getJSONArray(askArray.size() -1);
		        bigOneTicker.setAsk(new BigPrice(ask.getDouble(0), ask.getDouble(1)));
		        JSONArray bidArray = jsonObject.getJSONArray("bids");
		        JSONArray bid = bidArray.getJSONArray(0);
		        bigOneTicker.setBid(new BigPrice(bid.getDouble(0),bid.getDouble(1)));
//		        bigOneTicker.setClose(jsonObject.getDoubleValue("close"));
//		        bigOneTicker.setVolume(jsonObject.getDoubleValue("vol"));
//		        bigOneTicker.setOpen(jsonObject.getDoubleValue("open"));
//		        bigOneTicker.setLow(jsonObject.getDoubleValue("low"));
//		        bigOneTicker.setHigh(jsonObject.getDoubleValue("high"));
		        bigOneTicker.setMarket_id(sym.getMarketId());
		        return bigOneTicker;
		} catch (HttpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return null;
    }
    
        //现货市场深度
//        String depth = stockGet.depth("btc_usdt");
//        print(depth);
//        //现货OKCoin历史交易信息
//        String trades = stockGet.trades("btc_usdt", "20");
//
//        print(trades);
//
//        //现货用户信息
//        String userInfo = stockPost.userinfo();
//
//        print("userInfo:" + userInfo);
////        //现货下单交易
////        String tradeResult = stockPost.trade("btc_usd", "buy", "50", "0.02");
////        System.out.println(tradeResult);
////        JSONObject tradeJSV1 = JSONObject.parseObject(tradeResult);
////        String tradeOrderV1 = tradeJSV1.getString("order_id");
////
////        //现货获取用户订单信息
////        stockPost.order_info("btc_usd", tradeOrderV1);
////
////        //现货撤销订单
////        stockPost.cancel_order("btc_usd", tradeOrderV1);
////
////        //现货批量下单
////        stockPost.batch_trade("btc_usd", "buy", "[{price:50, amount:0.02},{price:50, amount:0.03}]");
////
////        //批量获取用户订单
////        stockPost.orders_info("0", "btc_usd", "125420341, 125420342");
//
//        //获取用户历史订单信息，只返回最近七天的信息
//        String orderHIst= stockPost.order_history("btc_usdt", "0", "1", "20");
//
//        print(orderHIst);
//
//    }

    public static void print(String s) {
        System.out.println(s);
    }
    /**
     * {"date":"1534946609","tickers":[{"symbol":"btc_usdt","high":"6875.7699","vol":"28987.0000","last":"6660.6067","low":"6374.1631","buy":"6657.5691","sell":"6661.2096"},
     * {"symbol":"ltc_usdt","high":"59.2512","vol":"417287.0000","last":"57.7672","low":"54.4800","buy":"57.7367","sell":"57.7873"},
     * {"symbol":"eth_usdt","high":"298.9000","vol":"265869.0000","last":"284.9150","low":"271.0000","buy":"284.8874","sell":"284.9670"},{"symbol":"okb_usdt","high":"2.0400","vol":"16627183.0000","last":"1.8832","low":"1.7860","buy":"1.8829","sell":"1.8869"},{"symbol":"etc_usdt","high":"13.4700","vol":"2738473.0000","last":"13.0368","low":"12.3157","buy":"13.0392","sell":"13.0535"},{"symbol":"bch_usdt","high":"574.8517","vol":"68120.0000","last":"546.5172","low":"516.4130","buy":"546.7085","sell":"547.4336"},{"symbol":"1st_usdt","high":"0.0772","vol":"4316022.0000","last":"0.0705","low":"0.0669","buy":"0.0704","sell":"0.0708"},{"symbol":"aac_usdt","high":"0.0260","vol":"25244436.0000","last":"0.0228","low":"0.0223","buy":"0.0227","sell":"0.0230"},{"symbol":"abt_usdt","high":"0.1634","vol":"1087798.0000","last":"0.1567","low":"0.1462","buy":"0.1565","sell":"0.1568"},{"symbol":"ace_usdt","high":"0.0585","vol":"2100623.0000","last":"0.0560","low":"0.0521","buy":"0.0553","sell":"0.0560"},{"symbol":"act_usdt","high":"0.0380","vol":"21807560.0000","last":"0.0355","low":"0.0323","buy":"0.0354","sell":"0.0356"},{"symbol":"ada_usdt","high":"0.1007","vol":"2623684.0000","last":"0.0952","low":"0.0910","buy":"0.0952","sell":"0.0955"},{"symbol":"ae_usdt","high":"1.1500","vol":"197780.0000","last":"0.9983","low":"0.9501","buy":"0.9990","sell":"1.0229"},{"symbol":"aidoc_usdt","high":"0.0116","vol":"573396.0000","last":"0.0112","low":"0.0105","buy":"0.0108","sell":"0.0113"},{"symbol":"amm_usdt","high":"0.0547","vol":"33395.0000","last":"0.0517","low":"0.0425","buy":"0.0431","sell":"0.0505"},{"symbol":"ark_usdt","high":"0.7596","vol":"5897.0000","last":"0.6971","low":"0.6444","buy":"0.6892","sell":"0.7023"},{"symbol":"ast_usdt","high":"0.0864","vol":"2655.0000","last":"0.0864","low":"0.0771","buy":"0.0864","sell":"0.0946"},{"symbol":"atl_usdt","high":"0.0427","vol":"7175.0000","last":"0.0427","low":"0.0251","buy":"0.0400","sell":"0.0426"},{"symbol":"auto_usdt","high":"0.0051","vol":"703172.0000","last":"0.0050","low":"0.0047","buy":"0.0048","sell":"0.0051"},{"symbol":"avt_usdt","high":"0.5000","vol":"0.0000","last":"0.5000","low":"0.5000","buy":"0.4807","sell":"0.5000"},{"symbol":"bcd_usdt","high":"1.1462","vol":"132934.0000","last":"1.0989","low":"1.0066","buy":"1.0924","sell":"1.1221"},{"symbol":"bcn_usdt","high":"0.00191","vol":"2952382.00000","last":"0.00191","low":"0.00163","buy":"0.00178","sell":"0.00191"},{"symbol":"bec_usdt","high":"0.2679","vol":"112586.0000","last":"0.2475","low":"0.2401","buy":"0.2472","sell":"0.2535"},{"symbol":"bkx_usdt","high":"0.0955","vol":"2130145.0000","last":"0.0863","low":"0.0762","buy":"0.0854","sell":"0.0871"},{"symbol":"bnt_usdt","high":"1.6828","vol":"45836.0000","last":"1.6221","low":"1.5261","buy":"1.6150","sell":"1.6246"},{"symbol":"brd_usdt","high":"0.3890","vol":"337.0000","last":"0.2419","low":"0.2419","buy":"0.3465","sell":"0.3849"},{"symbol":"btg_usdt","high":"19.8554","vol":"24006.0000","last":"18.8411","low":"18.1147","buy":"18.8346","sell":"18.9436"},{"symbol":"btm_usdt","high":"0.1797","vol":"6555220.0000","last":"0.1709","low":"0.1579","buy":"0.1704","sell":"0.1710"},{"symbol":"cac_usdt","high":"0.21589","vol":"1044698669.34130","last":"0.17140","low":"0.14850","buy":"0.17131","sell":"0.17150"},{"symbol":"cag_usdt","high":"0.1018","vol":"0.0000","last":"0.1018","low":"0.1018","buy":"0.0956","sell":"0.1073"},{"symbol":"cai_usdt","high":"0.000233","vol":"743474307.000000","last":"0.000202","low":"0.000188","buy":"0.000202","sell":"0.000203"},{"symbol":"can_usdt","high":"0.0083","vol":"311092412.0000","last":"0.0077","low":"0.0072","buy":"0.0077","sell":"0.0080"},{"symbol":"cbt_usdt","high":"0.0113","vol":"546.0000","last":"0.0111","low":"0.0107","buy":"0.0111","sell":"0.0120"},{"symbol":"chat_usdt","high":"0.0149","vol":"481306.0000","last":"0.0149","low":"0.0133","buy":"0.0138","sell":"0.0149"},{"symbol":"cic_usdt","high":"0.0022","vol":"19242034.0000","last":"0.0020","low":"0.0019","buy":"0.0019","sell":"0.0020"},{"symbol":"cmt_usdt","high":"0.0877","vol":"2933143.0000","last":"0.0848","low":"0.0751","buy":"0.0843","sell":"0.0855"},{"symbol":"ctxc_usdt","high":"0.8000","vol":"2214191.0000","last":"0.5046","low":"0.3859","buy":"0.5015","sell":"0.5179"},{"symbol":"cvc_usdt","high":"0.1216","vol":"4567671.0000","last":"0.1164","low":"0.1127","buy":"0.1164","sell":"0.1166"},{"symbol":"cvt_usdt","high":"0.03940","vol":"8055336.00000","last":"0.03374","low":"0.03136","buy":"0.03374","sell":"0.03400"},{"symbol":"dadi_usdt","high":"0.0950","vol":"17536.0000","last":"0.0881","low":"0.0767","buy":"0.0800","sell":"0.0903"},{"symbol":"dash_usdt","high":"153.7748","vol":"16103.0000","last":"145.6785","low":"137.9172","buy":"145.2560","sell":"145.6833"},{"symbol":"dat_usdt","high":"0.0092","vol":"7920534.0000","last":"0.0080","low":"0.0073","buy":"0.0080","sell":"0.0081"},{"symbol":"dcr_usdt","high":"39.9839","vol":"131.0000","last":"37.8410","low":"36.4652","buy":"37.6150","sell":"39.0000"},{"symbol":"dent_usdt","high":"0.0028","vol":"45754019.0000","last":"0.0025","low":"0.0021","buy":"0.0024","sell":"0.0026"},{"symbol":"dgb_usdt","high":"0.0268","vol":"367569.0000","last":"0.0252","low":"0.0242","buy":"0.0251","sell":"0.0255"},{"symbol":"dgd_usdt","high":"57.0000","vol":"60.0000","last":"57.0000","low":"54.5858","buy":"55.0652","sell":"57.6608"},{"symbol":"dna_usdt","high":"0.0791","vol":"26380.0000","last":"0.0765","low":"0.0680","buy":"0.0702","sell":"0.0793"},{"symbol":"dnt_usdt","high":"0.0207","vol":"0.0000","last":"0.0207","low":"0.0207","buy":"0.0207","sell":"0.0248"},{"symbol":"dpy_usdt","high":"0.2150","vol":"5380690.0000","last":"0.1919","low":"0.1874","buy":"0.1919","sell":"0.1932"},{"symbol":"edo_usdt","high":"0.8691","vol":"1156.0000","last":"0.7762","low":"0.7650","buy":"0.7636","sell":"0.8988"},{"symbol":"egt_usdt","high":"0.00292","vol":"29351788.00000","last":"0.00270","low":"0.00260","buy":"0.00270","sell":"0.00275"},{"symbol":"elf_usdt","high":"0.4000","vol":"2384744.0000","last":"0.3713","low":"0.3407","buy":"0.3705","sell":"0.3719"},{"symbol":"eng_usdt","high":"0.7970","vol":"6467.0000","last":"0.7970","low":"0.6519","buy":"0.6603","sell":"0.7054"},{"symbol":"enj_usdt","high":"0.0399","vol":"4260611.0000","last":"0.0383","low":"0.0348","buy":"0.0376","sell":"0.0383"},{"symbol":"eos_usdt","high":"5.3682","vol":"22287076.0000","last":"5.0323","low":"4.6577","buy":"5.0300","sell":"5.0345"},{"symbol":"evx_usdt","high":"0.3691","vol":"483.0000","last":"0.3691","low":"0.3582","buy":"0.3802","sell":"0.4184"},{"symbol":"fair_usdt","high":"0.0088","vol":"43624715.0000","last":"0.0071","low":"0.0060","buy":"0.0067","sell":"0.0070"},{"symbol":"fun_usdt","high":"0.0187","vol":"148474.0000","last":"0.0187","low":"0.0168","buy":"0.0177","sell":"0.0189"},{"symbol":"gas_usdt","high":"5.4406","vol":"172885.0000","last":"5.1019","low":"4.8307","buy":"5.0957","sell":"5.1067"},{"symbol":"gnt_usdt","high":"0.1594","vol":"2132617.0000","last":"0.1542","low":"0.1464","buy":"0.1540","sell":"0.1547"},{"symbol":"gnx_usdt","high":"0.0650","vol":"426563.0000","last":"0.0625","low":"0.0582","buy":"0.0615","sell":"0.0631"},{"symbol":"gsc_usdt","high":"0.0130","vol":"280122.0000","last":"0.0107","low":"0.0099","buy":"0.0107","sell":"0.0123"},{"symbol":"gto_usdt","high":"0.0880","vol":"73998325.0000","last":"0.0828","low":"0.0800","buy":"0.0824","sell":"0.0832"},{"symbol":"hmc_usdt","high":"0.0160","vol":"3542142.0000","last":"0.0155","low":"0.0148","buy":"0.0154","sell":"0.0156"},{"symbol":"hot_usdt","high":"0.0123","vol":"1630981.0000","last":"0.0118","low":"0.0111","buy":"0.0117","sell":"0.0120"},{"symbol":"hpb_usdt","high":"0.8392","vol":"110390.0000","last":"0.7840","low":"0.7367","buy":"0.7754","sell":"0.8108"},{"symbol":"hsr_usdt","high":"3.1888","vol":"963685.0000","last":"3.0592","low":"2.9182","buy":"3.0586","sell":"3.0700"},{"symbol":"hyc_usdt","high":"0.03795","vol":"9963120.00000","last":"0.03540","low":"0.03398","buy":"0.03508","sell":"0.03564"},{"symbol":"icn_usdt","high":"0.4699","vol":"0.0000","last":"0.4699","low":"0.4699","buy":"0.4462","sell":"0.4580"},{"symbol":"icx_usdt","high":"0.7034","vol":"399949.0000","last":"0.6454","low":"0.6099","buy":"0.6453","sell":"0.6461"},{"symbol":"ins_usdt","high":"0.3180","vol":"156078.0000","last":"0.3006","low":"0.2817","buy":"0.2963","sell":"0.3016"},{"symbol":"insur_usdt","high":"0.000645","vol":"162894355.000000","last":"0.000636","low":"0.000590","buy":"0.000632","sell":"0.000636"},{"symbol":"int_usdt","high":"0.0419","vol":"5879759.0000","last":"0.0390","low":"0.0377","buy":"0.0386","sell":"0.0395"},{"symbol":"iost_usdt","high":"0.0152","vol":"80576771.0000","last":"0.0144","low":"0.0127","buy":"0.0144","sell":"0.0145"},{"symbol":"iota_usdt","high":"0.5500","vol":"8796916.0000","last":"0.5385","low":"0.4983","buy":"0.5382","sell":"0.5414"},{"symbol":"ipc_usdt","high":"0.2177","vol":"65849.0000","last":"0.1986","low":"0.1955","buy":"0.1986","sell":"0.2047"},{"symbol":"itc_usdt","high":"0.2118","vol":"253319.0000","last":"0.1955","low":"0.1881","buy":"0.1943","sell":"0.1955"},{"symbol":"kan_usdt","high":"0.00554","vol":"38786120.00000","last":"0.00533","low":"0.00506","buy":"0.00532","sell":"0.00541"},{"symbol":"kcash_usdt","high":"0.0352","vol":"28566910.0000","last":"0.0322","low":"0.0305","buy":"0.0321","sell":"0.0323"},{"symbol":"key_usdt","high":"0.0065","vol":"2276309.0000","last":"0.0060","low":"0.0056","buy":"0.0059","sell":"0.0060"},{"symbol":"knc_usdt","high":"0.6478","vol":"1716420.0000","last":"0.4925","low":"0.4298","buy":"0.4924","sell":"0.4929"}
     * ,{"symbol":"lba_usdt","high":"0.0595","vol":"7033154.0000","last":"0.0525","low":"0.0469","buy":"0....
     * @return
     */
	public static List<SymbolPair> getAllSymbolPairs() {
		// TODO Auto-generated method stub
		List<SymbolPair> list = new ArrayList<>();			
			String resp = HttpUtil.doGet(url_prex+ "/api/v1/tickers.do");
			JSONObject json = JSONObject.parseObject(resp);
			JSONArray ticks = json.getJSONArray("tickers");
			for(Object tick :  ticks) {
				JSONObject ticker = (JSONObject) tick;
				SymbolPair symbolPair = new SymbolPair();
				String symbo = ticker.getString("symbol");
				symbolPair.setMarketId(symbo);
				String[] coins = StringUtils.split(symbo,"_");
				symbolPair.setBasicToken(coins[1]);
				symbolPair.setRealToken(coins[0]);

				
				list.add(symbolPair);
			}
		
		return list;
	}

	public static List<Kline> klines(String marketId, String period, String size,String since) {
		String reps = HttpUtil.doGet(url_prex +"api/v1/kline.do?symbol="+marketId+"&type="+period+"&size="+size+"&since="+since);
		System.out.println(reps);
		String[] res = StringUtils.split(reps, "[\\[|\\]]");
		List<Kline> klines = new ArrayList<>();

		for (String data : res) {
			String[] str = StringUtils.split(data, "[\\,|\"]");
			if (str.length != 6) {
				continue;
			}
			Kline kline = new Kline();
			kline.setOpen(Double.parseDouble(str[1]));
			kline.setHigh(Double.parseDouble(str[2]));
			kline.setLow(Double.parseDouble(str[3]));
			kline.setClose(Double.parseDouble(str[4]));
			kline.setVol(Double.parseDouble(str[5]));
			kline.setTimestamp(Long.parseLong(str[0]));
			klines.add(kline);
		}
		return klines;
	}
}
