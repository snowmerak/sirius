package hasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    static public byte[] hash(String source) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(source.getBytes(StandardCharsets.UTF_8));
    }
}
