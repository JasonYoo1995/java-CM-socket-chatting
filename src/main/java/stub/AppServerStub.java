package stub;

import core.UserConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class AppServerStub extends CMServerStub {

  public AppServerStub() {
    super();
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

}
