package executionEngine;

import db.Database;
import mempool.Mempool;
import transaction.Input;
import transaction.LockingScript;
import transaction.Output;
import transaction.Transaction;
import utxo.ScriptPubKey;
import utxo.UTXO;
import utxo.UTXOSet;
import script.ScriptPubKey;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutionEngine {
    private final Database database;
    private final Mempool mempool;
    private static final int BATCH_SIZE = 5;
    private final ScheduledExecutorService scheduler;
    private boolean isRunning = true;

    public ExecutionEngine(Database database, Mempool mempool) {
        this.mempool = mempool;
        this.database = database;
        this.scheduler = Executors.newScheduledThreadPool(1);
        startWatching();
        System.out.println("Execution Engine Initiated");
    }

    private void startWatching() {
        Thread watchThread = new Thread(() -> {
            while (isRunning) {
                if (mempool.size() >= BATCH_SIZE) {
                    System.out.println("Executing batch of transactions...");

                    for (int i = 0; i < BATCH_SIZE; i++) {
                        Transaction transaction = mempool.getTransactions().get(i);
                        processTransaction(transaction);
                        removeFromMempool(transaction);
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        watchThread.start();
    }

    private void processTransaction(Transaction transaction) {
        UTXOSet currentUtxoSet = database.getUTXOSet();
        List<UTXO> updatedUTXOs = new ArrayList<>(currentUtxoSet.getUtxos());

        for (Input input : transaction.getInputs()) {
            String prevTxHash = input.getPreviousTxHash();
            updatedUTXOs.removeIf(utxo -> utxo.getTxid().equals(prevTxHash));
        }

        String txid = calculateTXID(transaction);
        for (int i = 0; i < transaction.getOutputs().size(); i++) {
            Output output = transaction.getOutputs().get(i);
            UTXO newUTXO = createUTXO(txid, i, output);
            updatedUTXOs.add(newUTXO);
        }
        database.updateUTXOSet(updatedUTXOs);
    }

    private void removeSpentUTXOs(List<UTXO> currentUTXOs, Transaction transaction) {
        for (Input input : transaction.getInputs()) {
            String prevTxHash = input.getPreviousTxHash();
            currentUTXOs.removeIf(utxo -> utxo.getTxid().equals(prevTxHash));
        }
    }

    private void addNewUTXOs(List<UTXO> currentUTXOs, Transaction transaction) {
        String txid = calculateTXID(transaction);

        for (int i = 0; i < transaction.getOutputs().size(); i++) {
            Output output = transaction.getOutputs().get(i);
            UTXO newUTXO = createUTXO(txid, i, output);
            currentUTXOs.add(newUTXO);
        }
    }

    private UTXO createUTXO(String txid, int vout, Output output) {
        UTXO utxo = new UTXO();
        utxo.setTxid(txid);
        utxo.setVout(vout);
        utxo.setAmount(output.getAmount());

        ScriptPubKey scriptPubKey = new ScriptPubKey();
        scriptPubKey.setAsm(convertToAsm(output.getLockingScript()));
        scriptPubKey.setHex(convertToHex(output.getLockingScript()));
        scriptPubKey.setType(determineScriptType(output.getLockingScript()));

        utxo.setScriptPubkey(scriptPubKey);
        return utxo;
    }

    private void removeFromMempool(Transaction transaction) {
        mempool.getTransactions().remove(transaction);

    }

    // TXID 계산 메소드 (이전 구현 사용)
    private String calculateTXID(Transaction transaction) {
        // 이전에 구현한 TXID 계산 로직
        return ""; // 실제 구현 필요
    }

    private String convertToAsm(LockingScript lockingScript) {
        StringBuilder asm = new StringBuilder();
        for (String op : lockingScript.getScriptPubKey()) {
            if (asm.length() > 0) {
                asm.append(" ");
            }
            asm.append(op);
        }
        return asm.toString();
    }

    private String convertToHex(LockingScript lockingScript) {
        // 스크립트를 hex로 변환하는 로직
        return ""; // 실제 구현 필요
    }

    private String determineScriptType(LockingScript lockingScript) {
        List<String> script = lockingScript.getScriptPubKey();

        // P2PKH
        if (script.size() == 5 &&
                script.get(0).equals("OP_DUP") &&
                script.get(1).equals("OP_HASH160") &&
                script.get(3).equals("OP_EQUALVERIFY") &&
                script.get(4).equals("OP_CHECKSIG")) {
            return "p2pkh";
        }

        // P2SH
        if (script.size() == 3 &&
                script.get(0).equals("OP_HASH160") &&
                script.get(2).equals("OP_EQUAL")) {
            return "p2sh";
        }

        // Multisig
        if (script.get(script.size() - 1).equals("OP_CHECKMULTISIG")) {
            return "multisig";
        }

        return "unknown";
    }

    public void stop() {
        isRunning = false;
        scheduler.shutdown();
    }
}
