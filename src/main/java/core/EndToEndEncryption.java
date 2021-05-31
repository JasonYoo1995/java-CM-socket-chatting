package core;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

//util class needs to be 'final' and its methods need to be 'static'

public final class EndToEndEncryption {

    // 보낼 브로드캐스트 메세지를 전달받은 userName과 publickey를 더해 생성합니다.
    public static String getPublicKeyBroadcastMessage(String userName, PublicKey publicKey) {
        return userName + ' ' + publicKey2Base64String(publicKey);
    }

    // 브로드캐스트 메세지를 사용자 이름과 publickey 를 포함한 MAP 자료형으로 반환합니다.
    public static Map interpretPublicKeyBroadcastMessage(String message) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] strarr = message.split(" ");
        Map<String, PublicKey> publicKeyMap = new HashMap<String, PublicKey>();
        publicKeyMap.put(strarr[0], getPublicKeyFromBase64String(strarr[1]));
        return publicKeyMap;
    }

    // 1024비트 RSA 키쌍을 생성합니다.
    public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator gen;
        gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(1024, secureRandom);
        KeyPair keyPair = gen.genKeyPair();
        return keyPair;
    }

    // Public Key로 RSA 암호화를 수행합니다.
    public static String encryptRSA(String plainText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
        return encrypted;
    }

    // Private Key로 RAS 복호화를 수행합니다.
    public static String decryptRSA(String encrypted, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");
        return decrypted;
    }

    // 공개키를 Base64 인코딩한 문자일을 만듭니다. (문자열로 만든 후 broadcast 실)시
    public static String publicKey2Base64String(PublicKey publicKey) {
        byte[] bytePublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(bytePublicKey);
        return base64PublicKey;
    }

    // 개인키를 Base64 인코딩한 문자일을 만듭니다. (하지만 사용할 일 X)
    public static String privateKey2Base64String(PrivateKey privateKey) {
        byte[] bytePrivateKey = privateKey.getEncoded();
        String base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey);
        return base64PrivateKey;
    }

    // Base64 엔코딩된 개인키 문자열로부터 PrivateKey를 얻는다. (하지만 사용할 일 X)
    public static PrivateKey getPrivateKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String privateKeyString = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        return keyFactory.generatePrivate(keySpecPKCS8);
    }

    // Base64 엔코딩된 공용키 문자열로부터 PublicKey를 얻는다. (broadcast 수신 후 publicKey 얻은 후 publicKeyMap에 저장)
    public static PublicKey getPublicKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String publicKeyString = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        return keyFactory.generatePublic(keySpecX509);
    }

}
