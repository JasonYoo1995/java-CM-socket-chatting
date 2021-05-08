package app;

import callback.LoginCallback;
import handler.AppClientEventHandler;
import java.util.Vector;
import kr.ac.konkuk.ccslab.cm.entity.CMMember;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import stub.AppClientStub;
import views.AppClientFrame;

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
      }

      @Override
      public void onFailure() {
        System.exit(0);
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
