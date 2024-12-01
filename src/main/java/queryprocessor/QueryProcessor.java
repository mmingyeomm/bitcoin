package queryprocessor;

import db.Database;
import mempool.Mempool;
import transactionRecord.TransactionRecord;
import transactionRecord.TransactionRecordList;
import utxo.UTXO;
import java.util.List;
import java.util.Scanner;

public class QueryProcessor {
    private final Database database;
    private final TransactionRecordList transactionRecordList;
    private boolean isRunning = true;

    public QueryProcessor(Database database, TransactionRecordList transactionRecordList) {
        this.database = database;
        this.transactionRecordList = transactionRecordList;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Query Processor started. Type commands (snapshot transactions/snapshot utxoset/exit):");

        while (isRunning) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case "snapshot transactions":
                    showTransactionSnapshot();
                    break;
                case "snapshot utxoset":
                    showUTXOSetSnapshot();
                    break;
                case "exit":
                    isRunning = false;
                    break;
                default:
                    showAvailableCommands();
            }
        }

        scanner.close();
    }

    private void showAvailableCommands() {
        System.out.println("Unknown command. Available commands:");
        System.out.println("- snapshot transactions");
        System.out.println("- snapshot utxoset");
        System.out.println("- exit");
    }

    private void showTransactionSnapshot() {
        List<TransactionRecord> processedTxs = transactionRecordList.getRecords();

        if (processedTxs.isEmpty()) {
            System.out.println("No processed transactions yet.");
            return;
        }

        System.out.println("Processed Transactions:");
        for (TransactionRecord tx : processedTxs) {
            System.out.printf("transaction: %s, validity check: %s%n",
                    tx.getTxid(),
                    tx.isValid() ? "passed" : "failed");
        }
    }

    private void showUTXOSetSnapshot() {
        List<UTXO> utxos = database.getUTXOSet().getUtxos();

        if (utxos.isEmpty()) {
            System.out.println("UTXO set is empty.");
            return;
        }

        System.out.println("Current UTXO Set:");
        for (int i = 0; i < utxos.size(); i++) {
            UTXO utxo = utxos.get(i);
            printUTXOInfo(i, utxo);

            if (i < utxos.size() - 1) {
                System.out.println();
            }
        }
    }

    private void printUTXOInfo(int index, UTXO utxo) {
        System.out.printf("utxo%d: %s, output index: %d, amount: %.8f, locking script: %s%n",
                index,
                utxo.getTxid(),
                utxo.getVout(),
                utxo.getAmount(),
                utxo.getScriptPubkey().getAsm());
    }
}
