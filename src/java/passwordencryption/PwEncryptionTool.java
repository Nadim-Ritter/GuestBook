package passwordencryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PwEncryptionTool {

    static String password = "";
    static String choice = "";
    static BufferedReader br;

    /**
     * @param args
     * @author Avdyl Haxhaj Version: 1.0.0 encrypt/decrypt method source: Remo
     * Raeber
     */

    public static void main(String[] args) {
        while (!choice.equals("e")) {
            System.out.print("\n\n\\*********************\\"
                    + " \\* PW Encryption Tool*\\"
                    + " \\*********************\\" + "\n"
                    + "1. Encrypt Password\n" + "2. Decrypt Password\n"
                    + "e. Exit\n\n\n" + "Choice: ");

            choice = ReadLine();

            if (choice.equals("e")) {
                System.out.println("\n\nShut down ... ");
                System.out.print("... done");
            } else {
                System.out.print("\nEnter the password: ");
                password = ReadLine();

                if (choice.equals("1")) {
                    try {
                        System.out.print(
                                "\nEncrypted Password: " + encrypt(password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.print(
                                "\nDecrypted Password: " + decrypt(password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
	 * Uncomment and use this code for encryption
     */
    public static String encrypt(String crypt) throws Exception {
        byte[] password = "big38mac".getBytes("Cp850");
        Key key = new SecretKeySpec(password, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte crypted[] = cipher.doFinal(crypt.getBytes());

        return Base64.getEncoder().encodeToString(crypted);
    }

    public static String ReadLine() {
        String input = "";
        // open up standard input
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        // read the username from the command-line; need to use try/catch with
        // the
        // readLine() method
        try {
            input = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read your name!");
            System.exit(1);
        }

        return input;
    }

    public static String decrypt(String crypted) throws Exception {
        byte[] password = "big38mac".getBytes();
        Key key = new SecretKeySpec(password, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte decrypted[] = cipher.doFinal(Base64.getDecoder().decode(crypted));

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < decrypted.length; i++) {
            stringBuffer.append((char) decrypted[i]);
        }

        return stringBuffer.toString();
    }
}
