package views;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class AppServerFrame extends JFrame {

  private JTextPane logTextPane;

  public AppServerFrame() {
    setTitle("NKLCBDTalk Server");
    setSize(500, 500);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    setAppTheme();
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

  private void setAppTheme() {
    LafManager.install(new DarculaTheme());
  }

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
}
