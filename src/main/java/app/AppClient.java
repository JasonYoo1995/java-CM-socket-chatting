package app;

import callback.*;
import core.Group;
import handler.AppClientEventHandler;
import stub.AppClientStub;
import views.AppClientFrame;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AppClient {

  private final AppClientFrame appFrame;
  private final AppClientStub stub;

  public AppClient() throws NoSuchAlgorithmException {
    appFrame = new AppClientFrame();
    stub = new AppClientStub();
    AppClientEventHandler clientEventHandler = new AppClientEventHandler(stub, appFrame);
    stub.setAppEventHandler(clientEventHandler);
  }

  public static void main(String[] args) throws NoSuchAlgorithmException {
    AppClient client = new AppClient();
    client.appFrame.loginCallback = new LoginCallback() {
      @Override
      public void onSuccess(String username, String password) {
        client.startCM();
        client.appFrame.init();
        boolean result = client.stub.loginCM(username, password);
        if (!result) {
          System.exit(0);
        }

        client.appFrame.updateUserProfile(username);

        // server에게 요청
      }

      @Override
      public void onFailure() {
        System.exit(0);
      }
    };

    client.appFrame.chatPanel.groupCallback = chatRoomName -> {
      // 채팅방 이름 중복 검사
      final List<Group> groupList = client.stub.groupList;
      for (Group group : groupList) {
        if (group.chatRoomName.equals(chatRoomName)) {
          System.out.println("채팅방 이름 중복");
          return;
        }
      }

      client.stub.createAndEnterChatRoom(chatRoomName);
      client.appFrame.chatPanel.enterChatRoom(chatRoomName);
    };

    client.appFrame.chatPanel.exitCallback = client.stub::exitChatRoom;

    client.appFrame.chatPanel.enterCallback = client.stub::selectAndEnterChatRoom;

    client.appFrame.chatPanel.stubCallback = new StubCallback() {
      @Override
      public AppClientStub getStub() {
        return client.stub;
      }
    };

    // client.appFrame.chatPanel.

    client.appFrame.showLoginModal();
  }

  private void startCM() {
    boolean isStarted = stub.startCM();
    if (!isStarted) {
      System.exit(0);
    }
  }
}
