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

package com.peercoin.peercoin.examples;

import java.io.File;
import java.net.InetAddress;

import com.matthewmitchell.peercoinj.core.AbstractWalletEventListener;
import com.matthewmitchell.peercoinj.core.BlockChain;
import com.matthewmitchell.peercoinj.core.Coin;
import com.matthewmitchell.peercoinj.core.NetworkParameters;
import com.matthewmitchell.peercoinj.core.PeerAddress;
import com.matthewmitchell.peercoinj.core.PeerGroup;
import com.matthewmitchell.peercoinj.core.Transaction;
import com.matthewmitchell.peercoinj.core.Wallet;
import com.matthewmitchell.peercoinj.net.discovery.DnsDiscovery;
import com.matthewmitchell.peercoinj.params.MainNetParams;
import com.matthewmitchell.peercoinj.store.BlockStore;
import com.matthewmitchell.peercoinj.store.MemoryBlockStore;
import com.matthewmitchell.peercoinj.store.ValidHashStore;

/**
 * RefreshWallet loads a wallet, then processes the block chain to update the transaction pools within it.
 */
public class RefreshWallet {
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        Wallet wallet = Wallet.loadFromFile(file);
        System.out.println(wallet.toString());

        ValidHashStore vhs = new ValidHashStore(new File("hashstore"));
        
        // Set up the components and link them together.
        final NetworkParameters params = MainNetParams.get();
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
