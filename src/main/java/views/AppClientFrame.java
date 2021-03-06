package views;

import callback.LoginCallback;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import core.UserConnection;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class AppClientFrame extends JFrame {

  public LoginCallback loginCallback;
  private ProfilePanel profilePanel;
  public ChatPanel chatPanel;

  public AppClientFrame() {
    setAppTheme();
    chatPanel = new ChatPanel();
  }

  public void init() {
    setTitle("NKLCBDTalk Client");
    setSize(600, 600);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setTabbedPane();
    setVisible(true);
  }

  private void setTabbedPane() {
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
    profilePanel = new ProfilePanel();
    tabbedPane.add("프로필", profilePanel);
    tabbedPane.add("채팅", chatPanel);
    add(tabbedPane);
  }

  private void setAppTheme() {
    LafManager.install(new DarculaTheme());
  }

  public void showLoginModal() {
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    Object[] msg = {
        "아이디", usernameField,
        "비밀번호", passwordField
    };

    int option = JOptionPane
        .showConfirmDialog(null, msg, "Login", JOptionPane.OK_CANCEL_OPTION);

    if (option == JOptionPane.OK_OPTION) {
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());
      loginCallback.onSuccess(username, password);
    } else if (option == JOptionPane.CANCEL_OPTION) {
      loginCallback.onFailure();
    }
  }

  public void updateUserProfile(String username) {
    profilePanel.updateOwnerProfile(username);
  }

  public void updateOthersProfilePanel(UserConnection[] userConnections) {
    profilePanel.updateOthersProfilePanel(userConnections);
  }

}
