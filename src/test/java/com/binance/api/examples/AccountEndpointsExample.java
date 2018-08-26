package com.binance.api.examples;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.Trade;

import java.util.List;

/**
 * Examples on how to get account information.
 */
public class AccountEndpointsExample {

  public static void main(String[] args) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("cBiOR54bELVZK1XF5fPTxq4vZUsaxuyAKfwWX2QQagOmcjxW65p5fU6VwTQprpyx",
            "ddmlQQTwznOoWY40l2qt5DwlU9OSCjSTGUZBY6i0YcKdbdft2QqS1hogo50T4EFV");
    BinanceApiRestClient client = factory.newRestClient();

    // Get account balances
    Account account = client.getAccount(6000000L, System.currentTimeMillis());
    System.out.println(account.getBalances());
    System.out.println(account.getAssetBalance("ETH"));

    // Get list of trades
    List<Trade> myTrades = client.getMyTrades("NEOETH");
    System.out.println(myTrades);

    // Get withdraw history
    System.out.println(client.getWithdrawHistory("ETH"));

    // Get deposit history
    System.out.println(client.getDepositHistory("ETH"));

    // Get deposit address
    System.out.println(client.getDepositAddress("ETH"));

    // Withdraw
//    client.withdraw("ETH", "0x123", "0.1", null);
  }
}