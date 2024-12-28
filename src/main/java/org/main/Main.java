package org.main;

import executionEngine.ExecutionEngine;
import db.Database;
import mempool.Mempool;
import network.Network;
import queryprocessor.QueryProcessor;
import transactionRecord.TransactionRecordList;

import java.io.IOException;


public class Main {
    private static final int BITCOINPORT = 8545;
    public static void main(String[] args) throws IOException {

        Database database = new Database();

        TransactionRecordList transactionRecordList = new TransactionRecordList();

        Mempool mempool = new Mempool(database, transactionRecordList);

        ExecutionEngine executionEngine = new ExecutionEngine(database, mempool);

        Network network = new Network(BITCOINPORT, mempool);
        network.start();
        QueryProcessor queryProcessor = new QueryProcessor(database, transactionRecordList);
        queryProcessor.start();
        

    }
}






