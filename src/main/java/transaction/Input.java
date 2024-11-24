package transaction;

public class Input {

    private String previous_tx_hash;
    private long previous_output_index;
    private UnlockingScript unlockingScript;

    public Input(String previous_tx_hash, long previous_output_index, UnlockingScript unlockingScript) {
        this.previous_tx_hash = previous_tx_hash;
        this.previous_output_index = previous_output_index;
        this.unlockingScript = unlockingScript;
    }

    public String getPreviousTxHash() {
        return previous_tx_hash;
    }

    public long getPreviousOutputIndex() {
        return previous_output_index;
    }

    public UnlockingScript getUnlockingScript() {
        return unlockingScript;
    }

    @Override
    public String toString() {
        return "Input{" +
                "previous_tx_hash='" + previous_tx_hash + '\'' +
                ", previous_output_index=" + previous_output_index +
                ", unlockingScript=" + unlockingScript +
                '}';
    }

}
