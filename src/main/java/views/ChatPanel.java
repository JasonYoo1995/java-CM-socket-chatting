package views;

import callback.*;
import core.Group;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ChatPanel extends JPanel {

  private JPanel chatRoomsPanel;
  private JList<String> chatRooms;
  private JPanel configPanel;
  private CircleButton addChatRoomButton;
  private ChatRoomFrame chatRoomFrame;

  public ArrayList<ChatRoomFrame> chatRoomFrameList = new ArrayList<ChatRoomFrame>();

  public GroupCallback groupCallback;
  public ExitCallback exitCallback;
  public EnterCallback enterCallback;
  public StubCallback stubCallback;

  public ChatPanel() {
    setLayout(new BorderLayout());
    setConfigPanel();
  }

  public void setChatRooms(List<Group> groupList) {
    System.out.println("[DEBUG] setChatRoomsPanel() 호출");
    chatRoomsPanel = new JPanel();
    String[] chatRoomNames = new String[groupList.size()];
    for (int i = 0; i < groupList.size(); i++) {
      chatRoomNames[i] = groupList.get(i).chatRoomName;
    }
    chatRooms = new JList<>(chatRoomNames);
    chatRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    chatRooms.setFont(new Font("Nanum Gothic", Font.PLAIN, 28));
    JScrollPane scrollPane = new JScrollPane(chatRooms, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    chatRooms.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          String chatRoomTitle = chatRooms.getSelectedValue();
          chatRoomFrame = new ChatRoomFrame(chatRoomTitle, exitCallback);
          chatRoomFrameList.add(chatRoomFrame);
          enterCallback.enter(chatRoomTitle);
        }
      }
    });
    chatRoomsPanel.add(scrollPane);
    scrollPane.setPreferredSize(new Dimension(560, 420));
    chatRoomsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    if (this.getComponentCount() == 2) {
      remove(1); // 기존 chatRoomsPanel 제거
    }
    add(chatRoomsPanel, BorderLayout.WEST);

    System.out.println("[DEBUG] GroupList in ChatPanel.java");
    System.out.println(groupList);

    this.revalidate();
    chatRooms.updateUI();
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
      if (option == JOptionPane.YES_OPTION) {
        String chatRoomName = chatRoomTextField.getText();
        groupCallback.onSuccess(chatRoomName);
      }
    });
    configPanel.add(addChatRoomButton, BorderLayout.EAST);
    configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(configPanel, BorderLayout.NORTH);
  }

  public void enterChatRoom(String chatRoomTitle) {
    chatRoomFrame = new ChatRoomFrame(chatRoomTitle, exitCallback);
    chatRoomFrameList.add(chatRoomFrame);
    chatRoomFrame.chatCallback = new ChatCallback() {
      @Override
      public void onSuccess(String chatStr) {
        System.out.println("chatCallback: " + chatStr);
        stubCallback.getStub().chat("/g", chatStr);
      }
    };

    /*chatRoomFrame.chatReceiveCallback = new ChatRoomFrameCallback() {
      @Override
      public void receive(ChatRoomFrame receiveStr) {
        System.out.println("ChatRoomFrameCallback: " + receiveStr);
        chatRoomFrame.addChatMessage(receiveStr);
      }
    };*/
  }

}
