import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.themes.MaterialOceanicTheme;

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

    showIPAndPortModal();

    try {
      UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

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
        "Server Address: ", serverAddressTextField,
        "Server Port: ", serverPortTextField
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
