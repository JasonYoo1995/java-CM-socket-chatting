package views;

import core.UserConnection;
import utils.UserConnectionRenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ProfilePanel extends JPanel {

  private JPanel ownerPanel;
  private JLabel ownerLabel;
  private JPanel othersPanel;
  private JList<UserConnection> others;

  public ProfilePanel() {
    setLayout(new BorderLayout());
    setOwnerProfilePanel();
    setOthersProfilePanel();
  }

  public void setOwnerProfilePanel() {
    ownerPanel = new JPanel();
    ownerPanel.setLayout(new BorderLayout());
    ownerLabel = new JLabel();
    ownerLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 32));
    ownerPanel.add(ownerLabel, BorderLayout.WEST);
    CircleButton logoutButton = new CircleButton("로그아웃");
    ownerPanel.add(logoutButton, BorderLayout.EAST);
    add(ownerPanel, BorderLayout.NORTH);
    ownerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  public void updateOwnerProfile(String username) {
    ownerLabel.setText(username);
  }

  public void setOthersProfilePanel() {
    othersPanel = new JPanel();
    others = new JList<>();
    others.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    others.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
    others.setCellRenderer(new UserConnectionRenderer());
    JScrollPane scrollPane = new JScrollPane(others, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    othersPanel.add(scrollPane);
    scrollPane.setPreferredSize(new Dimension(560, 420));
    othersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(othersPanel, BorderLayout.WEST);
  }

  public void updateOthersProfilePanel(UserConnection[] userConnections) {
    others.setListData(userConnections);
  }
}
