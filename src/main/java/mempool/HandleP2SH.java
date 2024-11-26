package mempool;

import opCodeStack.OPCodeStack;
import executionEngine.StackEngine;
import java.util.ArrayList;
import java.util.List;

public class HandleP2SH {
    public static void handleP2SH(OPCodeStack inputStack) throws Exception {
        // 리딤스크립트는 스택의 맨 마지막 요소
        String redeemScript = inputStack.pop();
        System.out.println("Processing P2SH transaction with redeemScript: " + redeemScript);

        // 서명들을 임시 저장
        List<String> signatures = new ArrayList<>();
        while (!inputStack.isEmpty()) {
            signatures.add(0, inputStack.pop()); // 역순으로 저장
        }

        // 스택 재구성
        // 1. 더미 0 추가 (CHECKMULTISIG 버그 해결용)
        inputStack.push("0");

        // 2. 서명들을 다시 스택에 푸시
        for (String sig : signatures) {
            inputStack.push(sig);
        }

        // 리딤스크립트를 OPCODE로 변환
        String[] opcodes = parseRedeemScript(redeemScript);

        // OPCODE 실행
        inputStack = StackEngine.executeOPCODE(opcodes, inputStack);

        System.out.println("P2SH script execution completed. Final stack: " + inputStack);

        // 검증 결과 확인
        if (inputStack.size() != 1 || !inputStack.peek().equals("true")) {
            throw new Exception("P2SH script validation failed");
        }
    }

    private static String[] parseRedeemScript(String script) throws Exception {
        List<String> opcodes = new ArrayList<>();

        // 첫 번째 OP_2 추가
        if (script.startsWith("52")) {
            opcodes.add("OP_2");
        }

        int pos = 2; // "52" 이후부터 시작

        while (pos < script.length() - 4) { // 마지막 "52ae" 제외
            if (script.substring(pos, pos + 2).equals("21")) {
                // 33바이트(66자) 공개키 추가
                opcodes.add(script.substring(pos + 2, pos + 68));
                pos += 68; // 21 + 66자
            } else {
                throw new Exception("Invalid redeem script format");
            }
        }

        // 마지막 OP_2와 OP_CHECKMULTISIG 추가
        if (script.substring(script.length() - 4).equals("52ae")) {
            opcodes.add("OP_2");
            opcodes.add("OP_CHECKMULTISIG");
        }

        return opcodes.toArray(new String[0]);
    }
}
