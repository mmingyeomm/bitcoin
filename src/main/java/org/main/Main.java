package org.main;

import mempool.Mempool;
import network.RPC;

import java.io.IOException;


public class Main {
    private static final int BITCOINPORT = 8545;
    public static void main(String[] args) throws IOException {

        Mempool mempool = new Mempool();
        RPC rpc = new RPC(BITCOINPORT, mempool);
        rpc.start();







    }

}

