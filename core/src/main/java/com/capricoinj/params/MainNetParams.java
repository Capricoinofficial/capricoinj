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

package com.capricoinj.params;

import static com.google.common.base.Preconditions.checkState;

import com.capricoinj.core.NetworkParameters;
import com.capricoinj.core.Sha256Hash;
import com.capricoinj.core.Utils;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends NetworkParameters {
    public MainNetParams() {
        super();
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(504365055L);
        dumpedPrivateKeyHeader = 128 + 28;  // 128 + ( PUB_KEY_ADDRESS as seen here --- https://github.com/Capricoinofficial/CapriCoin/blob/master/src/base58.h#L279 )
        addressHeader = 28;
        p2shHeader = 35;  // SCRIPT_ADDRESS as seen here 
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        port = 22714;
        
        
        
        packetMagic= 2711659171L;  // 
       // genesisBlock = createGenesis(this);
        genesisBlock.setDifficultyTarget(504365055L);
        genesisBlock.setTime(1435945406); 
        genesisBlock.setNonce(303451);
        id = ID_MAINNET;
        spendableCoinbaseDepth = 6;
        String genesisHash = genesisBlock.getHashAsString();
        
        System.out.println("NOW:"+genesisHash);
        System.out.println("WAS: 00000d23fa0fc52c90893adb1181c9ddffb6c797a3e41864b9a23aa2f2981fe3");
        
        checkState(genesisHash.equals("00000d23fa0fc52c90893adb1181c9ddffb6c797a3e41864b9a23aa2f2981fe3"), genesisHash);
        
        dnsSeeds = new String[] {
        		"95.85.49.153",
        		"95.85.60.40",
        		"149.202.137.169",
        		"45.55.58.160",
        		"159.203.85.176",
        		"128.199.211.65",
        		"128.199.242.30"
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
