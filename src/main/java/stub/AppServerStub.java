package stub;

import com.github.weisj.darklaf.icons.IconUtil;
import core.Group;
import core.UserConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMMember;
import kr.ac.konkuk.ccslab.cm.entity.CMSession;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class AppServerStub extends CMServerStub {

  public AppServerStub() {
    super();
  }

  public void broadcastGroupStatus() {
    List<Group> groupList = this.getGroupList();

    StringBuffer sb = new StringBuffer("GROUPSTATUS\n");
    for(Group group : groupList){
      System.out.println(group.toString());
      sb.append(group.groupName).append(" ")
              .append(group.channelInfo.substring(0,group.channelInfo.length()-1)); // substring은 '\n'를 제거하기 위함
      for(CMUser user : group.userList){
        sb.append(" ").append(user.getName());
      }
      sb.append("\n");
    }

    CMDummyEvent due = new CMDummyEvent();
    due.setDummyInfo(sb.toString());
    System.out.println("[DEBUG] AppServerStub.java - broadcastGroupStatus() - dummy event - GROUPSTATUS");
    System.out.println(sb.toString());
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

  public List<Group> getGroupList() {
    System.out.println("[DEBUG] getGroupList() 호출");

    List<Group> groupList = new ArrayList<>();

    Iterator<CMGroup> groupIterator = this.getCMInfo().getInteractionInfo().getSessionList().iterator().next().getGroupList().iterator();
    while(groupIterator.hasNext())
    {
      CMGroup group = groupIterator.next();
      groupList.add(new Group(group));
    }

    return groupList;
  }

  public String getGroupListString() {
    List<Group> groupList = getGroupList();
    StringBuffer sb = new StringBuffer();
    for(Group group : groupList){
      sb.append(group.toString()+"\n");
    }
    return sb.toString();
  }
}
