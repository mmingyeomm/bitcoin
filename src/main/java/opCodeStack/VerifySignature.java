package opCodeStack;



public class VerifySignature {


    public static boolean verifySignature(String pubkey, String sig) throws Exception {

        System.out.println("verifysig / " + ECDSA.Digest(pubkey) + " / "+ sig );
        return (ECDSA.Digest(pubkey).equals(sig));

    }



}
