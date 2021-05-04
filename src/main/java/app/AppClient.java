package app;

import javax.swing.JFrame;
import views.AppClientFrame;

public class AppClient {

  private JFrame appFrame;

  public AppClient() {
    appFrame = new AppClientFrame();
  }

  public static void main(String[] args) {
    AppClient client = new AppClient();
  }
}
