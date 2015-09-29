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

package com.capricoinj.examples;

import java.io.File;
import java.net.InetAddress;
import java.util.List;

import com.capricoinj.core.BlockChain;
import com.capricoinj.core.NetworkParameters;
import com.capricoinj.core.Peer;
import com.capricoinj.core.PeerAddress;
import com.capricoinj.core.PeerGroup;
import com.capricoinj.core.Sha256Hash;
import com.capricoinj.core.Transaction;
import com.capricoinj.net.discovery.DnsDiscovery;
import com.capricoinj.params.MainNetParams;
import com.capricoinj.params.UnitTestParams;
import com.capricoinj.store.BlockStore;
import com.capricoinj.store.MemoryBlockStore;
import com.capricoinj.store.ValidHashStore;
import com.capricoinj.utils.BriefLogFormatter;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Downloads the given transaction and its dependencies from a peers memory pool then prints them out.
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH CAPRICOIN

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
