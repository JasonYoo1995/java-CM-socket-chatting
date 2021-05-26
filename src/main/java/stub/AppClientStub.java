package stub;

import core.Group;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMEventManager;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import core.EndToEndEncryption;

import java.security.*;
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
    keyPair = EndToEndEncryption.genRSAKeyPair();
    publicKey = keyPair.getPublic();
    privateKey = keyPair.getPrivate();
  }

  public void setPublicKeyMap(Map<String, PublicKey> paramMap) {
    this.publicKeyMap = paramMap;
  }

  public Map<String, PublicKey> getPublicKeyMap() {
    return this.publicKeyMap;
  }

  public void appendPublicKeyMap(Map<String, PublicKey> paramMap) {
    Map tmp = new HashMap(paramMap);
    tmp.keySet().removeAll(this.publicKeyMap.keySet());
    this.publicKeyMap.putAll(tmp);
  }

  public void publicKeyBroadcast(String chatRoomName) {

    CMInteractionInfo interInfo = getCMInfo().getInteractionInfo();
    String strDefServer = interInfo.getDefaultServerInfo().getServerName();

    String strPublicKey = EndToEndEncryption.getPublicKeyBroadcastMessage(getMyself().getName(), publicKey);

    StringBuffer sb = new StringBuffer("PUBLICKEYBROADCAST\n");
    sb.append(strPublicKey).append("\n");
    CMDummyEvent due = new CMDummyEvent();
    due.setDummyInfo(sb.toString());

    CMEventManager.broadcastEvent(due, getCMInfo());
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

}
