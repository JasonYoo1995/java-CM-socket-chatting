package core;

public class UserConnection {

  public String username;
  public boolean isConnected;

  public UserConnection(String username) {
    this.username = username;
  }

  public UserConnection(String username, boolean isConnected) {
    this(username);
    this.isConnected = isConnected;
  }

  @Override
  public String toString() {
    return "[" + this.username + ": " + (isConnected ? "로그인" : "로그아웃") + "]";
  }
}
