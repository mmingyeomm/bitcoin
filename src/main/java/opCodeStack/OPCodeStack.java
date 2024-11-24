package opCodeStack;

import java.util.Stack;


public class OPCodeStack extends Stack<String> {

    public OPCodeStack() {
        super();
    }

    public void executeCommands(){
        this.op_dup();
        this.op_hash();
        this.op_equalverify();

    }


    private void op_dup() {
        if (this.isEmpty()) {
            throw new IllegalStateException("OP_DUP: Stack is empty");
        }
        String top = this.peek();
        this.push(top);
    }

    private void op_hash(){

        if (this.isEmpty()){
            throw new IllegalStateException("OP_HASH: Stack is empty");
        }

        String top = this.pop();
        String hashedResult = CalculateHash160.calculateHash160(top);
        this.push(hashedResult);

        System.out.println("hashedResult" + hashedResult);
    }

    private boolean op_equalverify() {
        if (this.size() < 2) {
            throw new IllegalStateException("OP_EQUALVERIFY: Stack has fewer than 2 items");
        }
        String item1 = this.pop();
        String item2 = this.pop();

        return item1.equals(item2);
    }



}
