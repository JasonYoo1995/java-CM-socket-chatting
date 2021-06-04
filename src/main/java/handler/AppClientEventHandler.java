package handler;

import core.EndToEndEncryption;
import core.Group;
import core.UserConnection;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import stub.AppClientStub;
import views.AppClientFrame;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
      case CMInfo.CM_INTEREST_EVENT:
        processInterestEvent(cme);
      default:
        break;
    }
  }

  private void processDummyEvent(CMEvent cme) {
    CMDummyEvent due = (CMDummyEvent) cme;
    String[] info = due.getDummyInfo().split("\n");
    String tag = info[0];
    if (tag.equals("USERCONNECTION")) {
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
    } else if (tag.equals("GROUPSTATUS")) {
      List<Group> groupList = new ArrayList<>();
      for (int i = 1; i < info.length; i++) {
        String[] tmp = info[i].split(" ");
        groupList.add(new Group(tmp));
      }

      stub.groupList = groupList;

      // Refresh all client's public key map
      stub.sendPublicKeyToServer();

      // DEBUG log
      System.out.println("[DEBUG] Group Status in Client");
      StringBuffer sb = new StringBuffer();
      for (Group group : groupList) {
        System.out.println(group.toString());
      }

      frame.chatPanel.setChatRooms(groupList);
    } else if (tag.equals("PUBLICKEYBROADCAST")) {
      try {
        stub.appendPublicKeyMap(EndToEndEncryption.interpretPublicKeyBroadcastMessage(info[1]));
      } catch (InvalidKeySpecException e) {
        e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }

      // DEBUG log
      System.out.println("#####" + stub.getMyself().getName() + "'s publickeymap");
      stub.getPublicKeyMap().forEach((key, value) -> System.out.println("###" + key + ":" + value));
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
      case CMSessionEvent.SESSION_TALK:
        getChats(se);
      default:
        break;
    }
  }

  private void processInterestEvent(CMEvent cme) {
    CMInterestEvent ie = (CMInterestEvent) cme;
    switch (ie.getID()) {
      case CMInterestEvent.USER_TALK:
        getChats(ie);
        break;
      default:
        break;
    }
  }

  private void getChats(CMSessionEvent se) {
    //before decryption
    System.out.println("!!!!! BEFORE DECRYPTION !!!!!");
    String message = se.getUserName() + ":\n" + se.getTalk() + "\n";
    System.out.println("Get Session Chats >> " + message);

    //after decryption
    System.out.println("!!!!! AFTER DECRYPTION !!!!!");
    try {
      message = se.getUserName() + ":\n" + EndToEndEncryption.decryptRSA(se.getTalk(), stub.getPrivateKey()) + "\n";
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    System.out.println("Get Session Chats >> " + message);

    frame.chatPanel.getChatRoomFrame().addChatMessage(message);
    frame.chatPanel.getChatRoomFrame().addChatMessage(".");
  }

  private void getChats(CMInterestEvent ie) {
    //before decryption
    System.out.println("!!!!! BEFORE DECRYPTION !!!!!");
    String message = ie.getUserName() + ":\n" + ie.getTalk() + "\n";
    System.out.println("Get Interest Chats >> " + message);

    //after decryption
    System.out.println("!!!!! AFTER DECRYPTION !!!!!");
    try {
      message = ie.getUserName() + ":\n" + EndToEndEncryption.decryptRSA(ie.getTalk(), stub.getPrivateKey()) + "\n";
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    System.out.println("Get Interest Chats >> " + message);

    frame.chatPanel.getChatRoomFrame().addChatMessage(message);
    frame.chatPanel.getChatRoomFrame().addChatMessage(".");
  }

}
