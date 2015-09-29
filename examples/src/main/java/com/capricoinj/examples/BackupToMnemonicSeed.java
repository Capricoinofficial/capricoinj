package com.capricoinj.examples;

import com.capricoinj.core.NetworkParameters;
import com.capricoinj.core.Wallet;
import com.capricoinj.params.UnitTestParams;
import com.capricoinj.wallet.DeterministicSeed;
import com.google.common.base.Joiner;

/**
 * The following example shows you how to create a deterministic seed from a hierarchical deterministic wallet represented as a mnemonic code.
 * This seed can be used to fully restore your wallet. The RestoreFromSeed.java example shows how to load the wallet from this seed.
 * 
 * In Peercoin Improvement Proposal (BIP) 39 and BIP 32 describe the details about hierarchical deterministic wallets and mnemonic sentences
 * https://github.com/peercoin/bips/blob/master/bip-0039.mediawiki
 * https://github.com/peercoin/bips/blob/master/bip-0032.mediawiki
 */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH CAPRICOIN
public class BackupToMnemonicSeed {

    public static void main(String[] args) {

        NetworkParameters params = UnitTestParams.get();
        Wallet wallet = new Wallet(params);

        DeterministicSeed seed = wallet.getKeyChainSeed();
        System.out.println("seed: " + seed.toString());

        System.out.println("creation time: " + seed.getCreationTimeSeconds());
        System.out.println("mnemonicCode: " + Joiner.on(" ").join(seed.getMnemonicCode()));
    }
}
