package views;

import core.UserConnection;
import core.UserConnectionRenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import mdlaf.components.textarea.MaterialTextAreaUI;

public class ProfilePanel extends JPanel {

  /**
   * Owner profile pane.
   */
  private JPanel ownerPanel;

  /**
   * Owner profile.
   */
  private JLabel ownerLabel;

  /**
   * Other profiles panel.
   */
  private JPanel othersPanel;

  /**
   * Other profiles.
   */
  private JList<UserConnection> others;

  public ProfilePanel() {
    setLayout(new BorderLayout());
    setOwnerProfilePanel();
    setOthersProfilePanel();
  }

  private void setOwnerProfilePanel() {
    ownerPanel = new JPanel();
    ownerPanel.setLayout(new BorderLayout());
    ownerLabel = new JLabel();
    ownerLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 32));
    ownerLabel.setText("신윤섭");
    ownerPanel.add(ownerLabel, BorderLayout.WEST);
    CircleButton logoutButton = new CircleButton("로그아웃");
    ownerPanel.add(logoutButton, BorderLayout.EAST);
    add(ownerPanel, BorderLayout.NORTH);
    ownerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  private void setOthersProfilePanel() {
    othersPanel = new JPanel();
    UserConnection[] sampleUsers = {
        new UserConnection("유경원", true),
        new UserConnection("유진욱", true),
        new UserConnection("한지희", true),
        new UserConnection("임민규", false)
    };
    others = new JList<>(sampleUsers);
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
}
