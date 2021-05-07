package handler;

import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import stub.AppServerStub;
import views.AppServerFrame;

public class AppServerEventHandler implements CMAppEventHandler {

  private AppServerStub stub;
  private AppServerFrame frame;

  private AppServerEventHandler() {
  }

  public AppServerEventHandler(AppServerStub stub, AppServerFrame frame) {
    this.stub = stub;
    this.frame = frame;
  }

  @Override
  public void processEvent(CMEvent cme) {
    switch (cme.getType()) {
      case CMInfo.CM_SESSION_EVENT:
        processSessionEvent(cme);
        break;
    }
  }

  private void processSessionEvent(CMEvent cme) {
    CMSessionEvent se = (CMSessionEvent) cme;
    CMInfo cmInfo = stub.getCMInfo();
    CMConfigurationInfo configurationInfo = cmInfo.getConfigurationInfo();

    switch (se.getID()) {
      case CMSessionEvent.LOGIN:
        if (configurationInfo.isLoginScheme()) {
          boolean isAuthenticated = CMDBManager
              .authenticateUser(se.getUserName(), se.getPassword(), cmInfo);
          int returnCode = isAuthenticated ? 1 : 0;
          stub.replyEvent(cme, returnCode);
        }
        break;
      default:
        break;
    }
  }

}
