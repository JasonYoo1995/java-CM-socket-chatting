package views;

import callback.ChatCallback;
import callback.ExitCallback;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class ChatRoomFrame extends JFrame {

  private JTextPane othersChatTextPane;
  private JTextField userChatTextField;
  public ExitCallback exitCallback;

  public String title = "";

  public ChatCallback chatCallback;

  private ChatRoomFrame() {
  }

  public ChatRoomFrame(String chatRoomTitle, ExitCallback exitCallback) {
    this.exitCallback = exitCallback;
    this.title = chatRoomTitle;

    setTitle(title);
    setSize(600, 600);
    setResizable(false);
    setLayout(new BorderLayout());

    othersChatTextPane = new JTextPane();
    othersChatTextPane.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));

//    /* Test dummy message */
//    for (int i = 0; i < 100; i++) {
//      addChatMessage(i % 2 == 0 ? "신윤섭: 안녕하세요~!\n" : "임민규: 응 반갑다~!\n");
//    }

    othersChatTextPane.setEditable(false);
    add(othersChatTextPane, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane(othersChatTextPane,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollPane);

    userChatTextField = new JTextField();
    userChatTextField.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));
    add(userChatTextField, BorderLayout.SOUTH);

    Action chatEnter = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String chatStr = userChatTextField.getText();
            System.out.println("ChatRoomFrame Enter Action: " + chatStr);
            userChatTextField.setText("");
            chatCallback.onSuccess(chatStr);
        }
    };

    KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
    userChatTextField.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
    userChatTextField.getActionMap().put("ENTER", chatEnter);

    setVisible(true);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exitCallback.exit();
      }
    });
  }

  public void addChatMessage(String message) {
    String chat = othersChatTextPane.getText() + "\n" + message;
    othersChatTextPane.setText(chat.trim());
  }
}
