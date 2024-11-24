package transaction;

import java.util.List;

public class UnlockingScript {
    private List<String> scriptSig;

    public UnlockingScript(List<String> scriptSig) {
        this.scriptSig = scriptSig;
    }

    public List<String> getScriptSig() {
        return scriptSig;
    }

    public void setScriptSig(List<String> scriptSig) {
        this.scriptSig = scriptSig;
    }
}
