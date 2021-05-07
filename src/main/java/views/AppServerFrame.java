package views;

import callback.IPPortCallback;
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
  public IPPortCallback ipPortCallback;

  public AppServerFrame() {
    setAppTheme();
  }

  public void init() {
    setTitle("NKLCBDTalk Server");
    setSize(500, 500);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    logTextPane = new JTextPane();
    logTextPane.setEditable(false);
    add(logTextPane, BorderLayout.CENTER);
    JScrollPane scrollPane = new JScrollPane(logTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollPane);

    setVisible(true);
  }

  private void setAppTheme() {
    LafManager.install(new DarculaTheme());
  }

  public void addLogMessage(String message) {
    String log = logTextPane.getText() + "\n" + message;
    logTextPane.setText(log.trim());
  }

  public void showIPAndPortModal(String ip, int port) {
    JTextField serverIPTextField = new JTextField(ip);
    JTextField serverPortTextField = new JTextField(Integer.toString(port));

    Object[] msg = {
        "서버 IP", serverIPTextField,
        "서버 포트", serverPortTextField
    };

    int option = JOptionPane
        .showConfirmDialog(null, msg, "Server Information", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE, null);

    if (option == JOptionPane.OK_OPTION) {
      ipPortCallback.onSuccess();
    } else if (option == JOptionPane.CANCEL_OPTION) {
      ipPortCallback.onFailure();
    }
  }
}
