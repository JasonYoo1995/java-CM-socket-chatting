package app;

import javax.swing.JFrame;
import views.AppServerFrame;

public class AppServer {

  private JFrame appFrame;

  public AppServer() {
    appFrame = new AppServerFrame();
  }

  public static void main(String[] args) {
    AppServer server = new AppServer();
  }
}
