package opCodeStack;



public class VerifySignature {


    public static boolean verifySignature(String pubkey, String sig) throws Exception {
        System.out.println("Verifying signature...");
        System.out.println(ECDSA.Digest(pubkey));


        return (ECDSA.Digest(pubkey).equals(sig));

    }



}
