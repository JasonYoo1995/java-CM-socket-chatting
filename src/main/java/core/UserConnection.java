package core;

import java.util.Objects;

public class UserConnection {

  public String username;
  public boolean isConnected;

  public UserConnection(String username) {
    this.username = username;
    this.isConnected = false;
  }

  public UserConnection(String username, boolean isConnected) {
    this(username);
    this.isConnected = isConnected;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UserConnection) {
      return this.username.equals(((UserConnection) obj).username);
    }
    return false;
  }

  @Override
  public String toString() {
    return "[" + this.username + ": " + (isConnected ? "로그인" : "로그아웃") + "]";
  }
}
