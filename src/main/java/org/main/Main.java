package org.main;

import executionEngine.ExecutionEngine;
import db.Database;
import mempool.Mempool;
import network.RPC;

import java.io.IOException;


public class Main {
    private static final int BITCOINPORT = 8545;
    public static void main(String[] args) throws IOException {

        Database database = new Database();

        Mempool mempool = new Mempool(database);

        ExecutionEngine executionEngine = new ExecutionEngine(database);

        RPC rpc = new RPC(BITCOINPORT, mempool);
        rpc.start();


    }

}

