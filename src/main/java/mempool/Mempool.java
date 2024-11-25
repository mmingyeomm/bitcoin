package mempool;

import db.Database;
import opCodeStack.OPCodeStack;
import transaction.Input;
import transaction.Output;
import transaction.Transaction;
import utxo.UTXO;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Mempool {
    private Database database;

    public Mempool(Database database){
        this.database = database;
    }

    private List<Transaction> mempool = new ArrayList<>();

    public boolean addRequest(Transaction tx) throws Exception {

        if (this.ValidateTxInput(tx) ){
                this.AddtoMempool(tx);

                System.out.println("transaction added to mempool");
                System.out.println("view mempool");
                this.viewMempool();
                return true;

        } else {
            System.out.println("transaction validation failed");
            return false;
        }

    }

    private boolean ValidateTxInput(Transaction tx) throws Exception {
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
            System.out.println("input script stack before verify" + inputStack);


            String[] scriptPubKeyOpcodes = currentUTXO.getScriptPubkey().getAsm().split(" ");

            // 스택을 통해 verify
            for (int i = 0; i < scriptPubKeyOpcodes.length; i++) {
                if (scriptPubKeyOpcodes[i].startsWith("OP_") ){
                    String opcode = scriptPubKeyOpcodes[i].substring(3); // "OP_" 이후의 문자열 추출

                    switch (opcode) {
                        case "CHECKMULTISIG":
                            inputStack.op_checkmultisig();
                            System.out.println("Processing CHECKMULTISIG operation");
                            break;
                        case "CHECKMULTISIGVERIFY":
                            inputStack.op_checkmultisigverify();
                            System.out.println("Processing CHECKMULTISIGVERIFY operation");
                            break;
                        case "DUP":
                            inputStack.op_dup();
                            System.out.println("Processing DUP operation");
                            break;
                        case "HASH160":
                            inputStack.op_hash160();
                            System.out.println("Processing HASH160 operation");
                            break;
                        case "EQUALVERIFY":
                            inputStack.op_equalverify();
                            System.out.println("Processing EQUALVERIFY operation");
                            break;
                        case "CHECKSIG":
                            inputStack.op_checksig();
                            System.out.println("Processing CHECKSIG operation");
                            break;
                        default:
                            System.out.println("Unknown operation: " + opcode);
                            break;
                    }

                }
                else {
                    inputStack.push(scriptPubKeyOpcodes[i]);
                }




            }

            //밥먹고 할일 각 input마다 스택 보고 true false
            // ecdsa도 다시



            System.out.println("input script stack after verify" + inputStack);
            if (inputStack.size() == 1) {
                String verify = inputStack.pop();
            }



            if (!validInput) {
                System.out.println("Input not found in UTXO set: " + input.getPreviousTxHash());
                return false;
            }
            System.out.println("Input succesfully validated");
        }


        return true;
    }


    private void AddtoMempool(Transaction tx){
        mempool.add(tx);
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

}
