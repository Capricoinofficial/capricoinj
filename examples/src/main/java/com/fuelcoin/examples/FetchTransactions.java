/*
 * Copyright 2012 Google Inc.
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

package com.fuelcoin.examples;

import java.io.File;
import java.net.InetAddress;
import java.util.List;

import com.fuelcoinj.core.BlockChain;
import com.fuelcoinj.core.NetworkParameters;
import com.fuelcoinj.core.Peer;
import com.fuelcoinj.core.PeerAddress;
import com.fuelcoinj.core.PeerGroup;
import com.fuelcoinj.core.Sha256Hash;
import com.fuelcoinj.core.Transaction;
import com.fuelcoinj.net.discovery.DnsDiscovery;
import com.fuelcoinj.params.MainNetParams;
import com.fuelcoinj.params.UnitTestParams;
import com.fuelcoinj.store.BlockStore;
import com.fuelcoinj.store.MemoryBlockStore;
import com.fuelcoinj.store.ValidHashStore;
import com.fuelcoinj.utils.BriefLogFormatter;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Downloads the given transaction and its dependencies from a peers memory pool then prints them out.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH FUELCOIN

public class FetchTransactions {
    public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        System.out.println("Connecting to node");
        final NetworkParameters params = UnitTestParams.get();

        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, blockStore, new ValidHashStore(new File("hashstore")));
        PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.startAsync();
        peerGroup.awaitRunning();
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.waitForPeers(1).get();
        Peer peer = peerGroup.getConnectedPeers().get(0);

        Sha256Hash txHash = new Sha256Hash(MainNetParams.get().getGenesisBlock().getHashAsString());
        ListenableFuture<Transaction> future = peer.getPeerMempoolTransaction(txHash);
        System.out.println("Waiting for node to send us the requested transaction: " + txHash);
        Transaction tx = future.get();
        System.out.println(tx);

        System.out.println("Waiting for node to send us the dependencies ...");
        List<Transaction> deps = peer.downloadDependencies(tx).get();
        for (Transaction dep : deps) {
            System.out.println("Got dependency " + dep.getHashAsString());
        }

        System.out.println("Done.");
        peerGroup.stopAsync();
        peerGroup.awaitTerminated();
    }
}
