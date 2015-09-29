package com.capricoinj.examples;

import static com.capricoinj.core.Coin.CENT;
import static com.capricoinj.core.Coin.COIN;
import static com.capricoinj.core.Coin.SATOSHI;

import java.io.File;

import com.capricoinj.core.AbstractPeerEventListener;
import com.capricoinj.core.Address;
import com.capricoinj.core.Message;
import com.capricoinj.core.Peer;
import com.capricoinj.core.Transaction;
import com.capricoinj.core.Wallet;
import com.capricoinj.kits.WalletAppKit;
import com.capricoinj.params.UnitTestParams;
import com.capricoinj.utils.BriefLogFormatter;
import com.capricoinj.utils.Threading;

/**
 * This is a little test app that waits for a coin on a local regtest node, then  generates two transactions that double
 * spend the same output and sends them. It's useful for testing double spend codepaths but is otherwise not something
 * you would normally want to do.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH CAPRICOIN
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
