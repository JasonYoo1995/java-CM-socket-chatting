package app;

import callback.IPPortCallback;
import handler.AppServerEventHandler;
import stub.AppServerStub;
import views.AppServerFrame;

public class AppServer {

  private final AppServerFrame appFrame;
  private final AppServerStub stub;

  public AppServer() {
    appFrame = new AppServerFrame();
    stub = new AppServerStub();
    AppServerEventHandler serverEventHandler = new AppServerEventHandler(stub, appFrame);
    stub.setAppEventHandler(serverEventHandler);
  }

  public static void main(String[] args) {
    AppServer server = new AppServer();
    server.appFrame.ipPortCallback = new IPPortCallback() {
      @Override
      public void onSuccess() {
        boolean isStarted = server.stub.startCM();
        server.appFrame.init();
        if (!isStarted) {
          server.appFrame.addLogMessage("Server CM initialization failed.");
          System.exit(0);
        } else {
          server.appFrame.addLogMessage("Server CM starts.");
        }
      }

      @Override
      public void onFailure() {
        System.exit(0);
      }
    };

    String ip = server.stub.getServerAddress();
    int port = server.stub.getServerPort();
    server.appFrame.showIPAndPortModal(ip, port);
    server.stub.initiateGroupUserList();
  }

}
