package transactionRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionRecordList {
    private final List<TransactionRecord> records;

    public TransactionRecordList() {
        this.records = new ArrayList<>();
    }

    public void addRecord(String txid, boolean isValid) {
        if (txid != null) {
            TransactionRecord record = new TransactionRecord(txid, isValid);
            records.add(record);
        }
    }
    public List<TransactionRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public TransactionRecord findByTxid(String txid) {
        if (txid == null) return null;

        return records.stream()
                .filter(record -> txid.equals(record.getTxid()))
                .findFirst()
                .orElse(null);
    }

    public List<TransactionRecord> getValidTransactions() {
        return records.stream()
                .filter(TransactionRecord::isValid)
                .toList();
    }

    public List<TransactionRecord> getInvalidTransactions() {
        return records.stream()
                .filter(record -> !record.isValid())
                .toList();
    }

    public int size() {
        return records.size();
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public void clear() {
        records.clear();
    }


}
