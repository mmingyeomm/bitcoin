package queryprocessor;

import db.Database;
import mempool.Mempool;
import utxo.UTXO;
import java.util.List;
import java.util.Scanner;

public class QueryProcessor {
    private final Database database;
    private final Mempool mempool;
    private boolean isRunning = true;

    public QueryProcessor(Database database, Mempool mempool) {
        this.database = database;
        this.mempool = mempool;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Query Processor started. Type commands (snapshot transactions/snapshot utxoset/exit):");

        while (isRunning) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case "snapshot transactions":
//                    showTransactionSnapshot();
                    break;
                case "snapshot utxoset":
                    showUTXOSetSnapshot();
                    break;
                case "exit":
                    isRunning = false;
                    break;
                default:
                    System.out.println("Unknown command. Available commands:");
                    System.out.println("- snapshot transactions");
                    System.out.println("- snapshot utxoset");
                    System.out.println("- exit");
            }
        }

        scanner.close();
    }

//    private void showTransactionSnapshot() {
//        List<TransactionRecord> processedTxs = database.getProcessedTransactions();
//
//        if (processedTxs.isEmpty()) {
//            System.out.println("No processed transactions yet.");
//            return;
//        }
//
//        System.out.println("Processed Transactions:");
//        for (TransactionRecord tx : processedTxs) {
//            System.out.printf("transaction: %s, validity check: %s%n",
//                    tx.getTxid(),
//                    tx.isValid() ? "passed" : "failed");
//        }
//    }

    private void showUTXOSetSnapshot() {
        List<UTXO> utxos = database.getUTXOSet().getUtxos();

        if (utxos.isEmpty()) {
            System.out.println("UTXO set is empty.");
            return;
        }

        System.out.println("Current UTXO Set:");
        for (int i = 0; i < utxos.size(); i++) {
            UTXO utxo = utxos.get(i);
            System.out.printf("utxo%d: %s, output index%d, amount%d, locking script%s%n",
                    i, utxo.getTxid(), utxo.getVout(), utxo.getAmount(),
                    utxo.getScriptPubkey().getAsm());
        }
    }
}
