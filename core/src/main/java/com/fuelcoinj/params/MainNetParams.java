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

package com.fuelcoinj.params;

import static com.google.common.base.Preconditions.checkState;

import com.fuelcoinj.core.NetworkParameters;
import com.fuelcoinj.core.Sha256Hash;
import com.fuelcoinj.core.Utils;

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
        spendableCoinbaseDepth = 6;
        String genesisHash = genesisBlock.getHashAsString();
        
        System.out.println("NOW:"+genesisHash);
        System.out.println("WAS: 000007493cfc699b263c5b6b6366cc7365fa714aeb6da55ddf14c270ac7d6205");
        
        checkState(genesisHash.equals("000007493cfc699b263c5b6b6366cc7365fa714aeb6da55ddf14c270ac7d6205"), genesisHash);

       
        checkpoints.put(100000, new Sha256Hash("11540f150127bf2df932b5a45bf0d8f7a42c4d5405b513b1f66e6c699d739679"));
        checkpoints.put(200000, new Sha256Hash("fadea2ae32e04c20ed7c1fa31aba44571f19dd475e3943ccbb7663101499d97e"));
        checkpoints.put(300000, new Sha256Hash("9bf0e74a9944157120ef16aebb82ec9d7169cf42930bc71eae6228e4fb9adccd"));
        checkpoints.put(400000, new Sha256Hash("1b86df1c9f663c97135f50559e85d29711e0739db0c9c1d863ee66c0e4c7675f"));
        checkpoints.put(500000, new Sha256Hash("dcbb40f56782e42fb7986cf95970b572cf988fef2cbcfb98e80e516d23e3aabc"));
        checkpoints.put(575000, new Sha256Hash("0de3e13073edf1e7b6883d57664118acca04349a4f33377ac48527c4dcd16b43"));
        
        dnsSeeds = new String[] {
            	"107.191.106.210",         
            	"192.52.166.157",        
                "198.105.122.95",  
                "82.211.31.120",
                "108.61.185.161", 
                "108.61.181.185",
                "100.12.92.50",
                "104.231.73.98",
                "104.250.173.62",
                "104.4.105.201",
                "104.52.230.159",
                "107.217.12.221",
                "108.180.219.179",
                "108.197.141.91",
                "108.61.41.55",
                "108.93.227.34",
                "115.134.40.137",
                "120.151.154.210",
                "130.65.109.123",
                "162.194.108.227",
                "173.52.58.5",
                "174.69.209.254",
                "184.21.185.63",
                "188.72.125.13",
                "188.72.125.25",
                "188.72.125.39",
                "188.72.125.40",
                "196.42.29.33",
                "199.167.22.248",
                "212.227.249.163",
                "24.130.45.9",
                "24.19.65.143",
                "24.88.78.209",
                "45.74.60.108",
                "45.74.61.77",
                "45.74.63.30",
                "47.20.1.67",
                "5.81.117.178",
                "50.128.194.77",
                "50.185.187.65",
                "67.166.201.138",
                "67.246.248.49",
                "68.109.73.121",
                "68.195.164.133",
                "68.53.64.217",
                "68.6.171.207",
                "68.7.143.88",
                "69.249.152.18",
                "70.164.162.45",
                "70.29.70.140",
                "71.125.246.92",
                "71.125.249.89",
                "71.95.185.83",
                "72.220.192.193",
                "72.220.238.245",
                "73.0.95.68",
                "73.36.167.54",
                "74.197.212.55",
                "75.139.87.77",
                "75.141.96.145",
                "75.183.33.44",
                "75.19.10.188",
                "75.22.119.240",
                "75.27.59.148",
                "76.105.201.7",
                "76.14.221.195",
                "76.217.17.141",
                "76.94.123.146",
                "79.142.78.227",
                "79.246.232.200",
                "81.111.14.71",
                "82.37.124.125",
                "82.75.165.126",
                "85.57.9.128",
                "87.111.39.132",
                "87.249.125.15",
                "91.156.217.32",
                "95.116.195.41",
                "97.117.174.52",
                "97.80.215.213",
                "98.112.9.30",
                "98.114.9.60",
                "99.2.218.82",
                "99.8.68.249"
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
