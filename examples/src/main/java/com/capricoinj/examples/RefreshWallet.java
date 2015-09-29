/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.capricoinj.examples;

import java.io.File;
import java.net.InetAddress;

import com.capricoinj.core.AbstractWalletEventListener;
import com.capricoinj.core.BlockChain;
import com.capricoinj.core.Coin;
import com.capricoinj.core.NetworkParameters;
import com.capricoinj.core.PeerAddress;
import com.capricoinj.core.PeerGroup;
import com.capricoinj.core.Transaction;
import com.capricoinj.core.Wallet;
import com.capricoinj.net.discovery.DnsDiscovery;
import com.capricoinj.params.MainNetParams;
import com.capricoinj.params.UnitTestParams;
import com.capricoinj.store.BlockStore;
import com.capricoinj.store.MemoryBlockStore;
import com.capricoinj.store.ValidHashStore;

/**
 * RefreshWallet loads a wallet, then processes the block chain to update the transaction pools within it.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH CAPRICOIN
public class RefreshWallet {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        Wallet wallet = Wallet.loadFromFile(file);
        System.out.println(wallet.toString());

        ValidHashStore vhs = new ValidHashStore(new File("hashstore"));
        
        // Set up the components and link them together.
        final NetworkParameters params = UnitTestParams.get();
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, wallet, blockStore, vhs);

        final PeerGroup peerGroup = new PeerGroup(params, chain);
       // peerGroup.addAddress(new PeerAddress(InetAddress.getLocalHost()));
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.startAsync();

        wallet.addEventListener(new AbstractWalletEventListener() {
            @Override
            public synchronized void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
                System.out.println("\nReceived tx " + tx.getHashAsString());
                System.out.println(tx.toString());
            }
        });

        // Now download and process the block chain.
        peerGroup.downloadBlockChain();
        peerGroup.stopAsync();
        wallet.saveToFile(file);
        System.out.println("\nDone!\n");
        System.out.println(wallet.toString());
    }
}
