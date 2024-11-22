package utxo;

public class UTXO {
    public String txid;
    public int vout;
    public double amount;
    public Map<String, Object> script_pubkey;
    public String address;
    public int confirmations;
    public boolean spendable;
    public boolean solvable;
}
