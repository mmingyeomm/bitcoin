package transactionRecord;

public class TransactionRecord {
    private final String txid;
    private final boolean isValid;
    private final long timestamp;

    public TransactionRecord(String txid, boolean isValid) {
        this.txid = txid;
        this.isValid = isValid;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTxid() {
        return txid;
    }

    public boolean isValid() {
        return isValid;
    }


}
