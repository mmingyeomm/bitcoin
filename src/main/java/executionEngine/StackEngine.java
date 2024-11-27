package executionEngine;

import opCodeStack.OPCodeStack;

public class StackEngine {


    public static OPCodeStack executeOPCODE (String[] scriptPubKeyOpcodes, OPCodeStack inputStack) throws Exception {
        for (int i = 0; i < scriptPubKeyOpcodes.length; i++) {
            if (scriptPubKeyOpcodes[i].startsWith("OP_") ){
                String opcode = scriptPubKeyOpcodes[i].substring(3); // "OP_" 이후의 문자열 추출
                System.out.println("now" + inputStack);
                switch (opcode) {
                    case "DUP":
                        inputStack.op_dup();
                        System.out.println("Processing DUP operation");
                        break;
                    case "HASH160":
                        inputStack.op_hash160();
                        System.out.println("Processing HASH160 operation");
                        break;
                    case "EQUAL":
                        inputStack.op_equal();
                        System.out.println("Processing EQUAL operation");
                        break;
                    case "EQUALVERIFY":
                        inputStack.op_equalverify();
                        System.out.println("Processing EQUALVERIFY operation");
                        break;
                    case "CHECKMULTISIG":
                        System.out.println("Processing CHECKMULTISIG operation");

                        inputStack.op_checkmultisig();
                        break;
                    case "CHECKMULTISIGVERIFY":
                        inputStack.op_checkmultisigverify();
                        System.out.println("Processing CHECKMULTISIGVERIFY operation");
                        break;
                    case "CHECKSIG":
                        inputStack.op_checksig();
                        System.out.println("Processing CHECKSIG operation");
                        break;
                    case "CHECKSIGVERIFY":
                        inputStack.op_checksigverify();
                        System.out.println("Processing CHECKSIGVERIFY operation");
                        break;
                    case "IF":
                        inputStack.op_if();
                        System.out.println("Processing IF operation");
                        break;
                    case "ELSE":
                        inputStack.op_else();
                        System.out.println("Processing ELSE operation");
                        break;
                    case "ENDIF":
                        inputStack.op_endif();
                        System.out.println("Processing ENDIF operation");
                        break;





                    default:
                        System.out.println("Unknown operation: " + opcode);
                        break;
                }

            }
            else {
                inputStack.push(scriptPubKeyOpcodes[i]);
            }



        }
        return inputStack;

    }
}
