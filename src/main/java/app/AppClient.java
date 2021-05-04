package app;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import com.github.weisj.darklaf.theme.IntelliJTheme;
import com.github.weisj.darklaf.theme.OneDarkTheme;
import com.github.weisj.darklaf.theme.SolarizedDarkTheme;
import com.github.weisj.darklaf.theme.SolarizedLightTheme;
import com.github.weisj.darklaf.theme.laf.RenamedTheme;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
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
