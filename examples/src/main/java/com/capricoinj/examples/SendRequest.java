package com.fuelcoin.examples;

import java.io.File;

import com.fuelcoinj.core.Address;
import com.fuelcoinj.core.Coin;
import com.fuelcoinj.core.InsufficientMoneyException;
import com.fuelcoinj.core.NetworkParameters;
import com.fuelcoinj.core.Transaction;
import com.fuelcoinj.core.Wallet;
import com.fuelcoinj.core.Wallet.BalanceType;
import com.fuelcoinj.kits.WalletAppKit;
import com.fuelcoinj.params.MainNetParams;
import com.fuelcoinj.params.UnitTestParams;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * The following example shows you how to create a SendRequest to send coins from a wallet to a given address.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH FUELCOIN
public class SendRequest {

    public static void main(String[] args) throws Exception {

        // We use the WalletAppKit that handles all the boilerplate for us. Have a look at the Kit.java example for more details.
        NetworkParameters params = MainNetParams.get();
        WalletAppKit kit = new WalletAppKit(params, new File("."), "forwarding-service");
        kit.startAsync();
        kit.awaitRunning();
        kit.wallet().allowSpendingUnconfirmedTransactions();
               
        for(Transaction tx : kit.wallet().getPendingTransactions())
        {
        	System.out.println("Pending : "+tx.toString(kit.chain()));
        	
        }
        System.out.println("wallet balance is "+kit.wallet().getBalance());
        System.out.println("Send money to: " + kit.wallet().currentReceiveAddress().toString());
        
        System.exit(0);
        
        // How much coins do we want to send?
        // The Coin class represents a monetary Peercoin value.
        // We use the parseCoin function to simply get a Coin instance from a simple String.
        Coin value = Coin.parseCoin("1.00");

        // To which address you want to send the coins?
        // The Address class represents a Peercoin address.
        Address to = new Address(params, "FGFiHGDj6YxHYtmW6fpN1jLBieYcPmhv14");

        // There are different ways to create and publish a SendRequest. This is probably the easiest one.
        // Have a look at the code of the SendRequest class to see what's happening and what other options you have: https://peercoinj.github.io/javadoc/0.11/com/google/peercoin/core/Wallet.SendRequest.html
        // 
        // Please note that this might raise a InsufficientMoneyException if your wallet has not enough coins to spend.
        // When using the testnet you can use a faucet (like the http://faucet.xeno-genesis.com/) to get testnet coins.
        // In this example we catch the InsufficientMoneyException and register a BalanceFuture callback that runs once the wallet has enough balance.
        try {
            Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), to, value);
            System.out.println("coins sent. transaction hash: " + result.tx.getHashAsString());
            // you can use a block explorer like https://www.biteasy.com/ to inspect the transaction with the printed transaction hash. 
        } catch (InsufficientMoneyException e) {
            System.out.println("Not enough coins in your wallet. Missing " + e.missing.getValue() + " satoshis are missing (including fees)");
            System.out.println("Send money to: " + kit.wallet().currentReceiveAddress().toString());

            // Peercoinj allows you to define a BalanceFuture to execute a callback once your wallet has a certain balance.
            // Here we wait until the we have enough balance and display a notice.
            // Peercoinj is using the ListenableFutures of the Guava library. Have a look here for more information: https://code.google.com/p/guava-libraries/wiki/ListenableFutureExplained
            ListenableFuture<Coin> balanceFuture = kit.wallet().getBalanceFuture(value, BalanceType.AVAILABLE);
            FutureCallback<Coin> callback = new FutureCallback<Coin>() {
                public void onSuccess(Coin balance) {
                    System.out.println("coins arrived and the wallet now has enough balance");
                }

                public void onFailure(Throwable t) {
                    System.out.println("something went wrong");
                }
            };
            Futures.addCallback(balanceFuture, callback);
        }

        // shutting down 
        //kit.stopAsync();
        //kit.awaitTerminated();
    }
}
