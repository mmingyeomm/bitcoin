package utxo;  // or whatever package you're using

import java.util.List;
import java.util.Map;

public class UTXOSet {
    private List<UTXO> utxos;
    public UTXOSet(){}
    public UTXOSet(List<UTXO> utxos){
        this.utxos = utxos;
    }

    // Getters and Setters
    public List<UTXO> getUtxos() {
        return utxos;
    }

    public void setUtxos(List<UTXO> utxos) {
        this.utxos = utxos;
    }



    @Override
    public String toString() {
        return "UTXOSet{" +
                "utxos=" + utxos +
                '}';
    }
}
