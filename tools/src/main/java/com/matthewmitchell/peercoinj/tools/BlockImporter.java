package com.matthewmitchell.peercoinj.tools;

import java.io.File;

import com.google.common.base.Preconditions;
import com.matthewmitchell.peercoinj.core.AbstractBlockChain;
import com.matthewmitchell.peercoinj.core.Block;
import com.matthewmitchell.peercoinj.core.BlockChain;
import com.matthewmitchell.peercoinj.core.FullPrunedBlockChain;
import com.matthewmitchell.peercoinj.core.NetworkParameters;
import com.matthewmitchell.peercoinj.core.PrunedException;
import com.matthewmitchell.peercoinj.core.VerificationException;
import com.matthewmitchell.peercoinj.params.MainNetParams;
import com.matthewmitchell.peercoinj.store.BlockStore;
import com.matthewmitchell.peercoinj.store.BlockStoreException;
import com.matthewmitchell.peercoinj.store.FullPrunedBlockStore;
import com.matthewmitchell.peercoinj.store.H2FullPrunedBlockStore;
import com.matthewmitchell.peercoinj.store.MemoryBlockStore;
import com.matthewmitchell.peercoinj.store.MemoryFullPrunedBlockStore;
import com.matthewmitchell.peercoinj.store.SPVBlockStore;
import com.matthewmitchell.peercoinj.utils.BlockFileLoader;

/** Very thin wrapper around {@link com.matthewmitchell.peercoinj.utils.BlockFileLoader} */
public class BlockImporter {
    public static void main(String[] args) throws BlockStoreException, VerificationException, PrunedException {
        System.out.println("USAGE: BlockImporter (prod|test) (H2|Disk|MemFull|Mem|SPV) [blockStore]");
        System.out.println("       blockStore is required unless type is Mem or MemFull");
        System.out.println("       eg BlockImporter prod H2 /home/user/peercoinj.h2store");
        System.out.println("       Does full verification if the store supports it");
        Preconditions.checkArgument(args.length == 2 || args.length == 3);
        
        NetworkParameters params;
        if (args[0].equals("test"))
            params = TestNet3Params.get();
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
        if (store instanceof FullPrunedBlockStore)
            chain = new FullPrunedBlockChain(params, (FullPrunedBlockStore) store);
        else
            chain = new BlockChain(params, store);
        
        BlockFileLoader loader = new BlockFileLoader(params, BlockFileLoader.getReferenceClientBlockFileList());
        
        for (Block block : loader)
            chain.add(block);
    }
}
