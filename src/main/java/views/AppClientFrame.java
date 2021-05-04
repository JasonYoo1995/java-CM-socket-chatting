package views;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class AppClientFrame extends JFrame {

  public AppClientFrame() {
    setTitle("NKLCBDTalk Client");
    setSize(600, 600);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    setAppTheme();
    showLoginModal();
    setTabbedPane();

    setVisible(true);
  }

  private void setTabbedPane() {
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
    tabbedPane.add("프로필", new ProfilePanel());
    tabbedPane.add("채팅", new ChatPanel());
    add(tabbedPane);
  }

  private void setAppTheme() {
    LafManager.install(new DarculaTheme());
//    LafManager.install(new IntelliJTheme());
//    LafManager.install(new HighContrastLightTheme());
//    LafManager.install(new HighContrastDarkTheme());
//    LafManager.install(new OneDarkTheme());
//    LafManager.install(new SolarizedLightTheme());
//    LafManager.install(new SolarizedDarkTheme());
  }

  private void showLoginModal() {
    JTextField userNameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    Object[] msg = {
        "아이디", userNameField,
        "비밀번호", passwordField
    };

    int option = JOptionPane
        .showConfirmDialog(null, msg, "Login", JOptionPane.OK_CANCEL_OPTION);

    // TODO: Initialize CM client when option is OK.
  }

}
