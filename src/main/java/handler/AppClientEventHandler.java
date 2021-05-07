package handler;

import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import stub.AppClientStub;
import views.AppClientFrame;

public class AppClientEventHandler implements CMAppEventHandler {

  private AppClientStub stub;
  private AppClientFrame frame;

  private AppClientEventHandler() {
  }

  public AppClientEventHandler(AppClientStub stub, AppClientFrame frame) {
    this.stub = stub;
    this.frame = frame;
  }

  @Override
  public void processEvent(CMEvent cme) {
    switch (cme.getType()) {
      case CMInfo.CM_SESSION_EVENT:
        processSessionEvent(cme);
      default:
        break;
    }
  }

  private void processSessionEvent(CMEvent cme) {
    CMSessionEvent se = (CMSessionEvent) cme;
    switch (se.getID()) {
      case CMSessionEvent.LOGIN_ACK:
        int isValidUser = se.isValidUser();
        if (isValidUser == 0) {
          System.err.println("This client fails authentication by server.");
          System.exit(0);
        } else if (isValidUser == -1) {
          System.err.println("This client is already in the login-user list.");
          System.exit(0);
        } else {
          System.out.println("This client successfully logs in to the server.");
        }
        break;
      default:
        break;
    }
  }

}
