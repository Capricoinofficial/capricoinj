package com.fuelcoin.tools;

import java.io.File;
import java.io.IOException;

import com.fuelcoinj.core.AbstractBlockChain;
import com.fuelcoinj.core.Block;
import com.fuelcoinj.core.BlockChain;
import com.fuelcoinj.core.FullPrunedBlockChain;
import com.fuelcoinj.core.NetworkParameters;
import com.fuelcoinj.core.PrunedException;
import com.fuelcoinj.core.VerificationException;
import com.fuelcoinj.params.MainNetParams;
import com.fuelcoinj.params.UnitTestParams;
import com.fuelcoinj.store.BlockStore;
import com.fuelcoinj.store.BlockStoreException;
import com.fuelcoinj.store.FullPrunedBlockStore;
import com.fuelcoinj.store.H2FullPrunedBlockStore;
import com.fuelcoinj.store.MemoryBlockStore;
import com.fuelcoinj.store.MemoryFullPrunedBlockStore;
import com.fuelcoinj.store.SPVBlockStore;
import com.fuelcoinj.store.ValidHashStore;
import com.fuelcoinj.utils.BlockFileLoader;
import com.google.common.base.Preconditions;

/** Very thin wrapper around {@link com.fuelcoinj.utils.BlockFileLoader} */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH FUELCOIN
public class BlockImporter {
    public static void main(String[] args) throws BlockStoreException, VerificationException, PrunedException, IOException {
        System.out.println("USAGE: BlockImporter (prod|test) (H2|Disk|MemFull|Mem|SPV) [blockStore]");
        System.out.println("       blockStore is required unless type is Mem or MemFull");
        System.out.println("       eg BlockImporter prod H2 /home/user/peercoinj.h2store");
        System.out.println("       Does full verification if the store supports it");
        Preconditions.checkArgument(args.length == 2 || args.length == 3);
        
        NetworkParameters params;
        if (args[0].equals("test"))
            params = UnitTestParams.get();
        else
            params = MainNetParams.get();
        
        BlockStore store;
        if (args[1].equals("H2")) {
            Preconditions.checkArgument(args.length == 3);
            store = new H2FullPrunedBlockStore(params, args[2], 100);
        } else if (args[1].equals("MemFull")) {
            Preconditions.checkArgument(args.length == 2);
            store = new MemoryFullPrunedBlockStore(params, 100);
        } else if (args[1].equals("Mem")) {
            Preconditions.checkArgument(args.length == 2);
            store = new MemoryBlockStore(params);
        } else if (args[1].equals("SPV")) {
            Preconditions.checkArgument(args.length == 3);
            store = new SPVBlockStore(params, new File(args[2]));
        } else {
            System.err.println("Unknown store " + args[1]);
            return;
        }
        
        AbstractBlockChain chain = null;
        ValidHashStore hashstore =  new ValidHashStore(new File("hashstore"));
        if (store instanceof FullPrunedBlockStore)
            chain = new FullPrunedBlockChain(params, (FullPrunedBlockStore) store, hashstore);
        else
            chain = new BlockChain(params, store, hashstore);
        
        BlockFileLoader loader = new BlockFileLoader(params, BlockFileLoader.getReferenceClientBlockFileList());
        
        for (Block block : loader)
            chain.add(block);
    }
}
