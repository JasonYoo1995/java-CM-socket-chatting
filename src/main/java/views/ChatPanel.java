package views;

import callback.EnterCallback;
import callback.ExitCallback;
import callback.GroupCallback;
import core.Group;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMStub;
import stub.AppClientStub;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
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

  public GroupCallback groupCallback;
  public ExitCallback exitCallback;
  public EnterCallback enterCallback;

  public ChatPanel() {
    setLayout(new BorderLayout());
    setConfigPanel();
  }

  public void setChatRooms(List<Group> groupList) {
    System.out.println("[DEBUG] setChatRoomsPanel() 호출");
    chatRoomsPanel = new JPanel();
    String[] chatRoomNames = new String[groupList.size()];
    for(int i=0; i<groupList.size(); i++){
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
          new ChatRoomFrame(chatRoomTitle, exitCallback);
          enterCallback.enter(chatRoomTitle);
        }
      }
    });
    chatRoomsPanel.add(scrollPane);
    scrollPane.setPreferredSize(new Dimension(560, 420));
    chatRoomsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    if(this.getComponentCount()==2) remove(1); // 기존 chatRoomsPanel 제거
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
      // TODO: Update chatRooms if chat room is created successfully.
      if (option == JOptionPane.YES_OPTION){
        String chatRoomName = chatRoomTextField.getText();
        groupCallback.onSuccess(chatRoomName);
      }
    });
    configPanel.add(addChatRoomButton, BorderLayout.EAST);
    configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(configPanel, BorderLayout.NORTH);
  }

  public void enterChatRoom(String chatRoomTitle){
    new ChatRoomFrame(chatRoomTitle, exitCallback);
  }

}
