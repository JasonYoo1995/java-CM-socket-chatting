package app;

import callback.ExitCallback;
import callback.GroupCallback;
import callback.LoginCallback;
import callback.EnterCallback;
import core.Group;
import handler.AppClientEventHandler;
import stub.AppClientStub;
import views.AppClientFrame;

import java.util.List;

public class AppClient {

  private AppClientFrame appFrame;
  private AppClientStub stub;
  private AppClientEventHandler clientEventHandler;

  public AppClient() {
    appFrame = new AppClientFrame();
    stub = new AppClientStub();
    clientEventHandler = new AppClientEventHandler(stub, appFrame);
    stub.setAppEventHandler(clientEventHandler);
  }

  public static void main(String[] args) {
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

    client.appFrame.chatPanel.groupCallback = new GroupCallback() {
      @Override
      public void onSuccess(String chatRoomName) {
        // 채팅방 이름 중복 검사
        final List<Group> groupList = client.stub.groupList;
        for (Group group : groupList){
          if(group.chatRoomName.equals(chatRoomName)){
            System.out.println("채팅방 이름 중복");
            return;
          }
        }

        client.stub.createAndEnterChatRoom(chatRoomName);
      }
    };

    client.appFrame.chatPanel.exitCallback = new ExitCallback() {
      @Override
      public void exit() {
        client.stub.exitChatRoom();
      }
    };

    client.stub.enterCallback = new EnterCallback() {
      @Override
      public void enter(String chatRoomName) {
        client.appFrame.chatPanel.enterChatRoom(chatRoomName);
      }
    };

    client.appFrame.showLoginModal();
  }

  private void startCM() {
    boolean isStarted = stub.startCM();
    if (!isStarted) {
      System.exit(0);
    }
  }
}
