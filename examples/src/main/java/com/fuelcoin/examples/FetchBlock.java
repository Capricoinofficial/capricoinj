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

package com.fuelcoin.examples;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.Future;

import com.fuelcoinj.core.Block;
import com.fuelcoinj.core.BlockChain;
import com.fuelcoinj.core.NetworkParameters;
import com.fuelcoinj.core.Peer;
import com.fuelcoinj.core.PeerAddress;
import com.fuelcoinj.core.PeerGroup;
import com.fuelcoinj.core.Sha256Hash;
import com.fuelcoinj.net.discovery.DnsDiscovery;
import com.fuelcoinj.params.MainNetParams;
import com.fuelcoinj.store.BlockStore;
import com.fuelcoinj.store.MemoryBlockStore;
import com.fuelcoinj.store.ValidHashStore;
import com.fuelcoinj.utils.BriefLogFormatter;

/**
 * Downloads the block given a block hash from the localhost node and prints it out.
 */
//WARNING THIS IS ON THE MAINNET OF FUELCOIN
public class FetchBlock {
    public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        System.out.println("Connecting to node");
        final NetworkParameters params = MainNetParams.get();

        ValidHashStore vhs = new ValidHashStore(new File("hashstore"));
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, blockStore, vhs);
        PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.startAsync();
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.awaitRunning();
        peerGroup.waitForPeers(1).get();
        Peer peer = peerGroup.getConnectedPeers().get(0);

        Sha256Hash blockHash = new Sha256Hash(MainNetParams.get().getGenesisBlock().getHashAsString());
        Future<Block> future = peer.getBlock(blockHash);
        System.out.println("Waiting for node to send us the requested block: " + blockHash);
        Block block = future.get();
        System.out.println(block);
        peerGroup.stopAsync();
    }
}
