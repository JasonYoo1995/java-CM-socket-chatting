import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

public class AppServer extends JFrame {

  /**
   * Text area for log.
   */
  private JTextPane logTextPane;

  public AppServer() {
    setTitle("NKLCBDTalk Server");
    setSize(500, 500);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setUIFont(new javax.swing.plaf.FontUIResource("Nanum Gothic", Font.PLAIN, 12));

    setMaterialTheme();
    showIPAndPortModal();

    logTextPane = new JTextPane();
    logTextPane.setEditable(false);
    add(logTextPane, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane(logTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollPane);

    /* Test dummy message */
    for (int i = 0; i < 100; i++) {
      addLogMessage("Dummy log " + (i + 1));
    }

    setVisible(true);
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
  private void setUIFont(javax.swing.plaf.FontUIResource f) {
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource) {
        UIManager.put(key, f);
      }
    }
  }

  /**
   * Add server log messages.
   */
  private void addLogMessage(String message) {
    String log = logTextPane.getText() + "\n" + message;
    logTextPane.setText(log.trim());
  }

  /**
   * Set IP, Port modal before starting app server.
   */
  private void showIPAndPortModal() {
    /* Sample IP, port value. */
    String currentServerAddress = "127.0.0.1";
    String savedServerPort = "8001";

    JTextField serverAddressTextField = new JTextField(currentServerAddress);
    JTextField serverPortTextField = new JTextField(savedServerPort);

    Object[] msg = {
        "서버 IP", serverAddressTextField,
        "서버 포트", serverPortTextField
    };

    int option = JOptionPane
        .showConfirmDialog(null, msg, "Server Information", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE, null);

    // TODO: Initialize CM when option is OK.
  }

  public static void main(String[] args) {
    AppServer server = new AppServer();
  }
}
