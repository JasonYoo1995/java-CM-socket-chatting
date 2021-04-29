import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;

public class AppClient extends JFrame {

  public AppClient() {
    setTitle("NKLCBDTalk Client");
    setSize(600, 600);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    showLoginModal();

    try {
      UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    setVisible(true);
  }

  private void showLoginModal() {
    JTextField userNameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    Object[] msg = {
        "User Name:", userNameField,
        "Password:", passwordField
    };

    int option = JOptionPane
        .showConfirmDialog(null, msg, "Login", JOptionPane.OK_CANCEL_OPTION);

    // TODO: Initialize CM client when option is OK.
  }

  public static void main(String[] args) {
    AppClient client = new AppClient();
  }
}
