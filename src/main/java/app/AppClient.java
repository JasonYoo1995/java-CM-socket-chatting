package app;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;
import views.ChatPanel;
import views.ProfilePanel;

public class AppClient extends JFrame {

  public AppClient() {
    setTitle("NKLCBDTalk Client");
    setSize(600, 600);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setUIFont(new javax.swing.plaf.FontUIResource("Nanum Gothic", Font.PLAIN, 12));

    setMaterialTheme();
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

  private void setMaterialTheme() {
    try {
      UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  /**
   * Set global font.
   */
  private void setUIFont(FontUIResource f) {
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof FontUIResource) {
        UIManager.put(key, f);
      }
    }
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

  public static void main(String[] args) {
    AppClient client = new AppClient();
  }
}
