package handler;

import core.UserConnection;
import java.util.ArrayList;
import java.util.List;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
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
        break;
      case CMInfo.CM_DUMMY_EVENT:
        processDummyEvent(cme);
        break;
      default:
        break;
    }
  }

  private void processDummyEvent(CMEvent cme) {
    CMDummyEvent due = (CMDummyEvent) cme;
    System.out.println("---------------------------");
    System.out.println(due.getDummyInfo());
    System.out.println("---------------------------");

    String[] info = due.getDummyInfo().split("\n");
    if (info[0].equals("USERCONNECTION")) {
      String myUsername = stub.getMyself().getName();
      List<UserConnection> userConnections = new ArrayList<>();
      for (int i = 1; i < info.length; i++) {
        String[] tmp = info[i].split(" ");
        String username = tmp[0];
        boolean isConnected = Boolean.parseBoolean(tmp[1]);
        if (username.equals(myUsername)) {
          continue;
        }
        userConnections.add(new UserConnection(username, isConnected));
      }
      frame.updateOthersProfilePanel(userConnections.toArray(new UserConnection[0]));
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
