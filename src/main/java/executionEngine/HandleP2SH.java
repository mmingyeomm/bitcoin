package executionEngine;

import opCodeStack.OPCodeStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandleP2SH {

    private static final Map<String, String> OPCODE_MAP = new HashMap<String, String>() {{
        // OP_CODE
        put("76", "OP_DUP");
        put("a9", "OP_HASH160");
        put("87", "OP_EQUAL");
        put("88", "OP_EQUALVERIFY");
        put("ac", "OP_CHECKSIG");
        put("ad", "OP_CHECKSIGVERIFY");
        put("ae", "OP_CHECKMULTISIG");
        put("af", "OP_CHECKMULTISIGVERIFY");
        put("63", "OP_IF");
        put("67", "OP_ELSE");
        put("68", "OP_ENDIF");
        // 숫자
        put("51", "1");
        put("52", "2");
        put("53", "3");
        put("54", "4");
        put("55", "5");
    }};

    public static OPCodeStack handleP2SH(String[] scriptPubKeyOpcodes,OPCodeStack inputStack) throws Exception {

        String redeemScript = inputStack.peek();
        String[] opcodes = parseRedeemScript(redeemScript);

        for (int i = 0; i< scriptPubKeyOpcodes.length; i++) {
            System.out.println("p2sh scriptpubkeyopcodes : " + scriptPubKeyOpcodes[i]);
        }

        StackEngine.executeOPCODE(scriptPubKeyOpcodes, inputStack);
        // hash redeem script to validate
        if (inputStack.pop().equals("true") ) {
            System.out.println("RedeemScript Validated");
            System.out.println(inputStack);
        }

        // Store the opcodes in the stack
        StackEngine.executeOPCODE(opcodes,inputStack);
        return inputStack;
    }


    private static String[] parseRedeemScript(String redeemScript) {

        ArrayList<String> opcodes = new ArrayList<>();
        int index = 0;

        while (index < redeemScript.length()) {
            String currentByte = redeemScript.substring(index, Math.min(index + 2, redeemScript.length()));
            index += 2;

            int value = Integer.parseInt(currentByte, 16);
            if (value > 0x00 && value <= 0x4b) {
                String data = redeemScript.substring(index, index + (value * 2));
                opcodes.add(data);
                index += (value * 2);
            } else {
                String opcode = OPCODE_MAP.getOrDefault(currentByte, currentByte);
                opcodes.add(opcode);
            }
        }
        return opcodes.toArray(new String[0]);
    }
}
