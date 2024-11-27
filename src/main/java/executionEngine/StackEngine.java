package executionEngine;

import opCodeStack.OPCodeStack;

public class StackEngine {


    public static OPCodeStack executeOPCODE (String[] scriptPubKeyOpcodes, OPCodeStack inputStack) throws Exception {



        boolean if_true = true;
        boolean skip= false;

        for (int i = 0; i < scriptPubKeyOpcodes.length; i++) {
            System.out.println(inputStack);

            if(skip && !scriptPubKeyOpcodes[i].startsWith("OP_ENDIF") && !scriptPubKeyOpcodes[i].startsWith("OP_ELSE")){
                System.out.println("Skipping operation: " + scriptPubKeyOpcodes[i]);
            }

            else if (scriptPubKeyOpcodes[i].startsWith("OP_")  ) {

                String opcode = scriptPubKeyOpcodes[i].substring(3); // "OP_" 이후의 문자열 추출

                switch (opcode) {
                    case "DUP":
                        System.out.println("Processing DUP operation");
                        inputStack.op_dup();
                        break;
                    case "HASH160":
                        System.out.println("Processing HASH160 operation");
                        inputStack.op_hash160();
                        break;
                    case "EQUAL":
                        System.out.println("Processing EQUAL operation");
                        inputStack.op_equal();
                        break;
                    case "EQUALVERIFY":
                        System.out.println("Processing EQUALVERIFY operation");
                        inputStack.op_equalverify();
                        break;
                    case "CHECKMULTISIG":
                        System.out.println("Processing CHECKMULTISIG operation");
                        inputStack.op_checkmultisig();
                        break;
                    case "CHECKMULTISIGVERIFY":
                        System.out.println("Processing CHECKMULTISIGVERIFY operation");
                        inputStack.op_checkmultisigverify();
                        break;
                    case "CHECKSIG":
                        System.out.println("Processing CHECKSIG operation");
                        inputStack.op_checksig();
                        break;
                    case "CHECKSIGVERIFY":
                        System.out.println("Processing CHECKSIGVERIFY operation");
                        inputStack.op_checksigverify();
                        break;

                    case "IF":

                        System.out.println("Processing IF operation");
                        if_true = inputStack.op_if();
                        if (!if_true){
                            skip = true;
                        }
                        break;
                    case "ELSE":
                        if (if_true){
                            System.out.println("ELSE WHEN TRUE");
                            skip = true;
                        } else {
                            System.out.println("ELSE WHEN FALSE");
                            skip = false;
                        }
                        System.out.println("Processing ELSE operation");

                        break;
                    case "ENDIF":
                        System.out.println("Processing ENDIF operation");
                        break;


                    default:
                        System.out.println("Unknown operation: " + opcode);
                        break;
                }

            }
            else {
                if(skip) { System.out.println("Skipping operation"); } else{
                    inputStack.push(scriptPubKeyOpcodes[i]);
                }
            }



        }
        return inputStack;

    }
}
