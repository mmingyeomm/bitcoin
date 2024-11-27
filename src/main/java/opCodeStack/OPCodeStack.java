package opCodeStack;

import java.util.Stack;


public class OPCodeStack extends Stack<String> {

    private boolean ifCondition = false;
    private boolean inElseBlock = false;
    private Stack<Boolean> ifStack = new Stack<>();

    public OPCodeStack() {
        super();
    }

    public void op_dup() {
        if (this.isEmpty()) {
            throw new IllegalStateException("OP_DUP: Stack is empty");
        }
        String top = this.peek();
        this.push(top);
    }

    public void op_hash160(){

        if (this.isEmpty()){
            throw new IllegalStateException("OP_HASH: Stack is empty");
        }

        String top = this.pop();
        String hashedResult = Hash160.calculateHash160(top);
        this.push(hashedResult);

        System.out.println("hashedResult" + hashedResult);
    }

    public void op_equal(){

        if (this.size() < 2) {
            throw new IllegalStateException("OP_EQUAL: Stack has fewer than 2 items");
        }

        String item1 = this.pop();
        String item2 = this.pop();

        if (item1.equals(item2)) {
            this.push("true");
        } else {
            this.push("false");
        }


    }

    public boolean op_equalverify() {
        if (this.size() < 2) {
            throw new IllegalStateException("OP_EQUALVERIFY: Stack has fewer than 2 items");
        }
        String item1 = this.pop();
        String item2 = this.pop();

        return item1.equals(item2);
    }

    public void op_checksig() throws Exception {
        if (this.size() < 2) {
            throw new IllegalStateException("OP_CHECKSIG: Stack has fewer than 2 items");
        }

        String pubkey = this.pop();
        String signature = this.pop();

        boolean checkSigResult = VerifySignature.verifySignature(pubkey, signature);

        if (checkSigResult) {
            this.push("true");
        } else {
            this.push("false");
        }

    }

    public boolean op_checksigverify() throws Exception {
        String pubkey = this.pop();
        String signature = this.pop();

        return  VerifySignature.verifySignature(pubkey, signature);
    }

    public void op_checkmultisig() throws Exception {
        System.out.println("chekcing multisig...");

        if (this.size() < 1) {
            throw new IllegalStateException("OP_CHECKMULTISIG: Stack is empty");
        }

        int total_pubkey_count = Integer.parseInt(this.pop());
        String[] pubKeys = new String[total_pubkey_count];

        for (int i = 0; i < total_pubkey_count; i++) {
            pubKeys[total_pubkey_count - 1 - i] = this.pop();
        }

        int required_signature_count = Integer.parseInt(this.pop());

        String[] input_signatures  = new String[required_signature_count];
        for (int i = 0; i < required_signature_count; i++) {
            input_signatures[required_signature_count-1 -i] = this.pop();
        }


        int countValidSignatures = 0;
        for (int i = 0; i < input_signatures.length; i++) {

            for (int j = i + 1; j < pubKeys.length; j++) {

                if (VerifySignature.verifySignature(pubKeys[j], input_signatures[i])) {
                    countValidSignatures++;
                    break;
                }
            }
        }
        System.out.println(countValidSignatures);
        System.out.println(required_signature_count);
        if (countValidSignatures >= required_signature_count) {
            this.push("true");
        } else{
            this.push("false");
        }

    }

    public boolean op_checkmultisigverify() throws Exception {
        System.out.println("chekcing multisig...");

        if (this.size() < 1) {
            throw new IllegalStateException("OP_CHECKMULTISIG: Stack is empty");
        }

        int total_pubkey_count = Integer.parseInt(this.pop());
        String[] pubKeys = new String[total_pubkey_count];

        for (int i = 0; i < total_pubkey_count; i++) {
            pubKeys[total_pubkey_count - 1 - i] = this.pop();
        }

        int required_signature_count = Integer.parseInt(this.pop());

        String[] input_signatures  = new String[required_signature_count];
        for (int i = 0; i < required_signature_count; i++) {
            input_signatures[required_signature_count-1 -i] = this.pop();
        }

        int countValidSignatures = 0;
        for (int i = 0; i < input_signatures.length; i++) {

            for (int j = i + 1; j < pubKeys.length; j++) {

                if (VerifySignature.verifySignature(pubKeys[j], input_signatures[i])) {
                    countValidSignatures++;
                    break;
                }
            }
        }

        return (countValidSignatures >= required_signature_count);

    }

    public void op_if() {
        if (this.isEmpty()) {
            throw new IllegalStateException("OP_IF: Stack is empty");
        }

        String top = this.pop();
        boolean condition = top.equals("true");

        ifCondition = condition;
        inElseBlock = false;
        ifStack.push(ifCondition);
    }

    public void op_else() {
        if (ifStack.isEmpty()) {
            throw new IllegalStateException("OP_ELSE: No matching IF");
        }

        inElseBlock = true;
        ifCondition = !ifStack.peek();
    }

    public void op_endif() {
        if (ifStack.isEmpty()) {
            throw new IllegalStateException("OP_ENDIF: No matching IF");
        }

        ifStack.pop();
        if (!ifStack.isEmpty()) {
            ifCondition = ifStack.peek();
        } else {
            ifCondition = false;
        }
        inElseBlock = false;
    }





}
