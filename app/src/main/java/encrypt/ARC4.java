package encrypt;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import convert.Hex;
import hasher.SHA256;

public class ARC4 {
    static public String encrypt(String password, String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] passwordBytes = SHA256.hash(password);

        SecretKey key = new SecretKeySpec(passwordBytes, "ARC4");

        byte[] nonce = new byte[8];
        System.arraycopy(passwordBytes, 0, nonce, 0, 8);

        Cipher cipher = Cipher.getInstance("ARC4/NONE/NOPADDING");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Hex.from(cipherText);
    }

    static public String decrypt(String password, String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] passwordBytes = SHA256.hash(password);

        SecretKey key = new SecretKeySpec(passwordBytes, "ARC4");

        byte[] nonce = new byte[8];
        System.arraycopy(passwordBytes, 0, nonce, 0, 8);

        Cipher cipher = Cipher.getInstance("ARC4/NONE/NOPADDING");

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainBytes = cipher.doFinal(Hex.toBytes(cipherText));

        return new String(plainBytes, StandardCharsets.UTF_8);
    }
}
