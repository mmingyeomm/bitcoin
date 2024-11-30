package mempool;

import db.Database;
import executionEngine.HandleP2SH;
import executionEngine.StackEngine;
import opCodeStack.OPCodeStack;
import transaction.Input;
import transaction.Output;
import transaction.Transaction;
import utxo.UTXO;

import java.util.ArrayList;
import java.util.List;

public class Mempool {
    private Database database;

    public Mempool(Database database){
        this.database = database;
    }

    private List<Transaction> mempool = new ArrayList<>();

    public boolean addRequest(Transaction tx) throws Exception {

        if (this.ValidateTxInput(tx) ){
                this.AddtoMempool(tx);

                System.out.println("view mempool: ");
                this.viewMempool();
                return true;

        } else {
            System.out.println("transaction validation failed");
            return false;
        }

    }

    private boolean ValidateTxInput(Transaction tx) throws Exception {
        //validate tx 함수가 좀 헤비한 감이 있다.
        System.out.println("validating tx Input...");
        List<Input> inputs = tx.getInputs();
        List<UTXO> utxos = database.getUTXOSet().getUtxos();

        for (Input input : inputs) {
            boolean validInput = false;

            UTXO currentUTXO = null;
            OPCodeStack inputStack = new OPCodeStack();
            // input의 UTXO가 있나 확인
            for (UTXO utxo : utxos) {
                if (utxo.getTxid().equals(input.getPreviousTxHash()) &&
                        utxo.getVout() == input.getPreviousOutputIndex()) {

                    currentUTXO = utxo;
                    validInput = true;
                    break;
                }

            }
            for (int i = 0; i < input.getUnlockingScript().getScriptSig().size(); i++) {
                inputStack.push(input.getUnlockingScript().getScriptSig().get(i));
            }
            String[] scriptPubKeyOpcodes = currentUTXO.getScriptPubkey().getAsm().split(" ");


            if (currentUTXO.getScriptPubkey().getType().equals("p2sh")){
                // P2SH tx 따로 처리
                System.out.println("p2sh transaction");
                inputStack = HandleP2SH.handleP2SH(scriptPubKeyOpcodes, inputStack);
            } else {
                // P2PKH, MULTISIG tx 처리
                inputStack = StackEngine.executeOPCODE(scriptPubKeyOpcodes, inputStack);
            }


            if (StackEngine.isValid(inputStack)){
                validInput = true;
            } else {
                validInput = false;
            }

            // ecdsa도 다시

            if (!validInput) {
                System.out.println("Input not valid: " + input.getPreviousTxHash());
                return false;
            }
            System.out.println("Input succesfully validated");
        }


        return true;
    }

    private void AddtoMempool(Transaction tx){
        mempool.add(tx);
    }

    public void DiscardTransaction(Transaction tx){
        mempool.remove(tx);
    }
    public List<Transaction> getTransactions(){
        return mempool;
    }

    public void viewMempool() {
        if (mempool.isEmpty()) {
            System.out.println("Mempool is empty");
            return;
        }

        for (int i = 0; i < mempool.size(); i++) {
            Transaction tx = mempool.get(i);
            System.out.println("\nTransaction #" + (i + 1) + ":");
            System.out.println("Version: " + tx.getVersion());
            System.out.println("Type: " + tx.getType());
            System.out.println("Locktime: " + tx.getLocktime());

            System.out.println("\nInputs:");
            List<Input> inputs = tx.getInputs();
            if (inputs != null) {
                for (int j = 0; j < inputs.size(); j++) {
                    Input input = inputs.get(j);
                    System.out.println("  Input #" + (j + 1) + ":");
                    System.out.println("    Previous TX Hash: " + input.getPreviousTxHash());
                    System.out.println("    Previous Output Index: " + input.getPreviousOutputIndex());
                    System.out.println("    Unlocking Script: " + input.getUnlockingScript().getScriptSig());
                }
            }

            System.out.println("\nOutputs:");
            List<Output> outputs = tx.getOutputs();
            if (outputs != null) {
                for (int j = 0; j < outputs.size(); j++) {
                    Output output = outputs.get(j);
                    System.out.println("  Output #" + (j + 1) + ":");
                    System.out.println("    Amount: " + output.getAmount());
                    System.out.println("    Locking Script: " + output.getLockingScript().getScriptPubKey());
                }
            }

            System.out.println("\n----------------------------------------");
        }
    }

    public int size(){
        return mempool.size();
    }

}
