package views;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ChatRoomFrame extends JFrame {

  private JTextPane othersChatTextPane;
  private JTextField userChatTextField;

  private ChatRoomFrame() {
  }

  public ChatRoomFrame(String chatRoomTitle) {
    setTitle(chatRoomTitle);
    setSize(600, 600);
    setResizable(false);
    setLayout(new BorderLayout());

    othersChatTextPane = new JTextPane();
    othersChatTextPane.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));

    /* Test dummy message */
    for (int i = 0; i < 100; i++) {
      addChatMessage(i % 2 == 0 ? "신윤섭: 안녕하세요~!\n" : "임민규: 응 반갑다~!\n");
    }

    othersChatTextPane.setEditable(false);
    add(othersChatTextPane, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane(othersChatTextPane,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollPane);

    userChatTextField = new JTextField();
    userChatTextField.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));
    add(userChatTextField, BorderLayout.SOUTH);

    setVisible(true);
  }

  private void addChatMessage(String message) {
    String chat = othersChatTextPane.getText() + "\n" + message;
    othersChatTextPane.setText(chat.trim());
  }

}
