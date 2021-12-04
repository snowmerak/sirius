package dh;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.KeyAgreement;

import convert.Hex;

public class Ec {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Ec() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        ECGenParameterSpec ecParamSpec = new ECGenParameterSpec("secp521r1");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(ecParamSpec);

        KeyPair keyPair = kpg.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public String share(String other) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        byte[] otherPublicKey = Hex.toBytes(other);

        KeyFactory kf = KeyFactory.getInstance("EC");

        X509EncodedKeySpec x509ks = new X509EncodedKeySpec(otherPublicKey);
        PublicKey pubKey = kf.generatePublic(x509ks);

        KeyAgreement aKA = KeyAgreement.getInstance("ECDH");
        aKA.init(privateKey);
        aKA.doPhase(pubKey, true);

        return Hex.from(aKA.generateSecret());
    }

    public String getPublicKey() {
        return Hex.from(publicKey.getEncoded());
    }

    public void setPublicKey(String encodedHex) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Hex.toBytes(encodedHex);

        KeyFactory kf = KeyFactory.getInstance("EC");

        X509EncodedKeySpec x509ks = new X509EncodedKeySpec(encoded);
        publicKey = kf.generatePublic(x509ks);
    }

    public String getPrivateKey() {
        return Hex.from(privateKey.getEncoded());
    }

    public void setPrivateKey(String encodedHex) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Hex.toBytes(encodedHex);

        KeyFactory kf = KeyFactory.getInstance("EC");

        PKCS8EncodedKeySpec x509ks = new PKCS8EncodedKeySpec(encoded);

        privateKey = kf.generatePrivate(x509ks);
    }
}
