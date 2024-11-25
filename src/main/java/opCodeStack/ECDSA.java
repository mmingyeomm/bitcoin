package opCodeStack;

import java.security.MessageDigest;
import org.bouncycastle.util.encoders.Hex;

public class ECDSA {

    public static String Digest(String plain) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(plain.getBytes());
        byte[] b = md.digest();
        return Hex.toHexString(b);
    }
}
