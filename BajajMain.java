import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        String PRN = args[0];
        String JSONPath = args[1];

        try {
            String content = new String(Files.readAllBytes(Paths.get(JSONPath)));

            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuilder randomString = new StringBuilder(8);

            for (int i = 0; i < 8; i++) {
                randomString.append(characters.charAt(random.nextInt(characters.length())));
            }
        
            int keyPosition = content.indexOf("\"destination\"");

            int openQuotePosition = content.indexOf("\"", keyPosition + 13);
            int closeQuotePosition = content.indexOf("\"", openQuotePosition + 1);

            String value = content.substring(openQuotePosition + 1, closeQuotePosition);

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");

                md.update((PRN + value + randomString).getBytes());
                byte[] digestBytes = md.digest();

                StringBuilder hexString = new StringBuilder();
                for (byte b : digestBytes) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }

                System.out.println(hexString.toString() + ";" + randomString);
            } catch (NoSuchAlgorithmException  e) {
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } 
    }
}