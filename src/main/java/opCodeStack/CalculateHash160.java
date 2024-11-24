package opCodeStack;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

public class CalculateHash160 {

    public static String calculateHash160(String input) {
        try {
            // Step 1: Calculate SHA256
            byte[] sha256Hash = calculateSHA256(input.getBytes());

            // Step 2: Calculate RIPEMD160 of the SHA256 result
            byte[] ripemd160Hash = calculateRIPEMD160(sha256Hash);

            // Step 3: Convert to hex string
            return bytesToHex(ripemd160Hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating Hash160: " + e.getMessage());
        }
    }

    private static byte[] calculateSHA256(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    private static byte[] calculateRIPEMD160(byte[] input) {
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(input, 0, input.length);
        byte[] out = new byte[20]; // RIPEMD160 produces 20 bytes
        digest.doFinal(out, 0);
        return out;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
