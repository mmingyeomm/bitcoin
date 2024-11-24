package utxo;

public class ScriptPubKey {
    private String asm;
    private String hex;
    private String type;

    // Getters and Setters
    public String getAsm() {
        return asm;
    }

    public void setAsm(String asm) {
        this.asm = asm;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ScriptPubKey{" +
                "asm='" + asm + '\'' +
                ", hex='" + hex + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
