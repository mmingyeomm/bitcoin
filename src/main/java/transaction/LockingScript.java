package transaction;

import java.util.List;

public class LockingScript {
    private List<String> scriptPubKey;

    public LockingScript(List<String> scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public List<String> getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(List<String> scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }
}
