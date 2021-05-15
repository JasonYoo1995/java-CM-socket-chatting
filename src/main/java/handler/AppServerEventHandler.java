package handler;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
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
      case CMInfo.CM_INTEREST_EVENT:
        processInterestEvent(cme);
        break;
      case CMInfo.CM_DUMMY_EVENT:
        processDummyEvent(cme);
        break;
      default:
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
          stub.broadcastUserConnections();
          frame.addLogMessage("[" + se.getUserName() + "] logged in.");
        }
        break;
      case CMSessionEvent.LOGOUT:
        stub.broadcastUserConnections();
        frame.addLogMessage("[" + se.getUserName() + "] logged out.");
        break;
      case CMSessionEvent.JOIN_SESSION:
        frame.addLogMessage("["+se.getUserName()+"] requests to join session("+se.getSessionName()+").");
        break;
      default:
        break;
    }
  }

  private void processDummyEvent(CMEvent cme) {
    CMDummyEvent due = (CMDummyEvent) cme;
    String[] info = due.getDummyInfo().split("\n");
    String tag = info[0];
    if(tag.equals("CREATE&ENTERGROUP")){
      String[] tmp = info[1].split(" ");
      String groupName = tmp[0];
      String chatRoomName = tmp[1];
      stub.setGroupChatRoomName(groupName, chatRoomName);
      stub.broadcastGroupStatus();
    }
    else if(tag.equals("EXITGROUP")){
      String groupName = info[1];
      stub.emptyGroupChatRoomName(groupName); // userList.size==0이면 chatRoomName을 초기화
    }
  }

  private void processInterestEvent(CMEvent cme) {
    CMInterestEvent ie = (CMInterestEvent) cme;
    switch(ie.getID())
    {
      case CMInterestEvent.USER_ENTER:
        frame.addLogMessage("["+ie.getUserName()+"] enters group("+ie.getCurrentGroup()+") in session("
                +ie.getHandlerSession()+").");
        stub.updateGroupUserList();
        stub.broadcastGroupStatus();
        frame.addLogMessage(stub.getGroupListString());
        break;
      case CMInterestEvent.USER_LEAVE:
        frame.addLogMessage("["+ie.getUserName()+"] leaves group("+ie.getHandlerGroup()+") in session("
                +ie.getHandlerSession()+").");
        break;
      case CMInterestEvent.USER_TALK:
        frame.addLogMessage("("+ie.getHandlerSession()+", "+ie.getHandlerGroup()+")");
        frame.addLogMessage("<"+ie.getUserName()+">: "+ie.getTalk()+"");
        break;
      default:
        return;
    }
  }

}
