package transaction;

import java.util.List;

public class Transaction {
    private int version;
    private List<Input> inputs;
    private List<Output> outputs;
    private long locktime;
    private String type;

    public Transaction(int version, List<Input> inputs, List<Output> outputs, long locktime, String type) {
        this.version = version;
        this.inputs = inputs;
        this.outputs = outputs;
        this.locktime = locktime;
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public long getLocktime() {
        return locktime;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "version=" + version +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                ", locktime=" + locktime +
                ", type='" + type + '\'' +
                '}';
    }

}

