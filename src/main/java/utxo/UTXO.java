package utxo;

public class UTXO {
    private String txid;
    private int vout;
    private double amount;
    private ScriptPubKey scriptPubkey;

    // Getters and Setters
    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getVout() {
        return vout;
    }

    public void setVout(int vout) {
        this.vout = vout;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ScriptPubKey getScriptPubkey() {
        return scriptPubkey;
    }

    public void setScriptPubkey(ScriptPubKey scriptPubkey) {
        this.scriptPubkey = scriptPubkey;
    }

}
