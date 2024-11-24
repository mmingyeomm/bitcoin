package transaction;

public class Output {
    private double amount;
    private LockingScript lockingScript;

    public Output(double amount, LockingScript lockingScript) {
        this.amount = amount;
        this.lockingScript = lockingScript;
    }

    public double getAmount() {
        return amount;
    }

    public LockingScript getLockingScript() {
        return lockingScript;
    }

    @Override
    public String toString() {
        return "Output{" +
                "amount=" + amount +
                ", lockingScript=" + lockingScript +
                '}';
    }
}
