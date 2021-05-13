package views;

import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMStub;
import stub.AppClientStub;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ChatPanel extends JPanel {

  /**
   * Chat rooms panel.
   */
  private JPanel chatRoomsPanel;

  /**
   * Chatroom list.
   */
  private JList<String> chatRooms;

  /**
   * Chat room configure panel.
   */
  private JPanel configPanel;

  /**
   * Add chat room button.
   */
  private CircleButton addChatRoomButton;

  public ChatPanel() {
    setLayout(new BorderLayout());
    setConfigPanel();
    setChatRoomsPanel();
  }

  private void setChatRoomsPanel() {
    System.out.println("[DEBUG] setChatRoomsPanel() 호출");
    chatRoomsPanel = new JPanel();
    String[] sampleChatRooms = {
        "협동분산시스템", "분산시스템및컴퓨팅", "소프트웨어V&V", "클라우드IOT서비스", "실전취업특강", "HCI",
        "협동분산시스템", "분산시스템및컴퓨팅", "소프트웨어V&V", "클라우드IOT서비스", "실전취업특강", "HCI",
        "협동분산시스템", "분산시스템및컴퓨팅", "소프트웨어V&V", "클라우드IOT서비스", "실전취업특강", "HCI",
        "협동분산시스템", "분산시스템및컴퓨팅", "소프트웨어V&V", "클라우드IOT서비스", "실전취업특강", "HCI"
    };
    chatRooms = new JList<>(sampleChatRooms);
    chatRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    chatRooms.setFont(new Font("Nanum Gothic", Font.PLAIN, 28));
    JScrollPane scrollPane = new JScrollPane(chatRooms, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    chatRooms.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          String chatRoomTitle = chatRooms.getSelectedValue();
          new ChatRoomFrame(chatRoomTitle);
        }
      }
    });
    chatRoomsPanel.add(scrollPane);
    scrollPane.setPreferredSize(new Dimension(560, 420));
    chatRoomsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(chatRoomsPanel, BorderLayout.WEST);
  }

  private void setConfigPanel() {
    configPanel = new JPanel();
    configPanel.setLayout(new BorderLayout());
    addChatRoomButton = new CircleButton("채팅추가");
    addChatRoomButton.addActionListener(e -> {
      JTextField chatRoomTextField = new JTextField();
      Object[] chatRoomObject = {"채팅방 이름", chatRoomTextField};
      int option = JOptionPane.showConfirmDialog(
          this, chatRoomObject, "채팅방 생성", JOptionPane.OK_CANCEL_OPTION
      );
      // TODO: Update chatRooms if chat room is created successfully.
    });
    configPanel.add(addChatRoomButton, BorderLayout.EAST);
    configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(configPanel, BorderLayout.NORTH);
  }

}
