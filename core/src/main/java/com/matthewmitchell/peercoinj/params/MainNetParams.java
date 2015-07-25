/*
 * Copyright 2013 Google Inc.
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

package com.matthewmitchell.peercoinj.params;

import static com.google.common.base.Preconditions.checkState;

import com.matthewmitchell.peercoinj.core.NetworkParameters;
import com.matthewmitchell.peercoinj.core.Utils;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends NetworkParameters {
    public MainNetParams() {
        super();
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(504365055L);
        dumpedPrivateKeyHeader = 128 + 35;
        addressHeader = 35;
        p2shHeader = 28;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        port = 34162;
        packetMagic= 2711659171L;
       // genesisBlock = createGenesis(this);
        genesisBlock.setDifficultyTarget(504365055L);
        genesisBlock.setTime(1406900180);
        genesisBlock.setNonce(179517);
        id = ID_MAINNET;
        spendableCoinbaseDepth = 100;
        String genesisHash = genesisBlock.getHashAsString();
        
        System.out.println("NOW:"+genesisHash);
        System.out.println("WAS: 000007493cfc699b263c5b6b6366cc7365fa714aeb6da55ddf14c270ac7d6205");
        
        checkState(genesisHash.equals("000007493cfc699b263c5b6b6366cc7365fa714aeb6da55ddf14c270ac7d6205"), genesisHash);

       // checkpoints.put(19080, new Sha256Hash("000000000000bca54d9ac17881f94193fd6a270c1bb21c3bf0b37f588a40dbd7"));
       // checkpoints.put(30583, new Sha256Hash("d39d1481a7eecba48932ea5913be58ad3894c7ee6d5a8ba8abeb772c66a6696e"));
       // checkpoints.put(99999, new Sha256Hash("27fd5e1de16a4270eb8c68dee2754a64da6312c7c3a0e99a7e6776246be1ee3f"));

        dnsSeeds = new String[] {
            	"107.191.106.210",         
            	"192.52.166.157",        
                "198.105.122.95",  
                "82.211.31.120",
                "108.61.185.161", 
                "108.61.181.185",
        };
    }

    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
