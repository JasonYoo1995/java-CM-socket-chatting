package stub;

import core.Group;
import core.UserConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class AppServerStub extends CMServerStub {

  public List<Group> groupList = new ArrayList<>();

  public AppServerStub() {
    super();
  }

  public void broadcastGroupStatus() {
    StringBuffer sb = new StringBuffer("GROUPSTATUS\n");
    for (Group group : groupList) {
      if (group.groupName.equals("g1")) {
        continue; // g1은 대기실이므로 보내지 않음
      }
      if (group.userList.size() == 0) {
        continue; // 빈 채팅방은 보내지 않음
      }
      if (group.chatRoomName.equals("빈_채팅방")) {
        continue; // 빈 채팅방은 보내지 않음
      }
      System.out.println(group);
      sb.append(group.groupName).append(" ")
          .append(group.chatRoomName).append(" ")
          .append(group.channelInfo, 0, group.channelInfo.length() - 1); // substring은 '\n'를 제거하기 위함
      for (CMUser user : group.userList) {
        sb.append(" ").append(user.getName());
      }
      sb.append("\n");
    }

    CMDummyEvent due = new CMDummyEvent();
    due.setDummyInfo(sb.toString());
    System.out
        .println("[DEBUG] AppServerStub.java - broadcastGroupStatus() - dummy event - GROUPSTATUS");
    System.out.println(sb);
    broadcast(due);
  }

  public void broadcastUserConnections() {
    List<UserConnection> userConnections = getUserConnections();

    StringBuffer sb = new StringBuffer("USERCONNECTION\n");

    for (UserConnection userConnection : userConnections) {
      sb.append(userConnection.username).append(" ").append(userConnection.isConnected)
          .append("\n");
    }

    CMDummyEvent due = new CMDummyEvent();
    due.setDummyInfo(sb.toString());
    broadcast(due);
  }

  private List<UserConnection> getUserConnections() {
    HashSet<UserConnection> userConnections = new HashSet<>();
    ResultSet rs = CMDBManager.queryGetUsers(0, -1, getCMInfo());

    try {
      while (rs.next()) {
        String username = rs.getString(2);
        userConnections.add(new UserConnection(username));
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }

    Stream<CMUser> loggedInUsers = getLoginUsers().getAllMembers().stream();
    List<String> strLoggedInUsers = loggedInUsers.map(CMUser::getName).collect(Collectors.toList());

    for (String loggedInUser : strLoggedInUsers) {
      System.out.println("########## " + loggedInUser);
      UserConnection userConnection = new UserConnection(loggedInUser, true);
      userConnections.remove(userConnection);
      userConnections.add(userConnection);
    }

    return new ArrayList<>(userConnections);
  }

  public void initiateGroupUserList() {
    for (CMGroup group : this.getCMInfo().getInteractionInfo().getSessionList()
        .iterator().next().getGroupList()) {
      groupList.add(new Group(group));
    }
  }

  public void updateGroupUserList() {
    Iterator<CMGroup> groupIterator = this.getCMInfo().getInteractionInfo().getSessionList()
        .iterator().next().getGroupList().iterator();
    int i = 0;
    while (groupIterator.hasNext()) {
      CMGroup group = groupIterator.next();
      List<CMUser> userList = new ArrayList<>(group.getGroupUsers().getAllMembers());
      groupList.get(i).userList = userList;
      if (userList.size() == 0) {
        groupList.get(i).chatRoomName = "빈_채팅방";
      }
      i++;
      System.out.println("[DEBUG] userList " + userList);
    }
  }

  public void setGroupChatRoomName(String groupName, String chatRoomName) {
    for (Group group : groupList) {
      if (group.groupName.equals(groupName)) {
        group.chatRoomName = chatRoomName;
      }
    }
  }

  public void emptyGroupChatRoomName(String groupName) {
    for (Group group : groupList) {
      if (group.groupName.equals(groupName)) {
        if (group.userList.size() == 0) {
          group.chatRoomName = "빈_채팅방";
        }
      }
    }
  }

  public String getGroupListString() {
    StringBuffer sb = new StringBuffer();
    for (Group group : this.groupList) {
      sb.append(group.toString()).append("\n");
    }
    return sb.toString();
  }
}
