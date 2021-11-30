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

public class BlowFish {
    static public String encryptCBC(String password, String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] passwordBytes = SHA256.hash(password);

        SecretKey key = new SecretKeySpec(passwordBytes, "BLOWFISH");

        byte[] nonce = new byte[8];
        System.arraycopy(passwordBytes, 0, nonce, 0, 8);

        Cipher cipher = Cipher.getInstance("BLOWFISH/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(nonce);

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Hex.from(cipherText);
    }

    static public String decryptCBC(String password, String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] passwordBytes = SHA256.hash(password);

        SecretKey key = new SecretKeySpec(passwordBytes, "BLOWFISH");

        byte[] nonce = new byte[8];
        System.arraycopy(passwordBytes, 0, nonce, 0, 8);

        Cipher cipher = Cipher.getInstance("BLOWFISH/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(nonce);

        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] plainBytes = cipher.doFinal(Hex.toBytes(cipherText));

        return new String(plainBytes, StandardCharsets.UTF_8);
    }
}
