package mempool;

import executionEngine.StackEngine;
import opCodeStack.OPCodeStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandleP2SH {
    // Define opcode mapping
    private static final Map<String, String> OPCODE_MAP = new HashMap<String, String>() {{
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
        // Number opcodes
        put("51", "1");
        put("52", "2");
        put("53", "3");
        put("54", "4");
        put("55", "5");
    }};

    public static OPCodeStack handleP2SH(OPCodeStack inputStack) throws Exception {
        String redeemScript = inputStack.pop();
        String[] opcodes = parseRedeemScript(redeemScript);

        // Store the opcodes in the stack
        StackEngine.executeOPCODE(opcodes,inputStack);
        return inputStack;
    }

    private static String[] parseRedeemScript(String redeemScript) {
        System.out.println("parsing redeem script: " + redeemScript);
        ArrayList<String> opcodes = new ArrayList<>();
        int index = 0;

        while (index < redeemScript.length()) {
            // Read two characters (one byte)
            String currentByte = redeemScript.substring(index, Math.min(index + 2, redeemScript.length()));
            index += 2;

            // Check if it's a data push operation (0x01-0x4b)
            int value = Integer.parseInt(currentByte, 16);
            if (value > 0x00 && value <= 0x4b) {
                // It's a push data operation, get the data
                String data = redeemScript.substring(index, index + (value * 2));
                opcodes.add(data);
                index += (value * 2);
            } else {
                // It's an opcode
                String opcode = OPCODE_MAP.getOrDefault(currentByte, currentByte);
                opcodes.add(opcode);
            }
        }
        System.out.println(opcodes);
        return opcodes.toArray(new String[0]);
    }
}
