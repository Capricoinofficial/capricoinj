package com.fuelcoin.examples;

import static com.fuelcoinj.core.Coin.CENT;
import static com.fuelcoinj.core.Coin.COIN;
import static com.fuelcoinj.core.Coin.SATOSHI;

import java.io.File;

import com.fuelcoinj.core.AbstractPeerEventListener;
import com.fuelcoinj.core.Address;
import com.fuelcoinj.core.Message;
import com.fuelcoinj.core.Peer;
import com.fuelcoinj.core.Transaction;
import com.fuelcoinj.core.Wallet;
import com.fuelcoinj.kits.WalletAppKit;
import com.fuelcoinj.params.UnitTestParams;
import com.fuelcoinj.utils.BriefLogFormatter;
import com.fuelcoinj.utils.Threading;

/**
 * This is a little test app that waits for a coin on a local regtest node, then  generates two transactions that double
 * spend the same output and sends them. It's useful for testing double spend codepaths but is otherwise not something
 * you would normally want to do.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH FUELCOIN
public class DoubleSpend {
    public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        final UnitTestParams params = UnitTestParams.get();
        WalletAppKit kit = new WalletAppKit(params, new File("."), "doublespend");
        kit.connectToLocalHost();
        kit.setAutoSave(false);
        kit.startAsync();
        kit.awaitRunning();

        System.out.println(kit.wallet());

        kit.wallet().getBalanceFuture(COIN, Wallet.BalanceType.AVAILABLE).get();
        Transaction tx1 = kit.wallet().createSend(new Address(params, "muYPFNCv7KQEG2ZLM7Z3y96kJnNyXJ53wm"), CENT);
        Transaction tx2 = kit.wallet().createSend(new Address(params, "muYPFNCv7KQEG2ZLM7Z3y96kJnNyXJ53wm"), CENT.add(SATOSHI.multiply(10)));
        final Peer peer = kit.peerGroup().getConnectedPeers().get(0);
        peer.addEventListener(new AbstractPeerEventListener() {
            @Override
            public Message onPreMessageReceived(Peer peer, Message m) {
                System.err.println("Got a message!" + m.getClass().getSimpleName() + ": " + m);
                return m;
            }
        }, Threading.SAME_THREAD);
        peer.sendMessage(tx1);
        peer.sendMessage(tx2);

        Thread.sleep(5000);
        kit.stopAsync();
        kit.awaitTerminated();
    }
}
