package com.capricoinj.tools;

import java.io.File;
import java.io.IOException;

import com.capricoinj.core.AbstractBlockChain;
import com.capricoinj.core.Block;
import com.capricoinj.core.BlockChain;
import com.capricoinj.core.FullPrunedBlockChain;
import com.capricoinj.core.NetworkParameters;
import com.capricoinj.core.PrunedException;
import com.capricoinj.core.VerificationException;
import com.capricoinj.params.MainNetParams;
import com.capricoinj.params.UnitTestParams;
import com.capricoinj.store.BlockStore;
import com.capricoinj.store.BlockStoreException;
import com.capricoinj.store.FullPrunedBlockStore;
import com.capricoinj.store.H2FullPrunedBlockStore;
import com.capricoinj.store.MemoryBlockStore;
import com.capricoinj.store.MemoryFullPrunedBlockStore;
import com.capricoinj.store.SPVBlockStore;
import com.capricoinj.store.ValidHashStore;
import com.capricoinj.utils.BlockFileLoader;
import com.google.common.base.Preconditions;

/** Very thin wrapper around {@link com.capricoinj.utils.BlockFileLoader} */
//WARNING THIS HAS NOT BEEN SETUP OR TESTED WITH CAPRICOIN
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
