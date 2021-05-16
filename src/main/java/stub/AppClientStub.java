package stub;

import core.Group;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMEventManager;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class AppClientStub extends CMClientStub {

  public List<Group> groupList = new ArrayList<>();
  public static final int totalGroupCount = 5; // cm-session1.conf에 선언되어 있는 group의 개수

  private KeyPair keyPair; // PrivateKey + PublicKey
  private PublicKey publicKey;
  private PrivateKey privateKey;

  private Map<String, PublicKey> publicKeyMap = new HashMap<String, PublicKey>(); // Other Client's publicKeyMap for encryption

  public AppClientStub() throws NoSuchAlgorithmException {
    super();
    keyPair = genRSAKeyPair();
    publicKey = keyPair.getPublic();
    privateKey = keyPair.getPrivate();
  }

  public void createAndEnterChatRoom(String chatRoomName) {
    if (groupList.size() == totalGroupCount - 1) { // 모든 채팅방이 사용 중이라면
      System.out.println("더 이상 채팅방을 생성할 수 없습니다.");
      return;
    } // 채팅방 생성이 가능한 경우

    List<String> nameList = new ArrayList<>();
    for (int i = 2; i <= totalGroupCount; i++) {
      nameList.add("g" + i);
    }

    // 빈 group들 중 index가 가장 작은 group를 찾는 알고리즘
    for (Group group : groupList) {
      nameList.remove(group.groupName); // 1명 이상 있는 채팅방은 제외
    }

    String groupName = nameList.get(0); // 찾아낸 group의 이름 (g*)
    System.out.println("[DEBUG] groupName " + groupName);

    CMInteractionInfo interInfo = getCMInfo().getInteractionInfo();
    String strDefServer = interInfo.getDefaultServerInfo().getServerName();

    StringBuffer sb = new StringBuffer("CREATE&ENTERGROUP\n");
    sb.append(groupName).append(" ").append(chatRoomName).append("\n");
    CMDummyEvent due = new CMDummyEvent();
    due.setDummyInfo(sb.toString());

    CMEventManager.unicastEvent(due, strDefServer, getCMInfo()); // Dummy Event 전달

    changeGroup(groupName); // Interest Event 전달
  }

  public void selectAndEnterChatRoom(String chatRoomName) {
    String groupName = "";
    for (Group group : groupList) {
      if (group.chatRoomName.equals(chatRoomName)) {
        groupName = group.groupName;
        break;
      }
    }
    changeGroup(groupName); // Interest Event 전달
  }

  public void exitChatRoom() {
    changeGroup("g1"); // Interest Event 전달

    CMInteractionInfo interInfo = getCMInfo().getInteractionInfo();
    String strDefServer = interInfo.getDefaultServerInfo().getServerName();

    StringBuffer sb = new StringBuffer("EXITGROUP\n");
    sb.append(getMyself().getCurrentGroup()).append("\n");
    CMDummyEvent due = new CMDummyEvent();
    due.setDummyInfo(sb.toString());
    CMEventManager.unicastEvent(due, strDefServer, getCMInfo()); // Dummy Event 전달
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
