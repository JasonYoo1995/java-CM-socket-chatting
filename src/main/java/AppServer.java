import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class AppServer extends JFrame {

  /**
   * TextPane for log.
   */
  private JTextPane logTextPane;

  public AppServer() {
    setTitle("NKLCBDTalk Server");
    setSize(500, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    add(makeLogTextPane());

    showIPAndPortModal();

    setVisible(true);
  }

  /**
   * Make scrollable text pane for log.
   */
  private JScrollPane makeLogTextPane() {
    logTextPane = new JTextPane();
    logTextPane.setEditable(false);

    /* Test scrollable text pane.
    for (int i = 0; i < 100; i++) {
      emitLogMessage("Hello World!");
    } */

    return new JScrollPane(logTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  /**
   * Emit server log messages.
   */
  private void emitLogMessage(String message) {
    logTextPane.setText(logTextPane.getText() + message + "\n");
  }

  /**
   * Set IP, Port modal before starting app server.
   */
  private void showIPAndPortModal() {
    String strCurServerAddress = "127.0.0.1";
    String savedServerPort = "8001";

    JTextField serverAddressTextField = new JTextField(strCurServerAddress);
    JTextField serverPortTextField = new JTextField(savedServerPort);

    Object[] msg = {
        "Server Address: ", serverAddressTextField,
        "Server Port: ", serverPortTextField
    };

    int option = JOptionPane
        .showConfirmDialog(null, msg, "Server Information", JOptionPane.OK_CANCEL_OPTION);

    // TODO: Initialize CM.
  }

  public static void main(String[] args) {
    AppServer server = new AppServer();
  }
}
