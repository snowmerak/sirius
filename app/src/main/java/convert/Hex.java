package convert;

public class Hex {
    static public String from(byte[] a) {
        return new java.math.BigInteger(a).toString(16);
    }

    static public byte[] toBytes(String a) {
        return new java.math.BigInteger(a, 16).toByteArray();
    }
}
