package opCodeStack;



public class VerifySignature {


    public static boolean verifySignature(String pubkey, String sig) throws Exception {

        return (ECDSA.Digest(pubkey).equals(sig));

    }



}
