package encrypt;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import convert.Hex;
import hasher.SHA256;

public class AES {
    static protected String encrypt(String password, String plainText, String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] passwordBytes = SHA256.hash(password);

        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);

        SecretKey key = new SecretKeySpec(passwordBytes, "AES");
        byte[] iv = new byte[16];
        System.arraycopy(passwordBytes, 0, iv, 0, 16);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] ciphertext = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Hex.from(ciphertext);
    }

    static protected String decrypt(String password, String cipherText, String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] passwordBytes = SHA256.hash(password);

        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);

        SecretKey key = new SecretKeySpec(passwordBytes, "AES");
        byte[] iv = new byte[16];
        System.arraycopy(passwordBytes, 0, iv, 0, 16);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] decodedText = Hex.toBytes(cipherText);
        byte[] plainText = cipher.doFinal(decodedText);

        return new String(plainText, StandardCharsets.UTF_8);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public String encryptCBC(String password, String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return encrypt(password, plainText, "AES/CBC/PKCS5PADDING");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public String decryptCBC(String password, String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return decrypt(password, cipherText, "AES/CBC/PKCS5PADDING");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public String encryptGCM(String password, String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return encrypt(password, plainText, "AES/GCM/NOPADDING");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public String decryptGCM(String password, String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return decrypt(password, cipherText, "AES/GCM/NOPADDING");
    }
}
